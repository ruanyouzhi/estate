package com.ruanyouzhi.estate.estate.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND("你找的问题不见了，要不换个试试？");
    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }
@Override
    public String getMessage() {
        return message;
    }
}
