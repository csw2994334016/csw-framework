package com.three.common.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志对象
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log implements Serializable {

    private String username;
    /**
     * 模块
     */
    private String module;

    /**
     * 方法名
     */
    private String method;
    /**
     * 参数值
     */
    private String params;
    /**
     * 备注记录失败原因
     */
    private String message;
    /**
     * 是否执行成功
     */
    private Boolean flag;
    /**
     * 请求耗时
     */
    private Long time;
    /**
     * 请求ip
     */
    private String ipAddress;

    private String osName;

    private String device;

    private String browserType;
}
