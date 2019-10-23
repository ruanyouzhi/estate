package com.ruanyouzhi.estate.estate.dto;

import com.ruanyouzhi.estate.estate.Model.User;
import lombok.Data;

@Data
public class questionDTO {
    private long id;
    private long creator;
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
