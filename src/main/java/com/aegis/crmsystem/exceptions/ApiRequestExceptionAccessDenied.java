package com.aegis.crmsystem.exceptions;

public class ApiRequestExceptionAccessDenied extends RuntimeException{
    public ApiRequestExceptionAccessDenied(String message) {
        super(message);
    }
}
