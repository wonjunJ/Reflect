package com.ssafy.mademe.handler;

import com.ssafy.mademe.exception.room.NotAuthorizedException;
import com.ssafy.mademe.exception.room.RoomNotFoundException;
import com.ssafy.mademe.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.ssafy.mademe.handler.ExceptionHandlerTool.makeErrorResponse;

@RestControllerAdvice
public class RoomExceptionHandler {
    @ExceptionHandler(RoomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<ErrorResponse> roomNotFoundExceptionHandler(RoomNotFoundException e){
        return makeErrorResponse(e, "room");
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public List<ErrorResponse> notAuthorizedExceptionHandler(NotAuthorizedException e){
        return makeErrorResponse(e, "room");
    }
}
