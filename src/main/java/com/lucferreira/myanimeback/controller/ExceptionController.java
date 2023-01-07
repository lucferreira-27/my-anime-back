package com.lucferreira.myanimeback.controller;

import com.lucferreira.myanimeback.util.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorCode> handleResponseStatusException(ResponseStatusException e){
        ErrorCode errorCode = new ErrorCode(e);
        return ResponseEntity.status(errorCode.getCode()).body(errorCode);
    }
}
