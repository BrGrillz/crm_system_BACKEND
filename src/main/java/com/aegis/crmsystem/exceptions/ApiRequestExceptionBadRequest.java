package com.aegis.crmsystem.exceptions;

public class ApiRequestExceptionBadRequest extends RuntimeException{
    public ApiRequestExceptionBadRequest(String message) {
        super(message);
    }
}
