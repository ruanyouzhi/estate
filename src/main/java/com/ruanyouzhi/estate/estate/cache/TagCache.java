package com.ruanyouzhi.estate.estate.cache;

import com.ruanyouzhi.estate.estate.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        ArrayList<TagDTO> tagDTOs = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("java","c","c++","php","perl","python","javascript","c#","ruby","go","lua","node.js","erlang","scala","bash","actionscript","golang","typescript" ,"flutter"
        ));
        TagDTO tools = new TagDTO();
        tools.setCategoryName("工具");
        tools.setTags(Arrays.asList("vim","emacs","ide","eclipse","xcode","intellij-idea","textmate","sublime-text","visual-studio","git","github","svn","hg","docker","ci"
        ));
        TagDTO server = new TagDTO();
        server .setCategoryName("数据库");
        server.setTags(Arrays.asList("mysql","sqlite","oracle","sql","nosql","redis","mongodb","memcached","postgresql"
        ));

        tagDTOs.add(program);
        tagDTOs.add(tools);
        tagDTOs.add(server);
        return tagDTOs;
    }
    public static String filterInvalid(String tags){
     String[] split= StringUtils.split(tags,",");
     List<TagDTO> tagDTOS=get();
     List<String> tagList=tagDTOS.stream().flatMap(tag->tag.getTags().stream()).collect(Collectors.toList());
     String invalid=Arrays.stream(split).filter(t->!tagList.contains(t)).collect(Collectors.joining(","));
     return invalid;
    }
}
