package com.ssafy.mademe.exception.diary;

public class DiaryNotFoundException extends RuntimeException {
    public DiaryNotFoundException(String msg){
        super(msg);
    }
}
