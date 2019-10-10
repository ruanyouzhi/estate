package com.ruanyouzhi.estate.estate.Model;

import lombok.Data;

@Data
public class Question {
    private Integer id;
    private Integer creator;
    private String title;
    private String description;
    private String tag;
    private long gmtCreate;
    private long gmtModified;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
}