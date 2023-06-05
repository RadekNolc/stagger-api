package cz.radeknolc.stagger.controller;

import cz.radeknolc.stagger.model.payload.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static cz.radeknolc.stagger.model.payload.ServerResponseMessage.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ServerResponse<Void>> handleDisabledException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_ACCOUNT_DISABLED));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ServerResponse<Void>> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_BAD_CREDENTIALS));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ServerResponse<Void>> handleAccountExpiredException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_ACCOUNT_EXPIRED));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ServerResponse<Void>> handleAccountLockedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_ACCOUNT_LOCKED));
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ServerResponse<Void>> handleCredentialExpiredException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ServerResponse<>(AUTH_CREDENTIALS_EXPIRED));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return ResponseEntity.badRequest().body(new ServerResponse<>(VALIDATION_ERROR, errors));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ServerResponse<Void>> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ServerResponse<>(AUTH_ACCESS_DENIED));
    }
}
