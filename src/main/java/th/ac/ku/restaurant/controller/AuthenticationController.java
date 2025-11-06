package th.ac.ku.restaurant.controller;

import th.ac.ku.restaurant.dto.SignupRequest;
import th.ac.ku.restaurant.security.JwtUtil;
import th.ac.ku.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import th.ac.ku.restaurant.dto.LoginRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtils;

    @Autowired
    public AuthenticationController(UserService userService,
            AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("yes");
        return ResponseEntity.ok(jwtUtils.generateToken(userDetails.getUsername()));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest request) {

        if (userService.userExists(request.getUsername()))
            throw new EntityExistsException("Error: Username is already taken!");
        ;

        userService.createUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }
}
