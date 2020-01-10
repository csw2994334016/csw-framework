package com.three.points.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum ThemeTypeEnum {

    DAILY_POINTS(1, "日常奖扣"),
    MANAGER_TASK(2, "管理任务"),
    FIXED_POINTS(3, "固定积分"),
    OTHER(4, "其他得分");

    private int code;

    private String message;

    ThemeTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {
        for (ThemeTypeEnum themeStatusEnum : ThemeTypeEnum.values()) {
            if (themeStatusEnum.getCode() == code) {
                return themeStatusEnum.getMessage();
            }
        }
        return null;
    }
}
