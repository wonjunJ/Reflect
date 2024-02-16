package com.ssafy.mademe.handler;

import com.ssafy.mademe.exception.question.QAnswerNotFoundException;
import com.ssafy.mademe.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.ssafy.mademe.handler.ExceptionHandlerTool.makeErrorResponse;

@RestControllerAdvice
public class QuestionExceptionHandler {
    @ExceptionHandler(QAnswerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<ErrorResponse> qAnswerNotFoundExceptionHandler(QAnswerNotFoundException e){
        return makeErrorResponse(e, "question");
    }
}
