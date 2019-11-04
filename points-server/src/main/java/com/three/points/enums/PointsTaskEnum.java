package com.three.points.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum PointsTaskEnum {

    UNFINISHED(1, "未完成"),
    FINISHED(2, "已完成");

    private int code;

    private String message;

    PointsTaskEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {
        for (PointsTaskEnum themeEnum : PointsTaskEnum.values()) {
            if (themeEnum.getCode() == code) {
                return themeEnum.getMessage();
            }
        }
        return null;
    }
}
