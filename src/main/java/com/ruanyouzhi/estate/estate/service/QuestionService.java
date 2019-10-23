package com.ruanyouzhi.estate.estate.service;

import com.ruanyouzhi.estate.estate.Mapper.QuestionExtMapper;
import com.ruanyouzhi.estate.estate.Mapper.QuestionMapper;
import com.ruanyouzhi.estate.estate.Mapper.UserMapper;
import com.ruanyouzhi.estate.estate.Model.Question;
import com.ruanyouzhi.estate.estate.Model.QuestionExample;
import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.paginationDTO;
import com.ruanyouzhi.estate.estate.dto.questionDTO;
import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.exception.CustomizeException;
import oracle.jrockit.jfr.Recording;
import org.apache.ibatis.session.RowBounds;
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
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public paginationDTO list(Integer page, Integer size) {
        Integer totalPage;
        paginationDTO pagination = new paginationDTO();

        Integer totalCount=(int)questionMapper.countByExample(new QuestionExample());
        totalPage=totalCount%size==0?(totalCount/size):(totalCount/size+1);
        if(page<1)page=1;
        if(page>totalPage)page=totalPage;
        pagination.setPagination(page,totalPage);
        Integer offset=size*(page-1);
        List<questionDTO> questionDTOList=new ArrayList<>();//?
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        for (Question question : questionList) {
            questionDTO questionDTO = new questionDTO();
            BeanUtils.copyProperties(question,questionDTO);//用一个类给另一个类赋值
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pagination.setQuestions(questionDTOList);
        return pagination;
    }

    public paginationDTO listByUserId(long userId, Integer page, Integer size) {
        Integer totalPage;
        paginationDTO pagination = new paginationDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount= (int)questionMapper.countByExample(example);
        totalPage=totalCount%size==0?(totalCount/size):(totalCount/size+1);
        if(page<1)page=1;
        if(page>totalPage)page=totalPage;
        pagination.setPagination(page,totalPage);

        Integer offset=size*(page-1);
        List<questionDTO> questionDTOList=new ArrayList<>();//?
        QuestionExample example1=new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        for (Question question : questionList) {
            questionDTO questionDTO = new questionDTO();
            BeanUtils.copyProperties(question,questionDTO);//用一个类给另一个类赋值
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pagination.setQuestions(questionDTOList);
        return pagination;
    }

    public questionDTO getById(long id) {
        questionDTO questionDTO=new questionDTO();
        Question question=questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
    public void createOrUpdate(Question question){
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example= new QuestionExample();
            example.createCriteria().andCreatorEqualTo(question.getId());
            int updated=questionMapper.updateByExampleSelective(updateQuestion,new QuestionExample());
            if(updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(long id) {
        Question question=new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }
}
