package com.ssafy.mademe.exception.user;

public class AlreadyAttendanceException extends RuntimeException {
    public AlreadyAttendanceException(String msg){
        super(msg);
    }
}
