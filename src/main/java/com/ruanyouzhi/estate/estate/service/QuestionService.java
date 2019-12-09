package com.ruanyouzhi.estate.estate.service;

import com.ruanyouzhi.estate.estate.Mapper.QuestionExtMapper;
import com.ruanyouzhi.estate.estate.Mapper.QuestionMapper;
import com.ruanyouzhi.estate.estate.Mapper.UserMapper;
import com.ruanyouzhi.estate.estate.Model.Question;
import com.ruanyouzhi.estate.estate.Model.QuestionExample;
import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.QuestionQueryDTO;
import com.ruanyouzhi.estate.estate.dto.paginationDTO;
import com.ruanyouzhi.estate.estate.dto.questionDTO;
import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.exception.CustomizeException;
import oracle.jrockit.jfr.Recording;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.expression.Each;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public paginationDTO list(String search, Integer page, Integer size) {
        if(StringUtils.isNotBlank(search)){
            String []tags=StringUtils.split(search,' ');
            search=Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        Integer totalPage;
        paginationDTO<questionDTO> pagination = new paginationDTO<>();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount=questionExtMapper.countBySearch(questionQueryDTO);
        totalPage=totalCount%size==0?(totalCount/size):(totalCount/size+1);
        if(page<1)page=1;
        if(page>totalPage)page=totalPage;
        pagination.setPagination(page,totalPage);
        Integer offset=size*(page-1);
        List<questionDTO> questionDTOList=new ArrayList<>();//?
        QuestionExample example=new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);
        for (Question question : questionList) {
            questionDTO questionDTO = new questionDTO();
            BeanUtils.copyProperties(question,questionDTO);//用一个类给另一个类赋值
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pagination.setData(questionDTOList);
        return pagination;
    }

    public paginationDTO listByUserId(long userId, Integer page, Integer size) {
        Integer totalPage;
        paginationDTO<questionDTO> pagination = new paginationDTO();
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
        pagination.setData(questionDTOList);
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
        if(question.getId()==-1){
            //创建
            question.setId(null);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else {
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example= new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated=questionMapper.updateByExampleSelective(updateQuestion,example);
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

    public List<questionDTO> selectRelated(questionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String []tags=StringUtils.split(questionDTO.getTag(),"，|,");
        //将string 类型的tags转为arrays类型，再添加分隔符。
        String regexpTag= Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question=new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<com.ruanyouzhi.estate.estate.dto.questionDTO> questionDTOS = questions.stream().map(q -> {
            com.ruanyouzhi.estate.estate.dto.questionDTO questionDTO1 = new questionDTO();
            BeanUtils.copyProperties(q,questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return questionDTOS;
    }

    public long questionNum(Long userId) {
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        long questionNum = questionMapper.countByExample(example);
        return questionNum;
    }

    public List<Question> listTop() {
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("view_count desc Limit 5");
        List<Question> questions = questionMapper.selectByExample(example);
        return questions;
    }
}
