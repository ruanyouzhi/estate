package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.Mapper.QuestionMapper;
import com.ruanyouzhi.estate.estate.Model.Question;
import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.exception.CustomizeException;
import com.ruanyouzhi.estate.estate.service.QuestionService;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.ELState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode.QUESTION_NOT_FOUND;

@Controller
public class publishController {
    @Autowired
    private  QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") long id,
                       Model model){
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        } else{
            model.addAttribute("id",id);
            model.addAttribute("title",question.getTitle());
            model.addAttribute("description",question.getDescription());
            model.addAttribute("tag",question.getTag());
        }

        return "publish";
    }
    @GetMapping("/publish")
    public String publish() {
            return "publish";
        }
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "id",required = false,defaultValue = "-1") long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model){
        //此部分给填充内容赋值，使其能保持
        model.addAttribute("id",id);
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        //此部分判断是否提交正确
        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return publish();
        } if(description==null||description==""){
            model.addAttribute("error","问题描述不能为空");
            return publish();
        } if(tag==null||tag==""){
            model.addAttribute("error","标签不能为空");
            return publish();
        }

        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
