package com.ssafy.mademe.exception.user;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String msg){
        super(msg);
    }
}
