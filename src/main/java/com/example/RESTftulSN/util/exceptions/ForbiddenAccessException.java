package com.example.RESTftulSN.util.exceptions;

public class ForbiddenAccessException extends RuntimeException{
    public ForbiddenAccessException(String message){
        super(message);
    }
}
