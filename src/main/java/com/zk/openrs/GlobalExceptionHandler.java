package com.zk.openrs;

import com.zk.openrs.pojo.ExceptionResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ExceptionResponse usernameNotFoundExceptionHandler(){
        return new ExceptionResponse(40400,"UsernameNotFoundException");
    }
}
