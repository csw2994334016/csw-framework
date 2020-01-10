package com.three.points.enums;

import lombok.Getter;

/**
 * Created by csw on 2019/10/24.
 * Description:
 */
@Getter
public enum EventEnum {

    EVENT_TYPE_AUDIT_POS(1, "终审人奖分事件分类"),
    EVENT_TYPE_AUDIT_NEG(2, "终审人扣分事件分类"),
    EVENT_NAME_RECORDER_POS(3, "终审人通过，系统自动对记录人奖分事件"),
    EVENT_NAME_RECORDER_NEG(4, "终审人驳回，系统自动对记录人扣分事件"),
    EVENT_NAME_ATTN_POS(5, "终审人通过，系统自动对初审人奖分事件"),
    EVENT_NAME_ATTN_NEG(6, "终审人驳回，系统自动对初审人扣分事件"),

    THEME_NAME_AUDIT_POS(7, "终审人通过，系统自动生成主题"),
    THEME_NAME_AUDIT_NEG(8, "终审人驳回，系统自动生成主题"),
    EVENT_TYPE_MANAGER_TASK(9, "管理任务事件分类"),
    EVENT_NAME_MANAGER_TASK(10, "管理任务事件"),
    THEME_NAME_MANAGER_TASK(11, "系统自动结算管理任务主题");

    private int code;

    private String message;

    EventEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
