package com.sw.userauthservice.exceptions;

public class UserNotSignedUpException extends RuntimeException{
    public UserNotSignedUpException(String message){
        super(message);
    }
}
