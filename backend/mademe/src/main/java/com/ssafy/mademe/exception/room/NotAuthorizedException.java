package com.ssafy.mademe.exception.room;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String msg){
        super(msg);
    }
}
