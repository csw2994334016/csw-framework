package com.three.common.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum YesNoEnum {

    YES(1, "是"),
    NO(2, "否");

    private int code;

    private String message;

    YesNoEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
