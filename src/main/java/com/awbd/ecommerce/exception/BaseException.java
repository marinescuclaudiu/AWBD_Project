package com.awbd.ecommerce.exception;

public class BaseException extends RuntimeException{
    public BaseException(){

    }

    public BaseException(String message){
        super(message);
    }

    public BaseException(String message, Throwable throwable){
        super(message, throwable);
    }
}
