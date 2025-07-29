package com.skillbridge.exception;

public class LoggedInUserException extends RuntimeException {
    public LoggedInUserException(String message){
        super(message);
    }
}
