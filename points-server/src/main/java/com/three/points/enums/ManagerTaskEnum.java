package com.three.points.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum ManagerTaskEnum {

    TASK_DAY(1, "日"),
    TASK_WEEK(2, "周"),
    TASK_MONTH(3, "月");

    private int code;

    private String message;

    ManagerTaskEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {
        for (ManagerTaskEnum themeEnum : ManagerTaskEnum.values()) {
            if (themeEnum.getCode() == code) {
                return themeEnum.getMessage();
            }
        }
        return null;
    }
}
