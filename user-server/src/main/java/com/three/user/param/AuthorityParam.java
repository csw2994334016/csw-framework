package com.three.user.param;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by csw on 2018/9/14.
 * Description:
 */
@Builder
@Data
public class AuthorityParam {

    private String id;

    private String authorityUrl;

    @Length(min = 2, max = 20, message = "权限名称长度需要在2-20个字之间")
    private String authorityName;

    private Integer authorityType;

    private String authorityIcon;

    @Builder.Default
    private Integer sort = 0;

    @Builder.Default
    private String parentId = "-1";
}
