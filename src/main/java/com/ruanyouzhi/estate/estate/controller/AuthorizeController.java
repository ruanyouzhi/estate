package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.dto.GitHubUser;
import com.ruanyouzhi.estate.estate.dto.accessTokenDto;
import com.ruanyouzhi.estate.estate.provider.GitHubProvider;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String redirectUrl;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        accessTokenDto a = new accessTokenDto();
        a.setClient_id("10c78b0a763c6a093d41");
        a.setClient_secret("0c947d6cd42fc1217fe96fe689c0f336c19c99c1");
        a.setCode(code);
        a.setRedirect_uri("http://localhost:8080/callback");
        a.setState(state);
        String accessToken=gitHubProvider.getAccessToken(a);
        GitHubUser user = gitHubProvider.getUser(accessToken);
        System.out.println(user.getId());
        return "index";
    }
}
