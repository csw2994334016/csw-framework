package com.three.zuulserver.vo;

import lombok.Data;

/**
 * Created by csw on 2019/09/08.
 * Description:
 */
@Data
public class ServiceInfo {

    private String serverId;

    private String host;

    private int port;

    private String instanceId;

    private String zuulPath;
}
