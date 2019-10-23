package com.ruanyouzhi.estate.estate.advice;

import com.alibaba.fastjson.JSON;
import com.ruanyouzhi.estate.estate.dto.ResultDTO;
import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//默认的通用的异常处理
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handleControllerException(HttpServletRequest request, Throwable e, Model model,
                                     HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO = null;
            if (e instanceof CustomizeException) {   //instanceof 判断某个类的对象是不是其他类的实例
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                resultDTO = resultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;

        } else {
            if (e instanceof CustomizeException) {   //instanceof 判断某个类的对象是不是其他类的实例
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }

            return new ModelAndView("error");
        }

    }
}
