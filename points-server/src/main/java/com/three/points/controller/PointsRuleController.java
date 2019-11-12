package com.three.points.controller;

import com.three.points.entity.PointsRule;
import com.three.points.param.PointsRuleParam;
import com.three.points.service.PointsRuleService;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by csw on 2019-11-12.
 * Description:
 */

@Api(value = "积分规则设置", tags = "积分规则设置")
@RestController
@RequestMapping("/points/pointsRules")
public class PointsRuleController {

    @Autowired
    private PointsRuleService pointsRuleService;

    @LogAnnotation(module = "添加积分规则设置")
    @ApiOperation(value = "添加积分规则设置")
    @ApiImplicitParam(name = "pointsRuleParam", value = "积分规则设置信息", required = true, dataType = "PointsRuleParam")
    @PostMapping()
    public JsonResult create(@RequestBody PointsRuleParam pointsRuleParam) {
        pointsRuleService.create(pointsRuleParam);
        return JsonResult.ok("积分规则设置添加成功");
    }

    @LogAnnotation(module = "修改积分规则设置")
    @ApiOperation(value = "修改积分规则设置")
    @ApiImplicitParam(name = "pointsRuleParam", value = "积分规则设置信息", required = true, dataType = "PointsRuleParam")
    @PutMapping()
    public JsonResult update(@RequestBody PointsRuleParam pointsRuleParam) {
        pointsRuleService.update(pointsRuleParam);
        return JsonResult.ok("积分规则设置修改成功");
    }

    @ApiOperation(value = "查询系统积分规则设置", notes = "")
    @GetMapping("/findPointsRule")
    public JsonData<PointsRule> findPointsRule() {
        return new JsonData<>(pointsRuleService.findPointsRule()).success();
    }
}