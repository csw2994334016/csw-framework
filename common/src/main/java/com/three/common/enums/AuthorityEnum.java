package com.three.common.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/03/22.
 * Description:
 */
@Getter
public enum AuthorityEnum {

    MENU(1, "菜单"),
    BUTTON(2, "按钮");

    private int code;
    private String message;

    AuthorityEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

