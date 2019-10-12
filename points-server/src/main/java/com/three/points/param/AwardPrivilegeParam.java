package com.three.points.param;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
@Builder
@Data
public class AwardPrivilegeParam {

    private String id;

    @NotBlank(message = "奖扣权限名称不可以为空")
    private String name; // 奖扣权限名称

    @NotBlank(message = "A分权限不可以为空")
    private String aScore; // A分权限

    @NotBlank(message = "B分权限不可以为空")
    private String bScore; // B分权限


    private String remark;

}