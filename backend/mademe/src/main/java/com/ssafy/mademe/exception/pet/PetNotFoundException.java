package com.ssafy.mademe.exception.pet;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(String msg){
        super(msg);
    }
}
