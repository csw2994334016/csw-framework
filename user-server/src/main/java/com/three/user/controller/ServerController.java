package com.three.user.controller;

import com.three.user.entity.Server;
import com.three.user.param.ServerParam;
import com.three.user.service.ServerService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.JsonData;
import com.three.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by csw on 2020-07-06.
 * Description:
 */

@Api(value = "服务管理", tags = "服务管理")
@RestController
@RequestMapping("/sys/service")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @LogAnnotation(module = "添加服务信息")
    @ApiOperation(value = "添加服务信息")
    @ApiImplicitParam(name = "serverParam", value = "服务信息信息", required = true, dataType = "ServerParam")
    @PostMapping()
    public JsonResult create(@RequestBody ServerParam serverParam) {
        serverService.create(serverParam);
        return JsonResult.ok("服务信息添加成功");
    }

    @LogAnnotation(module = "修改服务信息")
    @ApiOperation(value = "修改服务信息")
    @ApiImplicitParam(name = "serverParam", value = "服务信息信息", required = true, dataType = "ServerParam")
    @PutMapping()
    public JsonResult update(@RequestBody ServerParam serverParam) {
        serverService.update(serverParam);
        return JsonResult.ok("服务信息修改成功");
    }

    @LogAnnotation(module = "删除服务信息")
    @ApiOperation(value = "删除服务信息")
    @ApiImplicitParam(name = "ids", value = "服务信息信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        serverService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("服务信息删除成功");
    }

    @ApiOperation(value = "查询服务信息（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<Server> query(Integer page, Integer limit, String searchValue) {
        return serverService.query(page, limit, StatusEnum.OK.getCode(), searchValue);
    }

    @ApiOperation(value = "查询服务信息（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "服务信息信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<Server> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(serverService.findById(id)).success();
    }
}