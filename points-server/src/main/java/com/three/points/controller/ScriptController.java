package com.three.points.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.script.entity.Script;
import com.three.resource_jpa.jpa.script.param.ScriptParam;
import com.three.resource_jpa.jpa.script.service.ScriptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "脚本管理", tags = "脚本管理")
@RestController
@RequestMapping("/three-points-server/scripts")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @LogAnnotation(module = "points-server添加脚本")
    @ApiOperation(value = "points-server添加脚本")
    @ApiImplicitParam(name = "scriptParam", value = "points-server脚本信息", required = true, dataType = "ScriptParam")
    @PostMapping()
    public JsonResult create(@RequestBody ScriptParam scriptParam) {
        scriptService.create(scriptParam);
        return JsonResult.ok("添加成功");
    }

    @LogAnnotation(module = "points-server修改脚本")
    @ApiOperation(value = "points-server修改脚本")
    @ApiImplicitParam(name = "scriptParam", value = "points-server脚本信息", required = true, dataType = "ScriptParam")
    @PutMapping()
    public JsonResult update(@RequestBody ScriptParam scriptParam) {
        scriptService.update(scriptParam);
        return JsonResult.ok("修改成功");
    }

    @LogAnnotation(module = "points-server删除脚本")
    @ApiOperation(value = "points-server删除脚本")
    @ApiImplicitParam(name = "ids", value = "groovy脚本ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        scriptService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("删除成功");
    }

    @ApiOperation(value = "points-server查询脚本（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "脚本名称", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<Script> query(Integer page, Integer limit, String searchValue) {
        PageQuery pageQuery = new PageQuery(page, limit);
        BeanValidator.check(pageQuery);
        return scriptService.query(pageQuery, StatusEnum.OK.getCode(), searchValue);
    }

}
