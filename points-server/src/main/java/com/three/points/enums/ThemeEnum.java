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
    SUBMIT(2, "待初审"),
    ATTN(3, "待终审"),
    AUDIT(4, "审核不通过"),
    FAILED(5, "审核通过");

    private int code;

    private String message;

    ThemeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
