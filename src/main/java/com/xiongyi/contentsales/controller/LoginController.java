package com.xiongyi.contentsales.controller;

import com.xiongyi.contentsales.entity.User;
import com.xiongyi.contentsales.enums.UserRoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiongyi on 2018/12/30.
 */
@Controller
@RequestMapping
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private Environment env;

    /**
     * 跳转到登录界面
     * @return
     */
    @RequestMapping(value ="login", method = RequestMethod.GET)
    public String login() {
        logger.info("[LoginController: login]");
        return "login";
    }

    /**
     * 登出（移除session中的昵称和角色）
     * @param request
     * @return
     */
    @RequestMapping(value ="logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        logger.info("[LoginController: logout]");
        HttpSession session = request.getSession();
        //移除session中的昵称和角色
        session.removeAttribute("nickName");
        session.removeAttribute("role");
        return "login";
    }

    /**
     * 校验用户名密码是否正确
     * @param request
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value ="validate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> validate(HttpServletRequest request, String userName, String password) {
        logger.info("[LoginController: validate](begin) userName={}, password={}", userName, password);
        Map<String, String> res = new HashMap<>();
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            // 用户名或者密码为空
            res.put("message","用户名或密码为空");
            res.put("code","400");
            logger.info("[LoginController: validate]用户名或密码为空 userName={}, password={}", userName, password);
            return res;
        }
        List<User> users = getUsers();
        for(User user: users) {
            if(userName.equals(user.getUserName()) && password.equals(user.getPassword())) {
                // 登录成功
                HttpSession session = request.getSession();
                session.setAttribute("nickName", user.getNickName());
                session.setAttribute("role",user.getRole());
                res.put("message","登录成功");
                res.put("code","200");
                logger.info("[LoginController: validate] 登录成功 userName={}, password={}", userName, password);
                return res;
            }
        }
        res.put("message","用户名或密码错误");
        res.put("code","400");
        logger.info("[LoginController: validate]用户名或密码错误 userName={}, password={}", userName, password);
        return res;
    }

    /**
     * 从配置文件中获取用户名密码和角色
     * @return
     */
    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        User buyerUser = new User();
        buyerUser.setUserName(env.getProperty("buyer.username"));
        buyerUser.setPassword(DigestUtils.md5DigestAsHex(env.getProperty("buyer.password").getBytes()));
        buyerUser.setNickName(env.getProperty("buyer.nickname"));
        buyerUser.setRole(UserRoleEnum.BUYER.getValue());
        User sellerUser = new User();
        sellerUser.setUserName(env.getProperty("seller.username"));
        sellerUser.setPassword(DigestUtils.md5DigestAsHex(env.getProperty("seller.password").getBytes()));
        sellerUser.setNickName(env.getProperty("seller.nickname"));
        sellerUser.setRole(UserRoleEnum.SELLER.getValue());
        users.add(buyerUser);
        users.add(sellerUser);
        return users;
    }
}
