package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.Model.Question;
import com.ruanyouzhi.estate.estate.dto.paginationDTO;
import com.ruanyouzhi.estate.estate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class daoController {
    @Autowired
    @GetMapping("/dao")
    public String index(){
        return "dao";
    }
}
