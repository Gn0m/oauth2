package com.example.oauth2.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<Error> handleAuthenticationException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.UNAUTHORIZED.value(), "Username or password is incorrect. " + e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<Error> handleAccountStatusException(AccountStatusException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.UNAUTHORIZED.value(), "User account is abnormal. " + e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Error> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new Error(HttpStatus.FORBIDDEN.value(), "No permission. " + e.getMessage()),
                HttpStatus.FORBIDDEN);
    }


}
