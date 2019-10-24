package com.three.common.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum  ThemeEnum {

    DRAFT(0, "草稿"),
    SAVE(1, "保存"),
    SUBMIT(2, "已提交"),
    ATTN(3, "初审人审核"),
    AUDIT(4, "终审人审核"),
    FAILED(5, "审核不通过"),
    SUCCESS(6, "审核通过");

    private int code;

    private String message;

    ThemeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
