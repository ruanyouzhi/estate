package com.ruanyouzhi.estate.estate.controller;
import com.ruanyouzhi.estate.estate.Mapper.CommentMapper;
import com.ruanyouzhi.estate.estate.Mapper.ThumbUpMapper;
import com.ruanyouzhi.estate.estate.Model.*;
import com.ruanyouzhi.estate.estate.dto.CommentCreateDTO;
import com.ruanyouzhi.estate.estate.dto.CommentDTO;
import com.ruanyouzhi.estate.estate.dto.LikeCommentDTO;
import com.ruanyouzhi.estate.estate.dto.ResultDTO;
import com.ruanyouzhi.estate.estate.enums.CommentTypeEnum;
import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ThumbUpMapper thumbUpMapper;
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
     public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                                         HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDTO==null|| StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment=new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setCommentator(user.getId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment,user);
        return ResultDTO.okOf();
     }
     @ResponseBody
     @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
     public ResultDTO<List> comments(@PathVariable(name = "id") long id){
         List<CommentDTO> commentDTOs = commentService.ListByTargetId(id, CommentTypeEnum.COMMENT);
         return ResultDTO.okOf(commentDTOs);
     }
    @ResponseBody
    @RequestMapping(value = "/likeComment",method = RequestMethod.POST)
    public Object likeComment(@RequestBody LikeCommentDTO likeCommentDTO,
                              HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (likeCommentDTO.getLikeCount()==null)likeCommentDTO.setLikeCount((long) 0);
        Comment comment=new Comment();
        comment.setId(likeCommentDTO.getId());
        ThumbUpExample thumbExample = new ThumbUpExample();
        thumbExample.createCriteria().andFansEqualTo(likeCommentDTO.getUserId()).
                andQuestionIdEqualTo(likeCommentDTO.getQuestionId());
        List<ThumbUp> thumbUps = thumbUpMapper.selectByExample(thumbExample);
        if(thumbUps.size()==0){
            ThumbUp thumbUp = new ThumbUp();
            thumbUp.setFans(likeCommentDTO.getUserId());
            thumbUp.setQuestionId(likeCommentDTO.getQuestionId());
            thumbUp.setCommentId(likeCommentDTO.getId());
            thumbUp.setCommentator(likeCommentDTO.getCommentator());
            thumbUpMapper.insert(thumbUp);
            comment.setLikeCount(likeCommentDTO.getLikeCount()+1);
            commentMapper.updateByPrimaryKeySelective(comment);
        }else {
            thumbUpMapper.deleteByPrimaryKey(thumbUps.get(0).getFans());
            comment.setLikeCount(likeCommentDTO.getLikeCount()-1);
            commentMapper.updateByPrimaryKeySelective(comment);
        }

        return ResultDTO.okOf();
    }
}
