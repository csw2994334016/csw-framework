package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
@Data
public class CustomGroupEmpParam {


    @NotBlank(message = "分组id不可以为空")
    @ApiModelProperty("分组id")
    private String customGroupId; // 分组id

    @ApiModelProperty("分组绑定员工对象List")
    private List<CustomGroupEmpParam1> customGroupEmpParam1List = new ArrayList<>(); // 分组绑定员工对象List

}