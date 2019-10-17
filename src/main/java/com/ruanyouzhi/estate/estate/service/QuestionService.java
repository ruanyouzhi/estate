package com.ruanyouzhi.estate.estate.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.ruanyouzhi.estate.estate.Mapper.QuestionMapper;
import com.ruanyouzhi.estate.estate.Mapper.UserMapper;
import com.ruanyouzhi.estate.estate.Model.Question;
import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.paginationDTO;
import com.ruanyouzhi.estate.estate.dto.questionDTO;
import com.sun.org.apache.regexp.internal.REUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public paginationDTO list(Integer page, Integer size) {
        Integer totalPage;
        paginationDTO pagination = new paginationDTO();
        Integer totalCount=questionMapper.count();
        totalPage=totalCount%size==0?(totalCount/size):(totalCount/size+1);
        if(page<1)page=1;
        if(page>totalPage)page=totalPage;
        pagination.setPagination(page,totalPage);
        Integer offset=size*(page-1);
        List<questionDTO> questionDTOList=new ArrayList<>();//?
        List<Question> questionList=questionMapper.list(offset,size);
        for (Question question : questionList) {
            questionDTO questionDTO = new questionDTO();
            BeanUtils.copyProperties(question,questionDTO);//用一个类给另一个类赋值
            User user=userMapper.findById(question.getCreator());
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pagination.setQuestions(questionDTOList);
        return pagination;
    }

    public paginationDTO listByUserId(Integer userId, Integer page, Integer size) {
        Integer totalPage;
        paginationDTO pagination = new paginationDTO();
        Integer totalCount=questionMapper.countByUserId(userId);
        totalPage=totalCount%size==0?(totalCount/size):(totalCount/size+1);
        if(page<1)page=1;
        if(page>totalPage)page=totalPage;
        pagination.setPagination(page,totalPage);

        Integer offset=size*(page-1);
        List<questionDTO> questionDTOList=new ArrayList<>();//?
        List<Question> questionList=questionMapper.listByUserId(userId,offset,size);
        for (Question question : questionList) {
            questionDTO questionDTO = new questionDTO();
            BeanUtils.copyProperties(question,questionDTO);//用一个类给另一个类赋值
            User user=userMapper.findById(question.getCreator());
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pagination.setQuestions(questionDTOList);
        return pagination;
    }

    public questionDTO getById(Integer id) {
        questionDTO questionDTO=new questionDTO();
        Question question=questionMapper.getById(id);
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
