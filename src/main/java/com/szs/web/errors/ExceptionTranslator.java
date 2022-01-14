package com.szs.web.errors;

import com.szs.service.ScrapSaveFailException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator {

    private final Environment env;

    public ExceptionTranslator(Environment env) {
        this.env = env;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorDTO errorDTO = new ErrorDTO("Method Argument Not Valid", "method argument not valid", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorDTO errorDTO = new ErrorDTO("Access Denied", "access denied", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadCredentialsException.class, UserInfoNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleUnAuthorizedException() {
        ErrorDTO errorDTO = new ErrorDTO("Not Authorized", "authorized user session not found", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleScrapNotFoundException(ScrapNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO("Scrap Not Found", "can't find scrap info", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleScrapSaveFailException(ScrapSaveFailException ex) {
        ErrorDTO errorDTO = new ErrorDTO("Scrap Save Fail", "can't save scrap info", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleScrapSaveFailException(LoginAlreadyUsedException ex) {
        ErrorDTO errorDTO = new ErrorDTO("Login Already Used", "user id duplicate", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleGlobalException(Exception ex) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
