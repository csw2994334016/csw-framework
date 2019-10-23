package com.three.user.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RoleParam {

    private String id;

    @Length(min = 2, max = 100, message = "角色名称长度需要在2-100个字之间")
    private String roleName;

    private String roleCode; // 角色编码

    private String englishName; // 英文名称

    private String remark;

}