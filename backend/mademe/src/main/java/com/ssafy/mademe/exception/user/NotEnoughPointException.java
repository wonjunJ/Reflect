package com.ssafy.mademe.exception.user;

public class NotEnoughPointException extends RuntimeException {
    public NotEnoughPointException(String msg){
        super(msg);
    }
}
