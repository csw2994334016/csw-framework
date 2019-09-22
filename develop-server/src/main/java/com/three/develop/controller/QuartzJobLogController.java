package com.three.develop.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.develop.entity.QuartzJobLog;
import com.three.develop.service.QuartzJobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "定时任务", tags = "定时任务")
@RestController
@RequestMapping("/develop/quartzJobLogs")
public class QuartzJobLogController {

    @Autowired
    private QuartzJobLogService quartzJobLogService;

    @LogAnnotation(module = "删除调度日志")
    @ApiOperation(value = "删除调度日志")
    @ApiImplicitParam(name = "ids", value = "调度日志ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String ids) {
        quartzJobLogService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("删除成功");
    }

    @ApiOperation(value = "查询调度日志（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段(任务名称)", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String")
    })
    @PostMapping("/query")
    public PageResult<QuartzJobLog> query(Integer page, Integer limit, String searchKey, String searchValue, String startDate, String endDate) {
        PageQuery pageQuery = new PageQuery(page, limit);
        BeanValidator.check(pageQuery);
        return quartzJobLogService.query(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue, startDate, endDate);
    }

}
