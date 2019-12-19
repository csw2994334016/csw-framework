package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.vo.JsonData;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.entity.ManagerTaskScore;
import com.three.points.service.ManagerTaskScoreService;
import com.three.points.service.ThemeDetailService;
import com.three.points.vo.MyPointsScoreTrendVo;
import com.three.points.vo.ThemeDetailDailyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "我的积分", tags = "我的积分")
@RestController
@RequestMapping("/points/myPoints")
public class MyPointsController {

    @Autowired
    private ThemeDetailService themeDetailService;

    @Autowired
    private ManagerTaskScoreService managerTaskScoreService;

    @ApiOperation(value = "人员积分总览(包括月度、年度、累计积分趋势)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "themeDate", value = "奖扣时间(月份，时间戳，毫秒)", dataType = "Long"),
            @ApiImplicitParam(name = "empId", value = "人员ID，不传则表示当前登录用户", dataType = "String")
    })
    @GetMapping("/totalScore")
    public JsonData<MyPointsScoreTrendVo> totalScore(Long themeDate, String empId) {
        return new JsonData<>(themeDetailService.totalScore(themeDate, empId));
    }

    @ApiOperation(value = "日常奖扣（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "themeDate", value = "奖扣时间(月份，时间戳，毫秒)", dataType = "Long"),
    })
    @GetMapping("/themeDetailDaily")
    public PageResult<ThemeDetailDailyVo> themeDetailDaily(Integer page, Integer limit, Long themeDate) {
        if (page != null && limit != null) {
            return themeDetailService.themeDetailDaily(new PageQuery(page, limit), StatusEnum.OK.getCode(), themeDate, null);
        } else {
            return themeDetailService.themeDetailDaily(null, StatusEnum.OK.getCode(), themeDate, null);
        }
    }

    @ApiOperation(value = "奖扣任务（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "taskDate", value = "管理任务时间(月份，时间戳，毫秒)", dataType = "Long"),
    })
    @GetMapping("/managerTaskScore")
    public PageResult<ManagerTaskScore> managerTaskScore(Integer page, Integer limit, Long taskDate) {
        if (page != null && limit != null) {
            return managerTaskScoreService.managerTaskScore(new PageQuery(page, limit), StatusEnum.OK.getCode(), taskDate, null);
        } else {
            return managerTaskScoreService.managerTaskScore(null, StatusEnum.OK.getCode(), taskDate, null);
        }
    }
}
