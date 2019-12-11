package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.service.ThemeDetailService;
import com.three.points.vo.ThemeDetailStatisticsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "积分查询", tags = "积分查询")
@RestController
@RequestMapping("/points/pointsStatistics")
public class PointsStatisticsController {

    @Autowired
    private ThemeDetailService themeDetailService;

    @ApiOperation(value = "人员积分按月份统计（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "orgId", value = "左侧选择组织机构Id", dataType = "String"),
            @ApiImplicitParam(name = "themeDateSt", value = "奖扣时间开始(月份，时间戳，毫秒)", dataType = "Long"),
            @ApiImplicitParam(name = "themeDateEt", value = "奖扣时间结束(月份，时间戳，毫秒),默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "searchValue", value = "搜索人员（姓名/工号/手机号）", dataType = "String")
    })
    @GetMapping("/themeDetailStatistics")
    public PageResult<ThemeDetailStatisticsVo> themeDetailStatistics(Integer page, Integer limit, String orgId, Long themeDateSt, Long themeDateEt, String searchValue) {
        if (page != null && limit != null) {
            return themeDetailService.themeDetailStatistics(new PageQuery(page, limit), StatusEnum.OK.getCode(), orgId, themeDateSt, themeDateEt, searchValue);
        } else {
            return themeDetailService.themeDetailStatistics(null, StatusEnum.OK.getCode(), orgId, themeDateSt, themeDateEt, searchValue);
        }
    }
}
