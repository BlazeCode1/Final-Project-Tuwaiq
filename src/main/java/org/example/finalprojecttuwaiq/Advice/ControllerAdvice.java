package org.example.finalprojecttuwaiq.Advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.Api.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.PlaceholderResolutionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    //handles any apiException
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<?> ApiException(ApiException e){
        String message = e.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    //handles Jakarta Validation exceptions
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        String message = methodArgumentNotValidException.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    //handles sql constrains exception
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> SQlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException){
        String message = sqlIntegrityConstraintViolationException.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }


    //method not allowed
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> methodNotAllowed(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException){
        String message = httpRequestMethodNotSupportedException.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }


    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> MethodArgsTypeMismatch(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException){
        String message = methodArgumentTypeMismatchException.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }


    @ExceptionHandler(value = PlaceholderResolutionException.class)
    public ResponseEntity<?> PlaceholderResolutionException(PlaceholderResolutionException placeholderResolutionException){
        String message = placeholderResolutionException.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<?> IOException(IOException ioException){
        String message = ioException.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<?> DataIntegrityViolationException( DataIntegrityViolationException dataIntegrityViolationException){
        String message = dataIntegrityViolationException.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }


    @ExceptionHandler(value = JsonProcessingException.class)
    public ResponseEntity<?> JsonProcessingException(JsonProcessingException jsonProcessingException){
        String message = jsonProcessingException.getMessage();
        return ResponseEntity.status(400).body(new ApiResponse(message));
    }



}
