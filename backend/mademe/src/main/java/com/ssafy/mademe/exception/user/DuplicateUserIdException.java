package com.ssafy.mademe.exception.user;

public class DuplicateUserIdException extends RuntimeException{
    public DuplicateUserIdException(String msg){
        super(msg);
    }
}
