package com.ssafy.mademe.handler;

import com.ssafy.mademe.exception.diary.DiaryNotFoundException;
import com.ssafy.mademe.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.ssafy.mademe.handler.ExceptionHandlerTool.makeErrorResponse;

@RestControllerAdvice
public class DiaryExceptionHandler {
    @ExceptionHandler(DiaryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<ErrorResponse> DiaryNotFoundExceptionHandler(DiaryNotFoundException e){
        return makeErrorResponse(e, "diary");
    }
}
