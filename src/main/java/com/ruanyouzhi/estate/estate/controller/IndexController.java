package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.dto.paginationDTO;
import com.ruanyouzhi.estate.estate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(@RequestParam(name = "page", defaultValue = "1")Integer page,
                        @RequestParam(name = "size", defaultValue = "5" ) Integer size,
                        Model model){
        paginationDTO pagination=questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
