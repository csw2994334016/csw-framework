package com.three.zuulserver.param;

import lombok.Data;

@Data
public class LoginParam {

    private String username;
    private String password;

    private String client_id;
    private String client_secret;
    private String scope;

    private String refresh_token;
}
