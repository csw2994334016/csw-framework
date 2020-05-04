package com.three.authserver.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019/10/01.
 * Description:
 */
@Data
public class ChangePwdParam {

    @NotBlank(message = "client_id不可以为空")
    private String client_id;

    @NotBlank(message = "旧密码不可以为空")
    private String oldPwd;

    @NotBlank(message = "新密码不可以为空")
    private String newPwd;

    @NotBlank(message = "确认密码不可以为空")
    private String rePwd;
}
