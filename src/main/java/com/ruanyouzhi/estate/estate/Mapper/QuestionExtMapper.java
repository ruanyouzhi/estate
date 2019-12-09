package com.ruanyouzhi.estate.estate.Mapper;

import com.ruanyouzhi.estate.estate.Model.Question;
import com.ruanyouzhi.estate.estate.Model.QuestionExample;
import com.ruanyouzhi.estate.estate.dto.QuestionQueryDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

}