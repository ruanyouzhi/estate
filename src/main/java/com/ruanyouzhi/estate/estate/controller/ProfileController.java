package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.paginationDTO;
import com.ruanyouzhi.estate.estate.service.NotificationService;
import com.ruanyouzhi.estate.estate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size,
                          Model model){
        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        if("questions".equals(action)){
            paginationDTO pagination = questionService.listByUserId(user.getId(),page, size);
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName","我的提问");
            model.addAttribute("pagination",pagination);
        } else if("replies".equals(action)){
            paginationDTO pagination=notificationService.list(user.getId(),page,size);
            model.addAttribute("pagination",pagination);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName","最新回复");
        }

        return "profile";
    }
}

