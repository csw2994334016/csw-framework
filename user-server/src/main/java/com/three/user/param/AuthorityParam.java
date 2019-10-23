package com.three.user.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by csw on 2018/9/14.
 * Description:
 */
@Data
public class AuthorityParam {

    private String id;

    private String authorityUrl;

    @Length(min = 2, max = 100, message = "权限名称长度需要在2-100个字之间")
    private String authorityName;

    private Integer authorityType;

    private String authorityIcon;

    private Integer sort = 0;

    private String parentId = "-1";
}
