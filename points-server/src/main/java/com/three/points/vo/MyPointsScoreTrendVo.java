package com.three.points.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MyPointsScoreTrendVo {

    @ApiModelProperty("人员Id")
    private String empId; // 人员Id

    @ApiModelProperty("人员姓名")
    private String empFullName; // 人员姓名

    @ApiModelProperty("人员部门Id")
    private String empOrgId; // 人员部门Id

    @ApiModelProperty("人员部门")
    private String empOrgName; // 人员部门

    @ApiModelProperty("月度积分，奖分")
    private Integer monthAwardScore = 0; // 月度积分，奖分

    @ApiModelProperty("月度积分，扣分")
    private Integer monthDeductScore = 0;  // 月度积分，扣分

    @ApiModelProperty("月度排名")
    private Integer monthRanking; // 月度排名

    @ApiModelProperty("月度积分趋势图x坐标")
    private List<String> monthSeriesList = new ArrayList<>(); // 月度积分趋势图x坐标

    @ApiModelProperty("月度积分趋势数据，奖分")
    private List<Integer> monthAwardValueTrendList = new ArrayList<>(); // 月度积分趋势数据，奖分

    @ApiModelProperty("月度积分趋势数据，扣分")
    private List<Integer> monthDeductValueTrendList = new ArrayList<>(); // 月度积分趋势数据，扣分

    @ApiModelProperty("年度积分，奖分")
    private Integer yearAwardScore = 0; // 年度积分，奖分

    @ApiModelProperty("年度积分，扣分")
    private Integer yearDeductScore = 0;  // 年度积分，扣分

    @ApiModelProperty("年度排名")
    private Integer yearRanking; // 年度排名

    @ApiModelProperty("年度积分趋势图x坐标")
    private List<String> yearSeriesList = new ArrayList<>(); // 年度积分趋势图x坐标

    @ApiModelProperty("年度积分趋势数据，奖分")
    private List<Integer> yearAwardValueTrendList = new ArrayList<>(); // 年度积分趋势数据，奖分

    @ApiModelProperty("年度积分趋势数据，扣分")
    private List<Integer> yearDeductValueTrendList = new ArrayList<>(); // 年度积分趋势数据，扣分

    @ApiModelProperty("累计积分，奖分")
    private Integer totalAwardScore = 0; // 累计积分，奖分

    @ApiModelProperty("累计积分，扣分")
    private Integer totalDeductScore = 0;  // 累计积分，扣分

    @ApiModelProperty("累计排名")
    private Integer totalRanking; // 累计排名

    @ApiModelProperty("累计积分趋势图x坐标")
    private List<String> totalSeriesList = new ArrayList<>(); // 累计积分趋势图x坐标

    @ApiModelProperty("累计积分趋势数据，奖分")
    private List<Integer> totalAwardValueTrendList = new ArrayList<>(); // 累计积分趋势数据，奖分

    @ApiModelProperty("累计积分趋势数据，扣分")
    private List<Integer> totalDeductValueTrendList = new ArrayList<>(); // 累计积分趋势数据，扣分

}
