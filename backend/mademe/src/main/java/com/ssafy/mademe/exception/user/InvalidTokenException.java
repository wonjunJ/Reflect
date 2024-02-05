package com.ssafy.mademe.exception.user;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String msg){
        super(msg);
    }
}
