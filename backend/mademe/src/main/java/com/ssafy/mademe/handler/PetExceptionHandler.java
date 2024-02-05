package com.ssafy.mademe.handler;

import com.ssafy.mademe.exception.diary.DiaryNotFoundException;
import com.ssafy.mademe.exception.pet.DuplicatePetException;
import com.ssafy.mademe.exception.pet.PetNotFoundException;
import com.ssafy.mademe.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.ssafy.mademe.handler.ExceptionHandlerTool.makeErrorResponse;

@RestControllerAdvice
public class PetExceptionHandler {
    @ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<ErrorResponse> petNotFoundExceptionHandler(PetNotFoundException e){
        return makeErrorResponse(e, "my pet");
    }

    @ExceptionHandler(DuplicatePetException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public List<ErrorResponse> duplicatePetExceptionHandler(DuplicatePetException e){
        return makeErrorResponse(e, "my pet");
    }
}
