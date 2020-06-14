package com.three.points.controller;

import com.three.common.vo.PageQuery;
import com.three.points.entity.CustomReport;
import com.three.points.param.CustomReportParam;
import com.three.points.service.CustomReportService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.JsonData;
import com.three.common.vo.PageResult;
import com.three.points.service.PointsStatisticsService;
import com.three.points.vo.CustomReportVo;
import com.three.points.vo.PointsRankVo;
import com.three.points.vo.ReportGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by csw on 2020-04-11.
 * Description:
 */

@Api(value = "自定义报表", tags = "自定义报表")
@RestController
@RequestMapping("/points/customReports")
public class CustomReportController {

    @Autowired
    private CustomReportService customReportService;

    @Autowired
    private PointsStatisticsService pointsStatisticsService;

    @LogAnnotation(module = "添加自定义报表")
    @ApiOperation(value = "添加自定义报表")
    @ApiImplicitParam(name = "customReportParam", value = "自定义报表信息", required = true, dataType = "CustomReportParam")
    @PostMapping()
    public JsonResult create(@RequestBody CustomReportParam customReportParam) {
        customReportService.create(customReportParam);
        return JsonResult.ok("自定义报表添加成功");
    }

    @LogAnnotation(module = "修改自定义报表")
    @ApiOperation(value = "修改自定义报表")
    @ApiImplicitParam(name = "customReportParam", value = "自定义报表信息", required = true, dataType = "CustomReportParam")
    @PutMapping()
    public JsonResult update(@RequestBody CustomReportParam customReportParam) {
        customReportService.update(customReportParam);
        return JsonResult.ok("自定义报表修改成功");
    }

    @LogAnnotation(module = "删除自定义报表")
    @ApiOperation(value = "删除自定义报表")
    @ApiImplicitParam(name = "ids", value = "自定义报表信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        customReportService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("自定义报表删除成功");
    }

    @ApiOperation(value = "查询自定义报表（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<CustomReport> query(Integer page, Integer limit, String searchValue) {
        return customReportService.query(page, limit, StatusEnum.OK.getCode(), searchValue);
    }

    @ApiOperation(value = "查询自定义报表（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "自定义报表信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<CustomReportVo> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(customReportService.findById(id)).success();
    }

    @ApiOperation(value = "自定义报表排名预览", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "自定义分组信息id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "themeDateSt", value = "开始时间(月份，时间戳，毫秒),默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "themeDateEt", value = "结束时间(月份，时间戳，毫秒),默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "cumulativeFlag", value = "累计排名标志，默认0=不按累计分，1=按累计积分", dataType = "String"),
    })
    @GetMapping("/findRankByGroupId")
    public PageResult<PointsRankVo> findRankByGroupId(@RequestParam(required = true) String groupId, Long themeDateSt, Long themeDateEt, @RequestParam(defaultValue = "0") String cumulativeFlag) {
        return pointsStatisticsService.findRankByGroupId(groupId, themeDateSt, themeDateEt, cumulativeFlag);
    }
}