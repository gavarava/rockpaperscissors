package com.rps.infrastructure;

import com.rps.application.RPSException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RPSExceptionController {

  @ExceptionHandler(value = RPSException.class)
  public ResponseEntity<Object> exception(RPSException rpsException) {
    return new ResponseEntity<>(rpsException.getMessage(),
        HttpStatus.BAD_REQUEST);
  }
}
