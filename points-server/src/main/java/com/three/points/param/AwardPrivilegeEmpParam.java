package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
@Data
public class AwardPrivilegeEmpParam {


    @NotBlank(message = "奖扣权限id不可以为空")
    @ApiModelProperty("奖扣权限id")
    private String awardPrivilegeId; // 奖扣权限id

    @ApiModelProperty("奖扣权限绑定员工对象List")
    private List<AwardPrivilegeEmpParam1> awardPrivilegeEmpParam1List = new ArrayList<>(); // 奖扣权限绑定员工对象List

}