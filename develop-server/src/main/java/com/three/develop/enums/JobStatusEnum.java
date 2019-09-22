package com.three.develop.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/03/22.
 * Description: 数据库字段状态枚举
 */
@Getter
public enum JobStatusEnum {

    RUNNING(1, "运行中"),
    PAUSE(2, "暂停");

    private int code;

    private String message;

    JobStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

