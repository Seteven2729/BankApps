package com.example.bankapps.exception;

import com.example.bankapps.model.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR = "error";

    // ! Feign exceptions
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponseDto> handleFeignException(FeignException e) {
        int status = e.status();
        String responseBody = e.contentUTF8(); // get raw response body
        String message;
        log.error(e.getMessage(),e);
        // get error inside feign error response body
        try {
            // Attempt to parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(responseBody);

            if (node.isArray() && !node.isEmpty() && node.get(0).has(ERROR)) {
                message = node.get(0).get(ERROR).asText();
            } else if (node.has(ERROR)) {
                message = node.get(ERROR).asText();
            } else if (node.has("errorMessage"))
            {
                message = node.get("errorMessage").asText();
            }
            else {
                message = responseBody; // fallback
            }
        } catch (Exception ex) {
            message = responseBody; // fallback if parsing fails
        }


        return switch (status) {
            case 400 -> ResponseEntity.badRequest().body(new ErrorResponseDto("Invalid request", message));
            case 401 -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto("Unauthorized" , message));
            case 404 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Not Found" , message));
            case 409 -> ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto("Conflict" , message));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto("Internal error", message));
        };
    }

    // ! token retrieval failure
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("error" , e.getMessage()));
    }

    // ! database errors
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleDatabaseException(DataAccessException e) {
        log.error(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Database error" , e.getMessage()));
    }

    // ! invalid input
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(f -> f.getDefaultMessage() != null) // skip null messages
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> existing, // if duplicate field, keep first
                        LinkedHashMap::new
                ));
        log.error("Validation failed: {}",errors);

        String message = errors.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey()+": "+entry.getValue())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ErrorResponseDto("Validation failed",message));
    }

    // ! Fallback uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {
        log.error(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Unexpected error" , e.getMessage()));
    }

}
