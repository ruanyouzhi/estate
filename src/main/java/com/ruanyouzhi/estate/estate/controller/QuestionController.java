package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.dto.CommentDTO;
import com.ruanyouzhi.estate.estate.dto.questionDTO;
import com.ruanyouzhi.estate.estate.enums.CommentTypeEnum;
import com.ruanyouzhi.estate.estate.service.CommentService;
import com.ruanyouzhi.estate.estate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") long id,
                           Model model){

        questionDTO question=questionService.getById(id);
        List<CommentDTO>commentDTOList=commentService.ListByTargetId(question.getId(), CommentTypeEnum.QUESTION);
        questionService.incView(id);
        model.addAttribute("question",question);
        model.addAttribute("comments",commentDTOList);
        return "question";
    }
}
