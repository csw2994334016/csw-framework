package com.three.common.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/03/22.
 * Description:
 */
@Getter
public enum LogEnum {

    INFO(1, "INFO"),
    ERROR(2, "ERROR"),

    BUSINESS(1, "业务"),
    LOGIN(2, "登录"),
    SYSTEM(3, "系统");

    private int code;

    private String message;

    LogEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
