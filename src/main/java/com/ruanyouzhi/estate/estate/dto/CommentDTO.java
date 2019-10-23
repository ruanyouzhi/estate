package com.ruanyouzhi.estate.estate.dto;

import com.ruanyouzhi.estate.estate.Model.Comment;
import lombok.Data;

@Data
public class CommentDTO {
    private long id;
    private long parentId;
    private int type;
    private String content;
}
