package com.ruanyouzhi.estate.estate.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"你找的问题不见了，要不换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何评论或回复进行回复"),
    NO_LOGIN(2003,"未登录，不能进行评论"),
    SYS_ERROR(2004,"服务器开小差了，请稍后试试"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你操作的评论不存在"),
    CONTENT_IS_EMPTY(2007,"内容不能为空");
    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
