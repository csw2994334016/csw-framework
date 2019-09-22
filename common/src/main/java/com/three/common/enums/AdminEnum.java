package com.three.common.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/03/22.
 * Description:
 */
@Getter
public enum AdminEnum {

    YES(1, "超级管理员"),
    NO(2, "普通用户");

    private int code;
    private String message;

    AdminEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

