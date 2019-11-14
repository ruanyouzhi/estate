function post() {
    var questionId = $("#question_id").val();
    var commentContent = $("#comment_content").val();
    if(!commentContent){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": questionId,
            "content": commentContent,
            "type": 1
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