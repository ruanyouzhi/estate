package com.ruanyouzhi.estate.estate.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class paginationDTO {
    List<questionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEnd;
    private Integer currentPage;
    private Integer totalPage;
    List<Integer> pages=new ArrayList<>();

    public void setPagination(Integer page, Integer totalPage) {
        this.totalPage=totalPage;
        currentPage =page;
        //计算总共页数
        pages.add(currentPage);
        for(int i=1;i<=3;i++){
            if(currentPage-i>=1){
                pages.add(0,currentPage-i);
            }
            if(currentPage+i<=totalPage){
                pages.add(currentPage+i);
            }
        }
        //是否展示上一页
        showPrevious=currentPage==1?false:true;
        //是否展示下一页
        showNext=currentPage==totalPage?false:true;
        //是否展示第一页
        showFirstPage=pages.contains(1)==true?false:true;
        //是否展示最后一页
        showEnd=pages.contains(totalPage)==true?false:true;
    }
}
