package com.three.common.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/03/22.
 * Description: 数据库字段状态枚举
 */
@Getter
public enum StatusEnum {

    OK(1, "正常"),
    FROZEN(2, "锁定"),
    DELETE(3, "删除");

    private int code;

    private String message;

    StatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

