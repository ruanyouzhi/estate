package com.ruanyouzhi.estate.estate.provider;

import com.alibaba.fastjson.JSON;
import com.ruanyouzhi.estate.estate.dto.GitHubUser;
import com.ruanyouzhi.estate.estate.dto.accessTokenDto;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class GitHubProvider {
    public String getAccessToken(accessTokenDto a){
       MediaType mediaType
                = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(a));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string=response.body().string();
            String string2=string.split("&")[0];
            String string3=string.split("=")[1];
            return string3;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public GitHubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            return null;
        }
    }
}
