package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskStatisticsVo {

    @ApiModelProperty("人员ID")
    private String empId; // 人员ID

    @ApiModelProperty("总奖分")
    private Integer allAwardScore = 0; // 总奖分

    @ApiModelProperty("总扣分")
    private Integer allDeductScore = 0; // 总扣分

    @ApiModelProperty("总人次")
    private Integer allEmpCount = 0; // 总人次

    @ApiModelProperty("总比例")
    private Double allRatio = 0.0; // 总比例

    @ApiModelProperty("统计图x坐标列表")
    private List<String> seriesList = new ArrayList<>(); // 统计图x坐标列表

    @ApiModelProperty("奖分趋势数据列表")
    private List<Integer> awardValueTrendList = new ArrayList<>(); // 奖分趋势数据列表

    @ApiModelProperty("扣分趋势数据列表")
    private List<Integer> deductValueTrendList = new ArrayList<>(); // 扣分趋势数据列表

    @ApiModelProperty("奖扣人次趋势数据列表")
    private List<Integer> empCountValueTrendList = new ArrayList<>(); // 奖扣人次趋势数据列表

    @ApiModelProperty("奖扣分比例趋势数据列表")
    private List<Double> ratioValueTrendList = new ArrayList<>(); // 奖扣分比例趋势数据列表
}
