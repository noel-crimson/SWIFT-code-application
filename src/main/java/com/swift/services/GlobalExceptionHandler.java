package com.swift.services;

import com.swift.DTO.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResponseStatusException.class) // Handles known validation errors
    public ResponseEntity<MessageDTO> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new MessageDTO(ex.getReason()));
    }

    @ExceptionHandler(Exception.class) // Handles all other unexpected exceptions
    public ResponseEntity<MessageDTO> handleUnexpectedException(Exception ex) {
        // Log the error for debugging
        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDTO("An unexpected error occurred. Please try again later."));
    }
}
