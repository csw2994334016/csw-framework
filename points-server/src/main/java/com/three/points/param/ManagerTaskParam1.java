package com.three.points.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csw on 2019-11-06.
 * Description:
 */
@Data
public class ManagerTaskParam1 {

    @NotBlank(message = "管理任务Id")
    @ApiModelProperty("管理任务Id")
    private String taskId; // 管理任务Id

    @ApiModelProperty("任务考核人员列表")
    List<ManagerTaskEmpParam> managerTaskEmpParamList = new ArrayList<>(); // 任务考核人员列表

}