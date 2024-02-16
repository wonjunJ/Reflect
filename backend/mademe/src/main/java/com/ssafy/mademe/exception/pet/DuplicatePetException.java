package com.ssafy.mademe.exception.pet;

public class DuplicatePetException extends RuntimeException {
    public DuplicatePetException(String msg){
        super(msg);
    }
}
