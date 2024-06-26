package com.awbd.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
