package com.ssafy.mademe.exception.user;

public class InvalidCodeException extends RuntimeException{
    public InvalidCodeException(String msg){
        super(msg);
    }
}
