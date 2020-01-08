package com.three.points.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum ThemeStatusEnum {

    DRAFT(0, "草稿"),
    SAVE(1, "拟稿"),
    ATTN(2, "待初审"),
    AUDIT(3, "待终审"),
    REJECT(4, "驳回"),
    SUCCESS(5, "审核通过"),
    LOCK(6, "锁定"),
    INVALID(7, "已作废");

    private int code;

    private String message;

    ThemeStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {
        for (ThemeStatusEnum themeStatusEnum : ThemeStatusEnum.values()) {
            if (themeStatusEnum.getCode() == code) {
                return themeStatusEnum.getMessage();
            }
        }
        return null;
    }
}
