package com.three.points.controller;

import com.three.common.vo.JsonData;
import com.three.points.entity.CustomReport;
import com.three.points.service.CustomReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by csw on 2020-04-11.
 * Description:
 */

@Api(value = "我的报表排名", tags = "我的报表排名")
@RestController
@RequestMapping("/points/myCustomReports")
public class MyCustomReportController {

    @Autowired
    private CustomReportService customReportService;

    @ApiOperation(value = "查询我的自定义报表", notes = "")
    @ApiImplicitParam(name = "empId", value = "人员Id，可以不传参", dataType = "String")
    @GetMapping("/queryMyReport")
    public JsonData<List<CustomReport>> queryMyReport(String empId) {
        return new JsonData<>(customReportService.queryMyReport(empId));
    }
}