package com.xiongyi.contentsales.entity;

/**
 * Created by xiongyi on 2018/12/30.
 */
public class Content {
    /**
     * 主键
     */
    private int id;
    /**
     * 标题
     */
    private String title;
    /**
     * 摘要
     */

    private String summary;
    /**
     * 图片url地址
     */
    private String image;
    /**
     * 价格
     */
    private long price;
    /**
     * 全文内容
     */
    private String text;
    /**
     * 销售数量
     */
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", text='" + text + '\'' +
                ", amount=" + num +
                '}';
    }
}
