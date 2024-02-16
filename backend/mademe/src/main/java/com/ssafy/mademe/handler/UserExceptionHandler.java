package com.ssafy.mademe.handler;

import com.ssafy.mademe.exception.user.*;
import com.ssafy.mademe.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.ssafy.mademe.handler.ExceptionHandlerTool.makeErrorResponse;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(DuplicateUserIdException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public List<ErrorResponse> duplicateUserIdExceptionHandler(DuplicateUserIdException e){
        return makeErrorResponse(e, "userid");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<ErrorResponse> userNotFoundExceptionHandler(UserNotFoundException e){
        return makeErrorResponse(e, "userid");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> invalidPasswordExceptionHandler(InvalidPasswordException e){
        return makeErrorResponse(e, "password");
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<ErrorResponse> userIdNotFoundExceptionHandler(UserIdNotFoundException e){
        return makeErrorResponse(e, "userid");
    }

    @ExceptionHandler(UserNotQualifiedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public List<ErrorResponse> userNotQualifiedExceptionHandler(UserNotQualifiedException e){
        return makeErrorResponse(e, "userId");
    }

    @ExceptionHandler(FailedJoinException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> failedJoinExceptionHandler(FailedJoinException e){
        return makeErrorResponse(e, "");
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public List<ErrorResponse> invalidTokenExceptionHandler(InvalidTokenException e){
        return makeErrorResponse(e, "token");
    }

    @ExceptionHandler(AlreadyLogoutException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> alreadyLogoutExceptionHandler(AlreadyLogoutException e){
        return makeErrorResponse(e, "token");
    }

    @ExceptionHandler(AlreadyAttendanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> alreadyAttendanceExceptionHandler(AlreadyAttendanceException e){
        return makeErrorResponse(e, "checked");
    }

    @ExceptionHandler(InvalidCodeException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public List<ErrorResponse> invalidCodeExceptionHandler(InvalidCodeException e){
        return makeErrorResponse(e, "invalid code");
    }

    @ExceptionHandler(TimeOutCodeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public List<ErrorResponse> timeOutCodeExceptionHandler(TimeOutCodeException e){
        return makeErrorResponse(e, "timeout code");
    }

    @ExceptionHandler(NotEnoughPointException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public List<ErrorResponse> notEnoughPointExceptionHandler(NotEnoughPointException e){
        return makeErrorResponse(e, "not enough point");
    }
}
