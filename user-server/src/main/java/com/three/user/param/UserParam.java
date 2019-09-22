package com.three.user.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by csw on 2019/03/27.
 * Description:
 */
@Data
public class UserParam implements Serializable {

    private String id;

    @NotEmpty(message = "用户名不能为空")
    private String username;

    private String fullName;

    private String phone;

    private String remark;

    private String roleIds;

}
