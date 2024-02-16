package com.ssafy.mademe.exception.user;

public class AlreadyLogoutException extends RuntimeException{
    public AlreadyLogoutException(String msg){
        super(msg);
    }
}
