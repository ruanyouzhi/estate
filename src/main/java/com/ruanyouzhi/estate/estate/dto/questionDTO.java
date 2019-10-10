package com.ruanyouzhi.estate.estate.dto;

import com.ruanyouzhi.estate.estate.Model.User;
import lombok.Data;

@Data
public class questionDTO {
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
    private User user;
}
