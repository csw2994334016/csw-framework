package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonData;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.entity.PointsTask;
import com.three.points.param.PointsTaskParam;
import com.three.points.service.PointsTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by csw on 2019-11-04.
 * Description:
 */

@Api(value = "我收到的任务", tags = "我收到的任务")
@RestController
@RequestMapping("/points/pointsTasksReceives")
public class PointsTaskReceiveController {

    @Autowired
    private PointsTaskService pointsTaskService;

    @ApiOperation(value = "查询我收到的任务（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "whoFlag", value = "我参与的/我负责的,1=我参与的;2=我负责的(默认1)", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<PointsTask> query(Integer page, Integer limit, @RequestParam(defaultValue = "1") String whoFlag) {
        if (page != null && limit != null) {
            return pointsTaskService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), whoFlag, null, null);
        } else {
            return pointsTaskService.query(null, StatusEnum.OK.getCode(), whoFlag, null, null);
        }
    }

    @ApiOperation(value = "查询我收到的任务详情（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "积分任务信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<PointsTask> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(pointsTaskService.findById(id)).success();
    }

    @LogAnnotation(module = "任务完成")
    @ApiOperation(value = "任务完成", notes = "")
    @ApiImplicitParam(name = "id", value = "积分任务信息id", required = true, dataType = "String")
    @GetMapping("/completeTask")
    public JsonResult completeTask(@RequestParam(required = true) String id) {
        pointsTaskService.completeTask(id);
        return JsonResult.ok();
    }
}