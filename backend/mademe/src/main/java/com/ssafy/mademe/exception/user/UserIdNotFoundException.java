package com.ssafy.mademe.exception.user;

public class UserIdNotFoundException extends RuntimeException{
    public UserIdNotFoundException(String msg){
        super(msg);
    }
}
