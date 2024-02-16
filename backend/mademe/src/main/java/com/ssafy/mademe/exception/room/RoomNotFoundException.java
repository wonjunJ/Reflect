package com.ssafy.mademe.exception.room;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String msg){
        super(msg);
    }
}
