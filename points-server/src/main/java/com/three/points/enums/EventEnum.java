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
    RECORDER_POS(3, "终审人通过，系统自动对记录人奖分事件"),
    RECORDER_NEG(4, "终审人驳回，系统自动对记录人扣分事件"),
    ATTN_POS(5, "终审人通过，系统自动对初审人奖分事件"),
    ATTN_NEG(6, "终审人驳回，系统自动对初审人扣分事件"),

    AUDIT_POS_THEME(7, "终审人通过，系统自动生成主题"),
    AUDIT_NEG_THEME(8, "终审人驳回，系统自动生成主题");

    private int code;

    private String message;

    EventEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
