package com.kxnvg.taskmanagement.exception.handler;

import com.kxnvg.taskmanagement.exception.IncorrectAuthenticationException;
import com.kxnvg.taskmanagement.exception.IncorrectUserActionException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleIncorrectAuthenticationException(IncorrectAuthenticationException e) {
        log.error("Incorrect authenticate", e);
        return new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
    }

    @ExceptionHandler(IncorrectUserActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleIncorrectUserActionException(IncorrectUserActionException e) {
        log.error("Incorrect user action", e);
        return new ErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN, LocalDateTime.now());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found", e);
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAuthenticateException(AuthenticationException e) {
        log.error("EAuthenticate exception", e);
        return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSQLException(SQLException e) {
        log.error("SQL Exception", e);
        return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSQLException(Exception e) {
        log.error("EXCEPTION", e);
        return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }
}
