package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.payload.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>("ACCOUNT_DISABLED"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>("BAD_CREDENTIALS"));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<?> handleAccountExpiredException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>("ACCOUNT_EXPIRED"));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleAccountLockedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>("ACCOUNT_LOCKED"));
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<?> handleCredentialExpiredException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>("CREDENTIALS_EXPIRED"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return ResponseEntity.badRequest().body(new ServerResponse<>("VALIDATION_ERROR", errors));
    }
}
