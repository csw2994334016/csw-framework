package com.three.develop.controller;

import com.three.resource_jpa.jpa.entity.entity.EntityField;
import com.three.resource_jpa.jpa.entity.entity.EntityPojo;
import com.three.resource_jpa.jpa.entity.param.EntityFieldParam;
import com.three.resource_jpa.jpa.entity.param.EntityPojoParam;
import com.three.resource_jpa.jpa.entity.service.EntityPojoService;
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

import java.util.List;

/**
 * Created by  on 2020-06-26.
 * Description:
 */

@Api(value = "实体管理", tags = "实体管理")
@RestController
@RequestMapping("/three-develop-server/entityPojo")
public class EntityPojoController {

    @Autowired
    private EntityPojoService entityPojoService;

    @LogAnnotation(module = "develop-server添加实体信息")
    @ApiOperation(value = "develop-server添加实体信息")
    @ApiImplicitParam(name = "entityPojoParam", value = "实体信息信息", required = true, dataType = "EntityPojoParam")
    @PostMapping()
    public JsonResult create(@RequestBody EntityPojoParam entityPojoParam) {
        entityPojoService.create(entityPojoParam);
        return JsonResult.ok("实体信息添加成功");
    }

    @LogAnnotation(module = "develop-server修改实体信息")
    @ApiOperation(value = "develop-server修改实体信息")
    @ApiImplicitParam(name = "entityPojoParam", value = "实体信息信息", required = true, dataType = "EntityPojoParam")
    @PutMapping()
    public JsonResult update(@RequestBody EntityPojoParam entityPojoParam) {
        entityPojoService.update(entityPojoParam);
        return JsonResult.ok("实体信息修改成功");
    }

    @LogAnnotation(module = "develop-server删除实体信息")
    @ApiOperation(value = "develop-server删除实体信息")
    @ApiImplicitParam(name = "ids", value = "实体信息信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        entityPojoService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("实体信息删除成功");
    }

    @ApiOperation(value = "develop-server查询实体信息（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<EntityPojo> query(Integer page, Integer limit, String searchValue) {
        return entityPojoService.query(page, limit, StatusEnum.OK.getCode(), searchValue);
    }

    @ApiOperation(value = "develop-server查询实体信息（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "实体信息信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonData<EntityPojo> findById(@RequestParam(required = true) String id) {
        return new JsonData<>(entityPojoService.findById(id)).success();
    }

    @ApiOperation(value = "develop-server查询实体设置字段", notes = "")
    @ApiImplicitParam(name = "entityPojoId", value = "实体信息信息id", required = true, dataType = "String")
    @GetMapping("/findFieldsByEntityPojoId")
    public JsonData<List<EntityField>> findFieldsByEntityPojoId(@RequestParam(required = true) String entityPojoId) {
        return new JsonData<>(entityPojoService.findFieldsByEntityPojoId(entityPojoId));
    }

    @LogAnnotation(module = "develop-server保存实体设置字段")
    @ApiOperation(value = "develop-server保存实体设置字段")
    @ApiImplicitParam(name = "entityFieldParam", value = "实体字段信息", required = true, dataType = "EntityFieldParam")
    @PostMapping("/saveField")
    public JsonResult saveField(@RequestBody EntityFieldParam entityFieldParam) {
        entityPojoService.saveField(entityFieldParam);
        return JsonResult.ok("实体字段保存成功");
    }

    @LogAnnotation(module = "develop-server删除实体设置字段")
    @ApiOperation(value = "develop-server删除实体设置字段")
    @ApiImplicitParam(name = "ids", value = "实体设置字段信息ids", required = true, dataType = "String")
    @DeleteMapping("/deleteField")
    public JsonResult deleteField(@RequestParam(required = true) String ids) {
        entityPojoService.deleteField(ids);
        return JsonResult.ok("实体字段删除成功");
    }
}