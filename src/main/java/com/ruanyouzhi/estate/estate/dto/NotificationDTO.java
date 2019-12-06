package com.ruanyouzhi.estate.estate.dto;

import com.ruanyouzhi.estate.estate.Model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private long id;
    private  long gmtCreate;
    private Integer status;
    private long notifier;
    private String notifierName;
    private String outerTitle;
    private long outerid;
    private String typeName;
    private Integer type;
}
