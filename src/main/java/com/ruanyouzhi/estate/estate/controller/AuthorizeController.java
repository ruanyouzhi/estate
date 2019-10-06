package com.ruanyouzhi.estate.estate.controller;

import com.ruanyouzhi.estate.estate.Mapper.UserMapper;
import com.ruanyouzhi.estate.estate.Model.User;
import com.ruanyouzhi.estate.estate.dto.GitHubUser;
import com.ruanyouzhi.estate.estate.dto.accessTokenDto;
import com.ruanyouzhi.estate.estate.provider.GitHubProvider;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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
    @Value("${github.redirect_uri}")
    private String redirectUrl;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        System.out.println(state);
        accessTokenDto a = new accessTokenDto();
        a.setClient_id(clientId);
        a.setClient_secret(clientSecret);
        a.setCode(code);
        a.setRedirect_uri(redirectUrl);
        a.setState(state);
        String accessToken=gitHubProvider.getAccessToken(a);
        System.out.println(accessToken);
        GitHubUser hubUser = gitHubProvider.getUser(accessToken);
        System.out.println(hubUser.getId());
        if(hubUser!=null){
            /*
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setAccountID(String.valueOf(hubUser.getId()));
            user.setName(hubUser.getName());
            user.setGmtCreat(System.currentTimeMillis());
            user.getGmtModified(user.getGmtCreat());

            userMapper.insert(user);

             */
            request.getSession().setAttribute("hubUser",hubUser);
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }
}
