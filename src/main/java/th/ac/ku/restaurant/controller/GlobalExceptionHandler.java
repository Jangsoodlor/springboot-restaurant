package th.ac.ku.restaurant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.IOException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        logger.error("Input Error: {}", errors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundExceptions(
            EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleEntityExistsExceptions(
            EntityExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public boolean handleSecurityException(SecurityException e) {
        logger.error("Invalid JWT signature: " + e.getMessage());
        return false;
    }

    @ExceptionHandler(MalformedJwtException.class)
    public boolean handleMalformedJwtException(MalformedJwtException e) {
        logger.error("Invalid JWT token: " + e.getMessage());
        return false;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public boolean handleExpiredJwtException(ExpiredJwtException e) {
        logger.error("JWT token is expired: " + e.getMessage());
        return false;
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public boolean handleUnsupportedJwtException(UnsupportedJwtException e) {
        logger.error("JWT token is unsupported: " + e.getMessage());
        return false;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public boolean handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("JWT claims string is empty: " + e.getMessage());
        return false;
    }

    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e) {
        logger.error("Cannot set user authentication: " + e);
    }

    @ExceptionHandler(ServletException.class)
    public void handleServletException(ServletException e) {
        logger.error("Cannot set user authentication: " + e);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleUsernameNotFoundException(UsernameNotFoundException e) {
        logger.error("Cannot set user authentication: " + e);
    }

}
