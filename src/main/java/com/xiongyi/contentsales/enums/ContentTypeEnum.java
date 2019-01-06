package com.xiongyi.contentsales.enums;

/**
 * @Auther: xiongyi
 * @Date: 2019/1/2 11:06
 */
public enum ContentTypeEnum {
    ALL(0, "所有商品"),
    UNPURCHASES(1, "未购买的商品");
    private int value;
    private String text;
    ContentTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
