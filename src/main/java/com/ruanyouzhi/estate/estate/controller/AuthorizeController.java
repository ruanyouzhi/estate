package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.GitHubUser;
import com.ruanyouzhi.estate.estate.dto.accessTokenDto;
import com.ruanyouzhi.estate.estate.provider.GitHubProvider;
import com.ruanyouzhi.estate.estate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;
    @Autowired
    private UserService userService;
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
        if(hubUser!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(String.valueOf(hubUser.getId()));
            user.setName(hubUser.getName());
            user.setAvatarUrl(hubUser.getAvatar_url());
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                       HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
