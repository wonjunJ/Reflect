package com.ssafy.mademe.exception.user;

public class UserNotQualifiedException extends RuntimeException{
    public UserNotQualifiedException(String msg){
        super(msg);
    }
}
