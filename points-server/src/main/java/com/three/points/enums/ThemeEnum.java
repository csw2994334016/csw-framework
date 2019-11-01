package com.three.points.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum  ThemeEnum {

    DRAFT(0, "草稿"),
    SAVE(1, "保存"),
    ATTN(2, "待初审"),
    AUDIT(3, "待终审"),
    REJECT(4, "驳回"),
    SUCCESS(5, "审核通过"),
    LOCK(6, "锁定");

    private int code;

    private String message;

    ThemeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {
        for (ThemeEnum themeEnum : ThemeEnum.values()) {
            if (themeEnum.getCode() == code) {
                return themeEnum.getMessage();
            }
        }
        return null;
    }
}
