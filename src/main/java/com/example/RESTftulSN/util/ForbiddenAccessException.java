package com.example.RESTftulSN.util;

public class ForbiddenAccessException extends RuntimeException{
    public ForbiddenAccessException(String message){
        super(message);
    }
}
