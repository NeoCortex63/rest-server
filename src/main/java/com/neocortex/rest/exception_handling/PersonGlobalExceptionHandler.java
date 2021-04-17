package com.neocortex.rest.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PersonGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<PersonIncorrectDate> handleException(NoSuchPersonException exception){
        PersonIncorrectDate data = new PersonIncorrectDate();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PersonIncorrectDate> handleException(Exception exception){
        PersonIncorrectDate data = new PersonIncorrectDate();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
