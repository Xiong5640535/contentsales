package com.xiongyi.contentsales.controller;

import com.xiongyi.contentsales.entity.User;
import com.xiongyi.contentsales.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongyi on 2018/12/30.
 */
@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private Environment env;

    @RequestMapping(value ="login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value ="validate", method = RequestMethod.POST)
    public String validate(HttpServletRequest request, String userName, String password) {
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            // 用户名或者密码为空
            return "";
        }
        List<User> users = getUsers();
        for(User user: users) {
            if(userName.equals(user.getUserName()) && password.equals(user.getPassword())) {
                // 登录成功
                HttpSession session = request.getSession();
                session.setAttribute("nickName", user.getNickName());
                session.setAttribute("role",user.getRole());
                return "redirect:/content/index";
            }
        }
        return "";
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        User buyerUser = new User();
        buyerUser.setUserName(env.getProperty("buyer.username"));
        buyerUser.setPassword(env.getProperty("buyer.password"));
        buyerUser.setNickName(env.getProperty("buyer.nickname"));
        buyerUser.setRole(UserRole.BUYER.getValue());
        User sellerUser = new User();
        sellerUser.setUserName(env.getProperty("seller.username"));
        sellerUser.setPassword(env.getProperty("seller.password"));
        sellerUser.setNickName(env.getProperty("seller.nickname"));
        sellerUser.setRole(UserRole.SELLER.getValue());
        users.add(buyerUser);
        users.add(sellerUser);
        return users;
    }
}
