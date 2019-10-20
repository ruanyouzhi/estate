package com.ruanyouzhi.estate.estate.advice;

import com.ruanyouzhi.estate.estate.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
//默认的通用的异常处理
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(HttpServletRequest request, Throwable e, Model model) {
        if(e instanceof CustomizeException){   //instanceof 判断某个类的对象是不是其他类的实例
            model.addAttribute("message",e.getMessage());
        }else {
            model.addAttribute("message","出了点问题");
        }

        return new ModelAndView("error");
    }

}
