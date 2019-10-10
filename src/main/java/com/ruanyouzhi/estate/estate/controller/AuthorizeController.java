package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.Mapper.UserMapper;
import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.GitHubUser;
import com.ruanyouzhi.estate.estate.dto.accessTokenDto;
import com.ruanyouzhi.estate.estate.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;
    @Autowired
    private UserMapper userMapper;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect_url}")
    private String redirectUrl;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        accessTokenDto a = new accessTokenDto();
        a.setClient_id(clientId);
        a.setClient_secret(clientSecret);
        a.setCode(code);
        a.setRedirect_uri(redirectUrl);
        a.setState(state);
        String accessToken=gitHubProvider.getAccessToken(a);
        GitHubUser hubUser = gitHubProvider.getUser(accessToken);
        System.out.println(hubUser.getId());
        if(hubUser!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountID(String.valueOf(hubUser.getId()));
            user.setName(hubUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(hubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }
}
