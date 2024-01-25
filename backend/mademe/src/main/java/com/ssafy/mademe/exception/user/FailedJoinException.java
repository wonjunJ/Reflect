package com.ssafy.mademe.exception.user;

public class FailedJoinException extends RuntimeException{
    public FailedJoinException(String msg){
        super(msg);
    }
}
