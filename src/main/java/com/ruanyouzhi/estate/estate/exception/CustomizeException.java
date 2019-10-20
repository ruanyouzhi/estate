package com.ruanyouzhi.estate.estate.exception;

public class CustomizeException extends RuntimeException{//为了不写try catch所以继承这个
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }
    public CustomizeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
