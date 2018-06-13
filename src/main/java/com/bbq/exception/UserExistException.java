package com.bbq.exception;

public class UserExistException extends RuntimeException {

    public UserExistException(String errorMsg) {
        super(errorMsg);
    }
}
