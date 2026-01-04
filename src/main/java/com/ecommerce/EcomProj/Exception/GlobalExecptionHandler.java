package com.ecommerce.EcomProj.Exception;

import com.ecommerce.EcomProj.PayLoad.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExecptionHandler {

    ErrorResponse errorResponse = new ErrorResponse();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String mess = err.getDefaultMessage();
            response.put(fieldName, mess);

        });

        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> myConstraintViolationException(jakarta.validation.ConstraintViolationException e){
        Map<String, String> response = new HashMap<>();

        e.getConstraintViolations().forEach(violation -> {
            String fieldPath = violation.getPropertyPath().toString();  // e.g. "addCategory.categoryDTORequest.categoryName"
            String mess = violation.getMessage();
            response.put(fieldPath, mess);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> myResourseNotFoundException(ResourceNotFound e){
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> myAPIExceptionHandler(APIException e){
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
