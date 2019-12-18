package com.ruanyouzhi.estate.estate.controller;
import com.ruanyouzhi.estate.estate.Mapper.QuestionMapper;
import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.CommentDTO;
import com.ruanyouzhi.estate.estate.dto.ResultDTO;
import com.ruanyouzhi.estate.estate.dto.questionDTO;
import com.ruanyouzhi.estate.estate.enums.CommentTypeEnum;
import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.service.CommentService;
import com.ruanyouzhi.estate.estate.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private QuestionMapper questionMapper;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") long id,
                           HttpServletRequest request,
                           Model model){
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            user=new User();
            user.setId(0L);
        }
        questionDTO question=questionService.getById(id);
        List<questionDTO> relatedQuestions= questionService.selectRelated(question);
        List<CommentDTO>commentDTOList=commentService.ListByTargetId(question.getId(), CommentTypeEnum.QUESTION);
        questionService.incView(id);
        model.addAttribute("question",question);
        model.addAttribute("comments",commentDTOList);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }

    //删除问题
    @GetMapping("/deleteQuestion/{id}")
    public Object post(@PathVariable(name="id") long id,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        questionMapper.deleteByPrimaryKey(id);
        return "redirect:/profile/questions";
    }
}
