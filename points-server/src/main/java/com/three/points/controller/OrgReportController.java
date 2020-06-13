package com.three.points.controller;

import com.three.common.vo.PageResult;
import com.three.points.service.PointsStatisticsService;
import com.three.points.vo.PointsRankVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by csw on 2020-04-11.
 * Description:
 */

@Api(value = "组织机构排名", tags = "组织机构排名")
@RestController
@RequestMapping("/points/orgReports")
public class OrgReportController {

    @Autowired
    private PointsStatisticsService pointsStatisticsService;

    @ApiOperation(value = "查询组织机构人员排名信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "组织机构Id，默认一级机构", dataType = "String"),
            @ApiImplicitParam(name = "themeDateSt", value = "开始时间(月份，时间戳，毫秒),默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "themeDateEt", value = "结束时间(月份，时间戳，毫秒),默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "containChildFlag", value = "包含子级部门人员标记：0=不包含；1=包含（默认0）", defaultValue = "0", dataType = "String"),
    })
    @GetMapping("/queryOrgRank")
    public PageResult<PointsRankVo> queryOrgRank(String orgId, Long themeDateSt, Long themeDateEt, String containChildFlag) {
        return pointsStatisticsService.queryOrgRank(orgId, themeDateSt, themeDateEt, containChildFlag);
    }
}