package com.three.points.controller;

import com.three.common.vo.JsonData;
import com.three.points.entity.PointsTask;
import com.three.points.param.PointsTaskParam;
import com.three.points.service.PointsTaskService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
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

@Api(value = "我发布的任务", tags = "我发布的任务")
@RestController
@RequestMapping("/points/pointsTasks")
public class PointsTaskController {

    @Autowired
    private PointsTaskService pointsTaskService;

    @LogAnnotation(module = "添加积分任务")
    @ApiOperation(value = "添加积分任务")
    @ApiImplicitParam(name = "pointsTaskParam", value = "积分任务信息", required = true, dataType = "PointsTaskParam")
    @PostMapping()
    public JsonResult create(@RequestBody PointsTaskParam pointsTaskParam) {
        pointsTaskService.create(pointsTaskParam);
        return JsonResult.ok("积分任务添加成功");
    }

    @LogAnnotation(module = "修改积分任务")
    @ApiOperation(value = "修改积分任务")
    @ApiImplicitParam(name = "pointsTaskParam", value = "积分任务信息", required = true, dataType = "PointsTaskParam")
    @PutMapping()
    public JsonResult update(@RequestBody PointsTaskParam pointsTaskParam) {
        pointsTaskService.update(pointsTaskParam);
        return JsonResult.ok("积分任务修改成功");
    }

    @LogAnnotation(module = "删除积分任务")
    @ApiOperation(value = "删除积分任务")
    @ApiImplicitParam(name = "ids", value = "积分任务信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        pointsTaskService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("积分任务删除成功");
    }

    @ApiOperation(value = "查询我发布的任务（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "sortKey", value = "排序方式（createDate=最新发布；deadline=截止时间）", dataType = "String"),
            @ApiImplicitParam(name = "chargePersonId", value = "责任人", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<PointsTask> query(Integer page, Integer limit, String sortKey, String chargePersonId) {
        if (page != null && limit != null) {
            return pointsTaskService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), "4", sortKey, chargePersonId);
        } else {
            return pointsTaskService.query(null, StatusEnum.OK.getCode(), "4", sortKey, chargePersonId);
        }
    }

    @ApiOperation(value = "查询我发布的任务详情（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "积分任务信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<PointsTask> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(pointsTaskService.findById(id)).success();
    }
}