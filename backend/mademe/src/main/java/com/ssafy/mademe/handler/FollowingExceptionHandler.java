package com.ssafy.mademe.handler;

import com.ssafy.mademe.exception.following.FailedDelFollowingException;
import com.ssafy.mademe.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.ssafy.mademe.handler.ExceptionHandlerTool.makeErrorResponse;

@RestControllerAdvice
public class FollowingExceptionHandler {
    @ExceptionHandler(FailedDelFollowingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> failedDelFollowingExceptionHandler(FailedDelFollowingException e){
        return makeErrorResponse(e, "userid");
    }
}
