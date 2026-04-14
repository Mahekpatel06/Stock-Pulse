package com.ownProject.GINS.exception;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionDetails> handleAllException(Exception ex, WebRequest request) throws Exception {
		ExceptionDetails errorDetails = new ExceptionDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<ExceptionDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public final ResponseEntity<ExceptionDetails> handleUserNotFoundException(Exception ex, WebRequest request) throws Exception {
		ExceptionDetails errorDetails = new ExceptionDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<ExceptionDetails>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {

// You can customize the message here to show ALL errors
        StringBuilder details = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            details.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );

        ExceptionDetails errorDetails = new ExceptionDetails(
            LocalDateTime.now(), 
            details.toString(), 
            request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(OptimisticLockingFailureException.class)
	public final ResponseEntity<ExceptionDetails> handleOptimisticLockingException(OptimisticLockingFailureException ex) throws Exception {
		ExceptionDetails errorDetails = new ExceptionDetails(LocalDateTime.now(),
							"The inventory was updated by another user or process. Please refresh and try again.",
							"Conflict (409)");
		
		return new ResponseEntity<ExceptionDetails>(errorDetails, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
	    return ResponseEntity.badRequest().body("Validation Error: " + ex.getMessage());
	}

}
