package ch.admin.seco.jobroom.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ JpaSystemException.class})
    protected ResponseEntity handleJpaSystemException(RuntimeException ex, WebRequest request) {

        String bodyOfResponse = ex.getCause().getCause().getMessage();
        return handleExceptionInternal(ex, ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(bodyOfResponse), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
