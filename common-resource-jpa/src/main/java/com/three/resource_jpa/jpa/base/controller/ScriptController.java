package com.three.resource_jpa.jpa.base.controller;

import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonData;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.common.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.DataApiServiceExecutor;
import com.three.resource_jpa.jpa.script.entity.Script;
import com.three.resource_jpa.jpa.script.param.ScriptParam;
import com.three.resource_jpa.jpa.script.service.ScriptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Api(value = "脚本管理", tags = "脚本管理")
@RestController
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @LogAnnotation(module = "添加脚本")
    @ApiOperation(value = "添加脚本")
    @ApiImplicitParam(name = "scriptParam", value = "脚本信息", required = true, dataType = "ScriptParam")
    @PostMapping("/{serverId}/scripts")
    public JsonResult create(@RequestBody ScriptParam scriptParam, @PathVariable String serverId) {
        scriptService.create(scriptParam);
        return JsonResult.ok("添加成功");
    }

    @LogAnnotation(module = "修改脚本")
    @ApiOperation(value = "修改脚本")
    @ApiImplicitParam(name = "scriptParam", value = "脚本信息", required = true, dataType = "ScriptParam")
    @PutMapping("/{serverId}/scripts")
    public JsonResult update(@RequestBody ScriptParam scriptParam, @PathVariable String serverId) {
        scriptService.update(scriptParam);
        return JsonResult.ok("修改成功");
    }

    @LogAnnotation(module = "删除脚本")
    @ApiOperation(value = "删除脚本")
    @ApiImplicitParam(name = "ids", value = "groovy脚本ids", required = true, dataType = "String")
    @DeleteMapping("/{serverId}/scripts")
    public JsonResult delete(String ids, @PathVariable String serverId) {
        scriptService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("删除成功");
    }

    @ApiOperation(value = "查询脚本（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "脚本名称", dataType = "String")
    })
    @GetMapping("/{serverId}/scripts/query")
    public PageResult<Script> query(Integer page, Integer limit, String searchValue, @PathVariable String serverId) {
        if (page != null && limit != null) {
            return scriptService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchValue);
        } else {
            return scriptService.query(null, StatusEnum.OK.getCode(), searchValue);
        }
    }

    @ApiOperation(value = "查看脚本代码")
    @ApiImplicitParam(name = "id", value = "脚本信息id", required = true, dataType = "String")
    @GetMapping("/{serverId}/scripts/findCode")
    public JsonData<Script> findCode(@RequestParam(required = true) String id, @PathVariable String serverId) {
        return new JsonData<>(scriptService.findCode(id));
    }

    @LogAnnotation(module = "保存脚本代码")
    @ApiOperation(value = "保存脚本代码")
    @ApiImplicitParam(name = "scriptParam", value = "develop-server脚本信息", required = true, dataType = "ScriptParam")
    @PutMapping("/{serverId}/scripts/saveCode")
    public JsonResult saveCode(@RequestBody ScriptParam scriptParam, @PathVariable String serverId) {
        scriptService.saveCode(scriptParam);
        return JsonResult.ok("保存成功");
    }

}
