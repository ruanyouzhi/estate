package com.ruanyouzhi.estate.estate.Model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private  String accountID;
    private String name;
    private String token;
    private long gmtCreate;
    private long gmtModified;
    private String avatarUrl;
}
