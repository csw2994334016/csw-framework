package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.entity.PointsTask;
import com.three.points.service.PointsTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by csw on 2019-11-04.
 * Description:
 */

@Api(value = "抄送给我的任务", tags = "抄送给我的任务")
@RestController
@RequestMapping("/points/pointsTasksCopies")
public class PointsTaskCopyController {

    @Autowired
    private PointsTaskService pointsTaskService;

    @ApiOperation(value = "查询抄送给我的任务（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "sortKey", value = "排序方式（createDate=最新发布；deadline=截止时间）", dataType = "String"),
            @ApiImplicitParam(name = "chargePersonId", value = "责任人", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<PointsTask> query(Integer page, Integer limit, String sortKey, String chargePersonId) {
        if (page != null && limit != null) {
            return pointsTaskService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), "3", sortKey, chargePersonId);
        } else {
            return pointsTaskService.query(null, StatusEnum.OK.getCode(), "3", sortKey, chargePersonId);
        }
    }

    @ApiOperation(value = "查询抄送给我的任务详情（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "积分任务信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonResult findById(@RequestParam(required = true) String id) {
        return JsonResult.ok().put("data", pointsTaskService.findById(id));
    }
}