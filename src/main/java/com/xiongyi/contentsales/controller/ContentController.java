package com.xiongyi.contentsales.controller;

import com.xiongyi.contentsales.entity.Account;
import com.xiongyi.contentsales.entity.Content;
import com.xiongyi.contentsales.enums.ContentTypeEnum;
import com.xiongyi.contentsales.enums.SessionValueEnum;
import com.xiongyi.contentsales.service.AccountService;
import com.xiongyi.contentsales.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

    private final Logger logger = LoggerFactory.getLogger(ContentController.class);

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
    public String index(HttpServletRequest request, Integer type, Model model) throws Exception{
        logger.info("[ContentController: index](begin) type={}", type);
        List<Content> contentList = new ArrayList<>();
        if(ObjectUtils.isEmpty(type) || ContentTypeEnum.ALL.getValue() == type) {
            // 获取全部内容商品（包括type不传）
            contentList = contentService.getContentList();
            type = ContentTypeEnum.ALL.getValue();
        } else if(ContentTypeEnum.UNPURCHASES.getValue() == type) {
            // 获取未购买的内容商品
            contentList = contentService.getUnpurchasedContentList();
        } else {
            logger.error("[ContentController: index] 非法入参 type={}", type);
            throw new Exception("非法入参");
        }
        HttpSession session = request.getSession();
        String nickName = (String) session.getAttribute(SessionValueEnum.NICKNAME.getValue());
        String role = (String) session.getAttribute(SessionValueEnum.ROLE.getValue());
        model.addAttribute("nickName", nickName);
        model.addAttribute("role",role);
        model.addAttribute("contentList", contentList);
        model.addAttribute("type", type);
        logger.info("[ContentController: index](end) contentList={}, nickName={}, role={}, type={}", contentList, nickName, role, type);
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
    public String details(HttpServletRequest request, int id, Model model) throws Exception {
        logger.info("[ContentController: details](begin) id={}", id);
        Content content = contentService.getContentById(id);
        if(ObjectUtils.isEmpty(content)) {
            logger.error("[ContentController: details] 没有该商品");
            throw new Exception("没有该商品");
        }
        if(content.getNum()!=0) {
            // 已购买
            Account account = accountService.getAccountByContentId(id);
            if(ObjectUtils.isEmpty(account)) {
                logger.error("[ContentController: details] 查不到订单");
                throw new Exception("没有该商品");
            }
            model.addAttribute("buyprice",account.getPrice());
        }
        model.addAttribute("content", content);
        HttpSession session = request.getSession();
        String nickName = (String) session.getAttribute(SessionValueEnum.NICKNAME.getValue());
        String role = (String) session.getAttribute(SessionValueEnum.ROLE.getValue());
        model.addAttribute("nickName", nickName);
        model.addAttribute("role", role);
        logger.info("[ContentController: details](end) content={}, nickName={}, role={}", content, nickName, role);
        return "details";
    }

    /**
     * 进入购物车界面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value ="cart", method = RequestMethod.GET)
    public String cart(HttpServletRequest request, Model model) {
        logger.info("[ContentController: cart](start)");
        HttpSession session = request.getSession();
        String nickName = (String) session.getAttribute(SessionValueEnum.NICKNAME.getValue());
        String role = (String) session.getAttribute(SessionValueEnum.ROLE.getValue());
        model.addAttribute("nickName", nickName);
        model.addAttribute("role", role);
        logger.info("[ContentController: cart](end) nickName={}, role={}", nickName, role);
        return "cart";
    }

    /**
     * 在购物车界面中点击结算按钮，将商品保存在订单表中
     * 由于需要同时操作订单表和商品表所以加上事务
     * @return
     */
    @RequestMapping(value ="settlement", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map<String,String> settlement(@RequestBody List<Content> newProducts) throws Exception {
        logger.info("[ContentController: settlement](begin) newProducts={}", newProducts);
        Map<String,String> res = new HashMap<>();
        if(ObjectUtils.isEmpty(newProducts)) {
            // 错误 当前购物车为空
            res.put("message", "当前购物车为空");
            res.put("code", "200");
            logger.info("[ContentController: settlement] 当前购物车为空");
            return res;
        }
        for(Content product: newProducts) {
            int id = product.getId();
            int num = product.getNum();
            Content content = contentService.getContentById(id);
            Account account = new Account();
            account.setContentId(id);
            account.setTitle(content.getTitle());
            account.setImage(content.getImage());
            account.setPrice(content.getPrice());
            account.setNum(num);
            account.setTime(new Date());
            // 更新销售数量
            int update = contentService.updateContentNumById(id, num);
            if(update <= 0) {
                logger.error("[ContentController: settlement] 更新销售数量失败 id={}, num={}", id, num);
                throw new Exception("更新数据库失败");
            }
            // 插入订单表
            int insert = accountService.insert(account);
            if(insert <= 0) {
                logger.error("[ContentController: settlement] 插入订单表错误 id={}, num={}", account, account);
                throw new Exception("更新数据库失败");
            }
        }
        res.put("message", "购买成功");
        res.put("code", "200");
        logger.info("[ContentController: settlement](end) 购买成功");
        return res;
    }

    /**
     * 从订单表中查出所有已购买的商品，进行列表展示
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value ="account", method = RequestMethod.GET)
    public String getContentList(HttpServletRequest request, Model model) {
        logger.info("[ContentController: getContentList](begin)");
        List<Account> accountList = accountService.getAccounts();
        if(ObjectUtils.isEmpty(accountList)) {
            logger.info("[ContentController: getContentList]当前没有订单");
            return "account";
        }
        long total = 0;
        for(Account account : accountList) {
            // 计算总价
            total += account.getPrice()* account.getNum();
        }
        model.addAttribute("accountList", accountList);
        model.addAttribute("total", total/100.0);
        HttpSession session = request.getSession();
        String nickName = (String) session.getAttribute(SessionValueEnum.NICKNAME.getValue());
        String role = (String) session.getAttribute(SessionValueEnum.ROLE.getValue());
        model.addAttribute("nickName", nickName);
        model.addAttribute("role", role);
        logger.info("[ContentController: getContentList](end) accountList={}, total={}, nickName={}, role={}", accountList, total, nickName, role);
        return "account";
    }

    /**
     * 根据内容商品ID删除商品
     * @param id 商品ID
     * @return
     */
    @RequestMapping(value ="delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> deleteContentById(int id) {
        logger.info("[ContentController: deleteContentById](begin) id={}", id);
        Map<String, String> res = new HashMap<>();
        int delete = contentService.deleteContentById(id);
        if(delete <= 0) {
            // 数据库删除错误
            res.put("message","数据库删除错误");
            res.put("code","400");
            logger.error("[ContentController: deleteContentById](begin) 数据库删除错误");
            return res;
        }
        res.put("message","删除成功");
        res.put("code","200");
        logger.info("[ContentController: deleteContentById](end) 删除成功 id={}", id);
        return res;
    }

    /**
     * 跳转到内容商品发布界面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value ="toPublish", method = RequestMethod.GET)
    public String toPublish(HttpServletRequest request, Model model) {
        logger.info("[ContentController: toPublish](begin)");
        HttpSession session = request.getSession();
        String nickName = (String) session.getAttribute(SessionValueEnum.NICKNAME.getValue());
        String role = (String) session.getAttribute(SessionValueEnum.ROLE.getValue());
        model.addAttribute("nickName", nickName);
        model.addAttribute("role", role);
        logger.info("[ContentController: toPublish](end) nickName={}, role={}", nickName, role);
        return "publish";
    }

    /**
     * 将发布界面提交的表单数据入库
     * @param title 标题
     * @param summary 摘要
     * @param image 图片地址
     * @param detail 正文
     * @param price 价格
     * @return
     */
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    public String publish(String title, String summary, String image, String detail, String price) throws Exception {
        logger.info("[ContentController: publish](begin) title={}, summary={}, image={}, detail={}, price={}", title, summary, image, detail, price);
        if(!isValid(title,summary,detail)) {
            logger.error("[ContentController: publish] 参数不合法 title={}, summary={}, image={}, detail={}, price={}", title, summary, image, detail, price);
            return "redirect:/content/toPublish";
        }
        Content content = new Content();
        content.setTitle(title);
        content.setSummary(summary);
        content.setImage(image);
        content.setText(detail);
        content.setPrice((long)(Double.parseDouble(price)*100));
        // 保存商品到数据库
        int insert = contentService.insert(content);
        if(insert <= 0) {
            logger.error("[ContentController: publish] 保存商品到数据库失败 content", content);
            throw new Exception("保存商品到数据库失败");
        }
        logger.info("[ContentController: publish](end) 保存商品到数据库成功 content", content);
        return "redirect:/content/index";
    }

    /**
     * 跳转到编辑界面
     * @param request
     * @param id 商品Id
     * @param model
     * @return
     */
    @RequestMapping(value ="toEdit", method = RequestMethod.GET)
    public String toEdit(HttpServletRequest request, int id, Model model) throws Exception {
        logger.info("[ContentController: toEdit](start) id={}", id);
        Content content = contentService.getContentById(id);
        if(ObjectUtils.isEmpty(content)) {
            logger.error("[ContentController: toEdit] 该商品不存在 id={}", id);
            throw new Exception("该商品不存在");
        }
        model.addAttribute("content", content);
        HttpSession session = request.getSession();
        String nickName = (String)session.getAttribute(SessionValueEnum.NICKNAME.getValue());
        String role = (String)session.getAttribute(SessionValueEnum.ROLE.getValue());
        model.addAttribute("nickName", nickName);
        model.addAttribute("role", role);
        logger.info("[ContentController: toEdit](end) content={} nickName={}, role={}", content, nickName, role);
        return "edit";
    }

    /**
     * 将编辑界面提交的表单数据更新到数据库
     * @param id 商品Id
     * @param title 标题
     * @param summary 摘要
     * @param image 图片地址
     * @param detail 正文
     * @param price 价格
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value ="edit", method = RequestMethod.POST)
    public String edit(int id, String title, String summary, String image, String detail, String price, RedirectAttributes redirectAttributes) throws Exception {
        logger.info("[ContentController: edit](begin) id={}, title={}, summary={}, image={}, detail={}, price={}", id, title, summary, image, detail, price);
        if(!isValid(title,summary,detail)) {
            logger.error("[ContentController: edit] 参数不合法id={}, title={}, summary={}, image={}, detail={}, price={}", id, title, summary, image, detail, price);
            redirectAttributes.addAttribute("id", id);
            return "redirect:/content/toEdit";
        }
        Content content = new Content();
        content.setId(id);
        content.setTitle(title);
        content.setSummary(summary);
        content.setImage(image);
        content.setText(detail);
        content.setPrice((long)(Double.parseDouble(price)*100));
        int update = contentService.updateContentById(content);
        if(update <= 0) {
            logger.error("[ContentController: edit] 更新数据库失败 content={}", content);
            throw new Exception("更新数据库失败");
        }
        redirectAttributes.addAttribute("id",id);
        logger.info("[ContentController: edit] (end) id={}", id);
        return "redirect:/content/editSubmit";
    }

    /**
     * 编辑成功之后跳转编辑成功界面
     * @param request
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value ="editSubmit", method = RequestMethod.GET)
    public String editSubmit(HttpServletRequest request, int id, Model model) {
        logger.info("[ContentController: editSubmit] (start) id={}", id);
        model.addAttribute("id",id);
        HttpSession session = request.getSession();
        String nickName = (String)session.getAttribute(SessionValueEnum.NICKNAME.getValue());
        String role = (String)session.getAttribute(SessionValueEnum.ROLE.getValue());
        model.addAttribute("nickName", nickName);
        model.addAttribute("role", role);
        logger.info("[ContentController: editSubmit] (end) id={}, nickName={}, role", id, nickName, role);
        return "editSubmit";
    }

    private boolean isValid(String title, String summary, String detail) {
        if(title.length() > 80 || title.length() < 2)
            return false;
        if(summary.length() > 140 || summary.length() < 2)
            return false;
        if(detail.length() > 1000 || detail.length() < 2)
            return false;
        return true;
    }

}
