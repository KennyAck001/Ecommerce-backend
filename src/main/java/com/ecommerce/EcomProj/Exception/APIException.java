package com.ecommerce.EcomProj.Exception;

public class APIException extends RuntimeException{

    public APIException(String message) {
        super(message);
    }

    public APIException() {
    }
}
