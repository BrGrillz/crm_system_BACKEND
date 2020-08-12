package com.aegis.crmsystem.exceptions;

public class ApiRequestExceptionUnauthorized extends RuntimeException{
    public ApiRequestExceptionUnauthorized(String message) {
        super(message);
    }
}
