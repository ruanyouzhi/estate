package com.ruanyouzhi.estate.estate.exception;

public class CustomizeException extends RuntimeException{//为了不写try catch所以继承这个
    private String message;
    private Integer code;
    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code =errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
