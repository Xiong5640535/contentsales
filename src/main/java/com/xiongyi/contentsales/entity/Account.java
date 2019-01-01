package com.xiongyi.contentsales.entity;

import java.util.Date;

/**
 * Created by xiongyi on 2018/12/30.
 */
public class Account {
    /**
     * 主键Id
     */
    private int id;
    /**
     * 内容Id
     */
    private int contentId;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片url
     */
    private String image;
    /**
     * 购买时的价格(单位：分)
     */
    private long price;
    /**
     * 购买时的数量
     */
    private int amount;
    /**
     * 购买时间（yyyy-MM-dd hh:mm:ss）
     */
    private Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
