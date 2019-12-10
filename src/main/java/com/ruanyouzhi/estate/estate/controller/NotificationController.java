package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.NotificationDTO;
import com.ruanyouzhi.estate.estate.enums.NotificationEnum;
import com.ruanyouzhi.estate.estate.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name="id") Long id,
                          HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO=notificationService.read(id,user);
        if(notificationDTO.getType()== NotificationEnum.REPLY_QUESTION.getType()||
        notificationDTO.getType()==NotificationEnum.REPLY_COMMENT.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {
            return "redirect:/";
        }
    }
}
