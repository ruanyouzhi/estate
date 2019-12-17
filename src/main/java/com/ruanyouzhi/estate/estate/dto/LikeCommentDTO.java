package com.ruanyouzhi.estate.estate.dto;


import lombok.Data;
@Data
public class LikeCommentDTO {
    private Long id;
    private Long likeCount;
    private Long userId;
    private Long questionId;
    private Long commentator;
}
