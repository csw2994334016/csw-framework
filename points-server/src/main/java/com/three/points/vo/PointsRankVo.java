package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class PointsRankVo {

    @ApiModelProperty("员工Id")
    private String empId; // 员工Id

    @ApiModelProperty("员工工号")
    private String empNum; // 员工工号

    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @ApiModelProperty("人员部门")
    private String empOrgName; // 人员部门

    @ApiModelProperty("分值")
    private Integer bscoreAll = 0; // 分值

    @ApiModelProperty("排名")
    private Integer sort = 0; // 排名

    public PointsRankVo(String empId, String empNum, String empFullName, String empOrgName) {
        this.empId = empId;
        this.empNum = empNum;
        this.empFullName = empFullName;
        this.empOrgName = empOrgName;
    }

    public PointsRankVo(String empId) {
        this.empId = empId;
    }
}
