package com.awbd.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseErrorException  extends BaseException{
    public DatabaseErrorException(String message){
        super(message);
    }
}
