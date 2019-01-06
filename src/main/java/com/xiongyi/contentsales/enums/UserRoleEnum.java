package com.xiongyi.contentsales.enums;

/**
 * Created by xiongyi on 2018/12/30.
 */
public enum UserRoleEnum {
    BUYER("buyer", "买家"),
    SELLER("seller", "卖家");
    private String value;
    private String text;
    UserRoleEnum(String value, String text) {
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
