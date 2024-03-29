package com.ruanyouzhi.estate.estate.dto;

import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.exception.CustomizeException;
import com.sun.org.apache.regexp.internal.REUtil;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;
    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(),errorCode.getMessage());
    }

    public static ResultDTO okOf() {
        return errorOf(200,"请求成功");
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }
    public static <T>ResultDTO okOf(T t){
        ResultDTO resultDTO =new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("评论成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
