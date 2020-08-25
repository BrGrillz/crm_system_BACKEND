package com.aegis.crmsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestExceptionNotFound.class})
    public ResponseEntity<Object> handlerApiRequestExceptionNotFound(ApiRequestExceptionNotFound error){
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                error.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {ApiRequestExceptionUnauthorized.class})
    public ResponseEntity<Object> handlerApiRequestExceptionUnauthorized(ApiRequestExceptionUnauthorized error){
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiException apiException = new ApiException(
                error.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {ApiRequestExceptionBadRequest.class})
    public ResponseEntity<Object> handlerApiRequestExceptionBadRequest(ApiRequestExceptionBadRequest error){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                error.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {ApiRequestExceptionAccessDenied.class})
    public ResponseEntity<Object> handlerApiRequestExceptionAccessDenied(ApiRequestExceptionAccessDenied error){
        HttpStatus status = HttpStatus.FORBIDDEN;

        ApiException apiException = new ApiException(
                error.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, status);
    }
}
