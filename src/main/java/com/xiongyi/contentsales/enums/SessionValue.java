package com.xiongyi.contentsales.enums;

/**
 * Created by xiongyi on 2019/1/2.
 */
public enum SessionValue {
    CART("cart", "购物车"),
    NICKNAME("nickName", "昵称"),
    ROLE("role", "角色");

    private String value;
    private String text;

    SessionValue(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
