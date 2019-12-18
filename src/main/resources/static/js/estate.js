//提交回复
function post() {
    var questionId = $("#question_id").val();
    var commentContent = $("#comment_content").val();
    comment2target(questionId,1,commentContent);
}
function comment(e) {
    var commentId=e.getAttribute('data-id');
    console.log(commentId);
    var content = $("#input-"+commentId).val();
    console.log(content)
    comment2target(commentId,2,content);
}
function comment2target(targetId, type, content) {

    if(!content){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var conf = confirm(response.message);
                    if (conf) {
                        window.open("https://github.com/login/oauth/authorize?client_id=10c78b0a763c6a093d41&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    } else {
                        alert(response.message);
                    }
                }else{
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });

}
/*
* 展开二级评论
*/
function collapseComment(e) {
    var id=e.getAttribute("data-id");
    //获得二级评论状态
    var collapse = e.getAttribute("data-collapse");
    var comments=$("#comment-"+id);
    if(collapse){
    //    关闭二级评论
        comments.removeClass('in');
        e.removeAttribute('data-collapse');
        e.classList.remove('active');
    }else{
        var subCommentContainer=$('#comment-'+id);
        if(subCommentContainer.children().length!=1){
            //展开二级评论
            comments.addClass('in');
            e.setAttribute('data-collapse','in');
            e.classList.add('active');
        }else{
            $.getJSON( "/comment/"+id, function( data ) {
                $.each( data.data.reverse(), function(index,comment) {
                    var mediaLeftElement=$("<div/>",{
                        "class":"media-left"
                    }).append($('<img/>',{
                        'class':"media-object img-rounded",
                        'src':comment.user.avatarUrl
                    }));
                    var mediaBodyElement=$('<div/>',{
                        'class':"media-heading"
                    }).append($('<h5/>',{
                        'class':"media-heading",
                        "html":comment.user.name
                    })).append($('<div/>',{
                        "html":comment.content
                    })).append($('<div/>',{
                        "class":"menu"
                    })).append($('<span/>',{
                        'class':"pull-right",
                        'html':moment(comment.gmtCreate).format('YYYY-MM-DD')
                    }));
                    var mediaElement=$("<div/>",{
                        'class':"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement=$("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);//加到父元素内的前面
                });
            });
            //展开二级评论
            comments.addClass('in');
            e.setAttribute('data-collapse','in');
            e.classList.add('active');
        }


    }

}
function selectTag(e) {
    var value=e.getAttribute('data-tag');
    var previous=$("#tag").val();
    if(previous.indexOf(value)!=-1){}
    else{
        if(previous){
            $("#tag").val(previous+','+value);
        }else{
            $("#tag").val(value)
        }
    }

}
function showSelectTag() {
    $("#select-tag").show();
}
function likeComment(e) {
    var id=e.getAttribute('data-id');
    var likeCount=e.getAttribute('data-LikeCount');
    var userId=e.getAttribute('data-user');
    var questionId=e.getAttribute('data-questionId');
    var commentator=e.getAttribute('data-commentator');
    console.log(likeCount);
    $.ajax({
        type: "POST",
        url: "/likeComment",
        contentType: "application/json",
        data: JSON.stringify({
            "id": id,
            "likeCount": likeCount,
             "userId":userId,
             "questionId":questionId,
            "commentator":commentator
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var conf = confirm(response.message);
                    if (conf) {
                        window.open("https://github.com/login/oauth/authorize?client_id=10c78b0a763c6a093d41&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    } else {
                        alert(response.message);
                    }
                }else{
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });

}
function questionDelete(e,status) {
    if(status==1){
        var id=e.getAttribute("data-questionId");
        $.ajax({
            type: "POST",
            url: "/deleteQuestion",
            contentType: "application/json",
            data: JSON.stringify({
                "id": id,
            }),
            success: function (response) {
                if (response.code == 200) {
                    window.location.reload();
                } else {
                    if (response.code == 2003) {
                        var conf = confirm(response.message);
                        if (conf) {
                            window.open("https://github.com/login/oauth/authorize?client_id=10c78b0a763c6a093d41&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                            window.localStorage.setItem("closable", true);
                        } else {
                            alert(response.message);
                        }
                    }else{
                        alert(response.message);
                    }
                }
            },
            dataType: "json"
        });

    }
}