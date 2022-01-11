package com.szs.web.errors;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator {

    private final Environment env;

    public ExceptionTranslator(Environment env) {
        this.env = env;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleUnAuthorizedException(UnAuthorizedException ex) {
        ErrorDTO errorDTO = new ErrorDTO("Not Authorized", "authorized user session not found", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleScrapNotFoundException(ScrapNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO("Scrap Not Found", "can't find scrap info", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleGlobalException(Exception ex) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
