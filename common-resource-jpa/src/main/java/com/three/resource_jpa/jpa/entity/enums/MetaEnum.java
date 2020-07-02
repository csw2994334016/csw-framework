package com.three.resource_jpa.jpa.entity.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum MetaEnum {

    POJO(0, "虚拟实体"),
    ENTITY(1, "实体");

    private int code;

    private String message;

    MetaEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
