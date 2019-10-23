package com.ruanyouzhi.estate.estate.service;

import com.ruanyouzhi.estate.estate.Mapper.CommentMapper;
import com.ruanyouzhi.estate.estate.Mapper.QuestionExtMapper;
import com.ruanyouzhi.estate.estate.Mapper.QuestionMapper;
import com.ruanyouzhi.estate.estate.Model.Comment;
import com.ruanyouzhi.estate.estate.Model.Question;
import com.ruanyouzhi.estate.estate.enums.CommentTypeEnum;
import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    public void insert(Comment comment) {
     if(comment.getParentId()==null||comment.getParentId()==0){
         throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
     }
     if(comment.getType()==null||!CommentTypeEnum.IsExist(comment.getType())){
         throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
     }
     if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
         //回复评论
         Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
         if(dbComment==null){
             throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
         }
         commentMapper.insert(comment);
     }else {
         //回复问题
         Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
         if(question==null){
             throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
         }
         commentMapper.insert(comment);
         question.setCommentCount(1);
         questionExtMapper.incCommentCount(question);

     }
    }
}
