package com.ruanyouzhi.estate.estate.dto;

import com.ruanyouzhi.estate.estate.Model.Comment;
import com.ruanyouzhi.estate.estate.Model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
    private Integer commentCount;
}
