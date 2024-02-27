package com.pblintern.web.Advices;

import com.pblintern.web.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(BadCredentialsException e){
        List<String> messages = new ArrayList<>(1);
        messages.add(e.getMessage());
        return new ErrorResponse(false , HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED,messages) ;
        }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e){
        List<String> messages = new ArrayList<>(1);
        messages.add(e.getMessage());
        return new ErrorResponse(false, HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND,messages);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e){
        List<String>  messages = new ArrayList<>(1);
        messages.add(e.getMessage());
        return new ErrorResponse(false, HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST, messages);
    }


    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFileStorageException(FileStorageException e){
        List<String> messages = new ArrayList<>(1);
        messages.add(e.getMessage());
        return new ErrorResponse(false, HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND, messages);
    }
}
