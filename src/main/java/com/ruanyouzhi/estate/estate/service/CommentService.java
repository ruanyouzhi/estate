package com.ruanyouzhi.estate.estate.service;

import com.ruanyouzhi.estate.estate.Mapper.*;
import com.ruanyouzhi.estate.estate.Model.*;
import com.ruanyouzhi.estate.estate.dto.CommentDTO;
import com.ruanyouzhi.estate.estate.enums.CommentTypeEnum;
import com.ruanyouzhi.estate.estate.enums.NotificationEnum;
import com.ruanyouzhi.estate.estate.enums.NotificationStatusEnum;
import com.ruanyouzhi.estate.estate.exception.CustomizeErrorCode;
import com.ruanyouzhi.estate.estate.exception.CustomizeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class CommentService {
    @Autowired
    private  NotificationMapper notificationMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ThumbUpMapper thumbUpMapper;
    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.IsExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            /*增加评论数*/
            Comment parentComment=new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //创建通知
            createdNotify(comment, dbComment.getCommentator(), question.getTitle(), commentator.getName(), NotificationEnum.REPLY_COMMENT,question.getId());
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createdNotify(comment,question.getCreator(),question.getTitle(),commentator.getName(),NotificationEnum.REPLY_QUESTION, question.getId());

        }
    }

    private void createdNotify(Comment comment, Long receiver, String outerTitle, String notifyName, NotificationEnum notificationEnum, Long outerId) {
        if(receiver.equals(comment.getCommentator())){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationEnum.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifyName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> ListByTargetId(long id, CommentTypeEnum type) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_modified desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return new ArrayList<>();
        }
            Set<Long> commentators=comments.stream().map(comment ->comment.getCommentator()).collect(Collectors.toSet());
            List<Long>userIds=new ArrayList<>();
            userIds.addAll(commentators);
            UserExample userExample=new UserExample();
            userExample.createCriteria()
                    .andIdIn(userIds);
            List<User> users = userMapper.selectByExample(userExample);
            //因为循环赋值效率低，所以做个map来赋值
            Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
