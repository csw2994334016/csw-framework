package com.three.points.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum EventEnum {

    AUDIT_POS_TYPE(1, "终审人奖分事件分类"),
    AUDIT_NEG_TYPE(2, "终审人扣分事件分类"),
    RECORDER_POS(3, "记录人奖分事件"),
    RECORDER_NEG(4, "记录人扣分事件"),
    ATTN_POS(5, "初审人奖分事件"),
    ATTN_NEG(6, "初审人扣分事件");

    private int code;

    private String message;

    EventEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {
        for (EventEnum anEnum : EventEnum.values()) {
            if (anEnum.getCode() == code) {
                return anEnum.getMessage();
            }
        }
        return null;
    }
}
