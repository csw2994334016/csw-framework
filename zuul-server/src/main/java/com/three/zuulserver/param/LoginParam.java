package com.three.zuulserver.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginParam {

    @ApiModelProperty("登录用户名")
    private String username;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("客户端ID：app端默认是app;web端默认是system")
    private String client_id;
    @ApiModelProperty("客户端密码：app端默认是123456;web端默认是system")
    private String client_secret;
    @ApiModelProperty("scope：app端默认是app;web端默认是system")
    private String scope;

    @ApiModelProperty("刷新token传入参数，登录的时候可以为空")
    private String refresh_token;
}
