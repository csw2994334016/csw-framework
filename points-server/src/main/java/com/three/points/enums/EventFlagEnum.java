package com.three.points.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum EventFlagEnum {

    STANDARD(1, "标准"),
    TEMPORARY(0, "临时");

    private int code;

    private String message;

    EventFlagEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
