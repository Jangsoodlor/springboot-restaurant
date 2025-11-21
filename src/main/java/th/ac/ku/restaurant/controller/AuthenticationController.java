package th.ac.ku.restaurant.controller;

import th.ac.ku.restaurant.dto.SignupRequest;
import th.ac.ku.restaurant.security.JwtUtil;
import th.ac.ku.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;

import th.ac.ku.restaurant.dto.LoginRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import jakarta.servlet.http.Cookie;
import th.ac.ku.restaurant.dto.UserInfoResponse;
import th.ac.ku.restaurant.entity.User;

import org.springframework.beans.factory.annotation.Value;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import th.ac.ku.restaurant.dto.GoogleAuthRequest;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Value("${google.clientId}")
    private String googleClientId;

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtils;

    public static final String AUTH_COOKIE_NAME = "token";

    @Autowired
    public AuthenticationController(UserService userService,
            AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails.getUsername());

        // Create HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, token)
                .httpOnly(true) // Javascript cannot read cookie
                .secure(true) // HTTPS only
                .path("/")
                .maxAge(60 * 60) // 1 hour
                .sameSite("Strict")
                .build();

        // Return cookie in response headers, optional JSON body
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                        "message", "Successfully logged in"));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest request) {

        if (userService.userExists(request.getUsername()))
            throw new EntityExistsException("Error: Username is already taken!");
        ;

        userService.createUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);
        if (token == null) {
            return ResponseEntity.status(401).body("No auth token");
        }

        String username = jwtUtils.getUsernameFromToken(token);
        if (username == null) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        return ResponseEntity.ok(new UserInfoResponse(username));
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = extractTokenFromCookie(request);

        if (token != null)
            jwtUtils.invalidateToken(token);

        // Clear cookie
        ResponseCookie cleared = ResponseCookie.from(AUTH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // expires immediately
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cleared.toString());

        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleAuthRequest request) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()).setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(request.credential);

        if (idToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String email = idToken.getPayload().getEmail();
        String name = (String) idToken.getPayload().get("name");

        User user = userService.findOrCreateGoogleUser(email, name);
        String token = jwtUtils.generateToken(user.getUsername());

        // Create session cookie
        ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(60 * 60) // 1 hour
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                        "message", "Successfully logged in using Google"));
    }

}
