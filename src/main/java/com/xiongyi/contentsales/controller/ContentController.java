package com.xiongyi.contentsales.controller;

import com.xiongyi.contentsales.entity.Account;
import com.xiongyi.contentsales.entity.Content;
import com.xiongyi.contentsales.service.ContentService;
import com.xiongyi.contentsales.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by xiongyi on 2018/12/30.
 */
@Controller
@RequestMapping(value = "/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value ="index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, String nickName, String role, Model model) {
        HttpSession session = request.getSession();
        List<Content> contentList = contentService.getContentList();
        model.addAttribute("contentList", contentList);
        model.addAttribute("nickName", session.getAttribute("nickName"));
        model.addAttribute("role",session.getAttribute("role"));
        return "index";
    }

    @RequestMapping(value ="details", method = RequestMethod.GET)
    public String details(int id, Model model) {
        Content content = contentService.getContentById(id);
        if(ObjectUtils.isEmpty(content)) {
            // 错误 没有该商品
            return "";
        }
        if(content.getAmount()!=0) {
            // 已购买
            Account account = accountService.getAccountByContentId(id);
            if(ObjectUtils.isEmpty(account)) {
                // 错误 查不到订单
                return "";
            }
            content.setPrice(account.getPrice());
        }
        model.addAttribute("content", content);
        return "details";
    }

    @RequestMapping(value ="buy", method = RequestMethod.POST)
    public String buy(HttpServletRequest request, Content content) {
        int id = content.getId();
        int amount = content.getAmount();
        if(id <= 0 || amount <= 0) {
            // 购买错误
            return "";
        }
        Content contentById = contentService.getContentById(id);
        if(ObjectUtils.isEmpty(contentById)) {
            // 错误 无效的content
            return "";
        }
        HttpSession session = request.getSession();
        Map<Integer,Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if(ObjectUtils.isEmpty(cart)) {
            cart = new HashMap<>();
        }
        cart.put(id,amount);
        session.setAttribute("cart",cart);
        return "redirect:cart";
    }

    @RequestMapping(value ="cart", method = RequestMethod.GET)
    public String cart(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Map<Integer,Integer> cart = (Map)session.getAttribute("cart");
        List<Content> contentList = new ArrayList<>();
        for(Map.Entry<Integer,Integer> map: cart.entrySet()) {
            int id = map.getKey();
            int amount = map.getValue();
            Content content = contentService.getContentById(id);
            if(content.getAmount() != 0) {
                // 错误 该商品已被购买
                return "";
            }
            content.setAmount(amount);
            contentList.add(content);
        }
        model.addAttribute("contentList",contentList);
        return "cart";
    }

    @RequestMapping(value ="settlement", method = RequestMethod.POST)
    public String settlement(HttpServletRequest request) {
        String[] idsStr = request.getParameterValues("ids");
        String[] pricesStr = request.getParameterValues("prices");
        String[] amountsStr = request.getParameterValues("amounts");
        if(StringUtils.isEmpty(idsStr) || StringUtils.isEmpty(pricesStr) || StringUtils.isEmpty(amountsStr)) {
            // 参数错误
            return "";
        }
        for(int i=0;i<idsStr.length;i++) {
            Account account = new Account();
            account.setContentId(Integer.parseInt(idsStr[i]));
            Content content = contentService.getContentById(Integer.parseInt(idsStr[i]));
            account.setTitle(content.getTitle());
            account.setImage(content.getImage());
            account.setPrice(Long.parseLong(pricesStr[i]));
            account.setAmount(Integer.parseInt(amountsStr[i]));
            account.setTime(new Date());
            int insert = accountService.insert(account);
            if(insert <= 0) {
                // 插入数据库错误
                return "";
            }
        }
        return "redirect:account";
    }

    @RequestMapping(value ="account", method = RequestMethod.GET)
    public String getContentList(Model model) {
        List<Account> accountList = accountService.getAccounts();
        if(CollectionUtils.isEmpty(accountList)) {
            // 当前没有账单
            return "";
        }
        long total = 0;
        for(Account account : accountList) {
            total += account.getPrice()* account.getAmount();
        }
        model.addAttribute("accountList", accountList);
        model.addAttribute("total", total/100.0);
        return "account";
    }

//    /**
//     * 显示创建用户表单
//     *
//     */
//    @RequestMapping(value = "/create", method = RequestMethod.GET)
//    public String createUserForm(ModelMap map) {
//        map.addAttribute("user", new User());
//        map.addAttribute("action", "create");
//        return "userForm";
//    }
//
//    /**
//     *  创建用户
//     *    处理 "/users" 的 POST 请求，用来获取用户列表
//     *    通过 @ModelAttribute 绑定参数，也通过 @RequestParam 从页面中传递参数
//     */
//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    public String postUser(@ModelAttribute User user) {
//        userService.insertByUser(user);
//        return "redirect:/users/";
//    }
//
//    /**
//     * 显示需要更新用户表单
//     *    处理 "/users/{id}" 的 GET 请求，通过 URL 中的 id 值获取 User 信息
//     *    URL 中的 id ，通过 @PathVariable 绑定参数
//     */
//    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
//    public String getUser(@PathVariable Long id, ModelMap map) {
//        map.addAttribute("user", userService.findById(id));
//        map.addAttribute("action", "update");
//        return "userForm";
//    }
//
//    /**
//     * 处理 "/users/{id}" 的 PUT 请求，用来更新 User 信息
//     *
//     */
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public String putUser(@ModelAttribute User user) {
//        userService.update(user);
//        return "redirect:/users/";
//    }
//
//    /**
//     * 处理 "/users/{id}" 的 GET 请求，用来删除 User 信息
//     */
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
//    public String deleteUser(@PathVariable Long id) {
//
//        userService.delete(id);
//        return "redirect:/users/";
//    }
}
