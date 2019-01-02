package com.xiongyi.contentsales.controller;

import com.xiongyi.contentsales.entity.Account;
import com.xiongyi.contentsales.entity.Content;
import com.xiongyi.contentsales.enums.ContentType;
import com.xiongyi.contentsales.enums.SessionValue;
import com.xiongyi.contentsales.service.AccountService;
import com.xiongyi.contentsales.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * 登录之后进去内容列表首页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value ="index", method = RequestMethod.GET)
    public String index(HttpServletRequest request, int type, Model model) {
        HttpSession session = request.getSession();
        List<Content> contentList = new ArrayList<>();
        if(ContentType.UNPURCHASES.getValue() == type) {
            contentList = contentService.getUnpurchasedContentList();
        } else if(ContentType.ALL.getValue() == type) {
            contentList = contentService.getContentList();
        } else {
            // 类型错误
            return "";
        }
        model.addAttribute("contentList", contentList);
        model.addAttribute("nickName", session.getAttribute(SessionValue.NICKNAME.getValue()));
        model.addAttribute("role",session.getAttribute(SessionValue.ROLE.getValue()));
        return "index";
    }

    /**
     * 点击内容图片之后进行内容详情页
     * @param request
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value ="details", method = RequestMethod.GET)
    public String details(HttpServletRequest request, int id, Model model) {
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
        HttpSession session = request.getSession();
        model.addAttribute("nickName", session.getAttribute(SessionValue.NICKNAME.getValue()));
        model.addAttribute("role",session.getAttribute(SessionValue.ROLE.getValue()));
        return "details";
    }

    /**
     * 点击购买按钮，将内容商品加入购物车保存在session中
     * @param request
     * @param content
     * @return
     */
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
        Map<Integer,Integer> cart = (Map<Integer, Integer>) session.getAttribute(SessionValue.CART.getValue());
        if(ObjectUtils.isEmpty(cart)) {
            cart = new HashMap<>();
        }
        cart.put(id,amount);
        session.setAttribute("cart",cart);
        return "redirect:cart";
    }

    /**
     * 将session中的购物车取出，查出详情进入购物车界面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value ="cart", method = RequestMethod.GET)
    public String cart(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Map<Integer,Integer> cart = (Map)session.getAttribute(SessionValue.CART.getValue());
        List<Content> contentList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cart)) {
            for(Map.Entry<Integer,Integer> map: cart.entrySet()) {
                int id = map.getKey();
                int amount = map.getValue();
                Content content = contentService.getContentById(id);
                content.setAmount(amount);
                contentList.add(content);
            }
        }
        model.addAttribute("contentList",contentList);
        model.addAttribute("nickName", session.getAttribute(SessionValue.NICKNAME.getValue()));
        model.addAttribute("role",session.getAttribute(SessionValue.ROLE.getValue()));
        return "cart";
    }

    /**
     * 在购物车界面中点击结算按钮，将商品保存在订单表中
     * @param request
     * @return
     */
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
            int id = Integer.parseInt(idsStr[i]);
            int amount = Integer.parseInt(amountsStr[i]);
            account.setContentId(id);
            Content content = contentService.getContentById(Integer.parseInt(idsStr[i]));
            account.setTitle(content.getTitle());
            account.setImage(content.getImage());
            account.setPrice(Long.parseLong(pricesStr[i]));
            account.setAmount(amount);
            account.setTime(new Date());
            // 更新销售数量
            contentService.updateContentAmountById(id, amount);
            // 插入订单表
            int insert = accountService.insert(account);
            if(insert <= 0) {
                // 插入数据库错误
                return "";
            }
        }
        HttpSession session = request.getSession();
        // 购买成功后清空购物车
        session.removeAttribute(SessionValue.CART.getValue());
        return "redirect:account";
    }

    /**
     * 从订单表中查出所有已购买的商品，进行列表展示
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value ="account", method = RequestMethod.GET)
    public String getContentList(HttpServletRequest request, Model model) {
        List<Account> accountList = accountService.getAccounts();
        long total = 0;
        for(Account account : accountList) {
            total += account.getPrice()* account.getAmount();
        }
        model.addAttribute("accountList", accountList);
        model.addAttribute("total", total/100.0);
        HttpSession session = request.getSession();
        model.addAttribute("nickName", session.getAttribute(SessionValue.NICKNAME.getValue()));
        model.addAttribute("role",session.getAttribute(SessionValue.ROLE.getValue()));
        return "account";
    }

    @RequestMapping(value ="delete", method = RequestMethod.GET)
    public String deleteContentById(HttpServletRequest request, int id, RedirectAttributes redirectAttributes) {
        int res = contentService.deleteContentById(id);
        if(res <= 0) {
            // 数据库删除错误
            return "";
        }
        redirectAttributes.addAttribute("type", ContentType.ALL.getValue());
        return "redirect:index";
    }
}
