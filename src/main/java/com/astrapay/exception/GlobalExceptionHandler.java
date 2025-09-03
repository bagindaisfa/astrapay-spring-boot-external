package com.astrapay.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Data integrity violation";
        String rootCauseMessage = ex.getRootCause() != null ? 
            ex.getRootCause().getMessage() :
            ex.getMostSpecificCause().getMessage();
        if (rootCauseMessage != null) {
            message = rootCauseMessage;
        }
        return createErrorResponse(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public final ResponseEntity<Object> handleNoteNotFound(NoteNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request");
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName() != null ? ex.getName() : "<unknown>";
        String typeName = ex.getRequiredType() != null ? 
            ex.getRequiredType().getSimpleName() : 
            "<unknown type>";
        String message = String.format("Parameter '%s' should be of type %s", paramName, typeName);
        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String message = String.format("Required parameter '%s' is not present", ex.getParameterName());
        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method not supported for this endpoint");
    }
    
    @ExceptionHandler(BindException.class)
    public final ResponseEntity<Object> handleBindException(BindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.joining("; "));
        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
            .map(violation -> String.format("%s: %s", 
                violation.getPropertyPath().toString(),
                violation.getMessage()))
            .collect(Collectors.joining("; "));
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation error: " + message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.joining("; "));
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation error: " + message);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<Object> handleResponseStatus(ResponseStatusException ex) {
        return createErrorResponse(ex.getStatus(), ex.getReason() != null ? ex.getReason() : ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        // Log the full exception for debugging
        ex.printStackTrace();
        
        // Return a generic error message to the client
        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "An unexpected error occurred. Please try again later."
        );
    }

    private ResponseEntity<Object> createErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", Instant.now().toString());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }
}

