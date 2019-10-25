package com.three.points.controller;

import com.three.points.entity.EventType;
import com.three.points.param.EventTypeParam;
import com.three.points.service.EventTypeService;
import com.three.common.enums.StatusEnum;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by csw on 2019-10-20.
 * Description:
 */

@Api(value = "事件库", tags = "事件库")
@RestController
@RequestMapping("/points/eventTypes")
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    @LogAnnotation(module = "添加事件分类")
    @ApiOperation(value = "添加事件分类")
    @ApiImplicitParam(name = "eventTypeParam", value = "事件分类信息", required = true, dataType = "EventTypeParam")
    @PostMapping()
    public JsonResult create(@RequestBody EventTypeParam eventTypeParam) {
        EventType eventType = eventTypeService.create(eventTypeParam);
        return JsonResult.ok("事件分类添加成功").put("data", eventType);
    }

    @LogAnnotation(module = "修改事件分类")
    @ApiOperation(value = "修改事件分类")
    @ApiImplicitParam(name = "eventTypeParam", value = "事件分类信息", required = true, dataType = "EventTypeParam")
    @PutMapping()
    public JsonResult update(@RequestBody EventTypeParam eventTypeParam) {
        EventType eventType = eventTypeService.update(eventTypeParam);
        return JsonResult.ok("事件分类修改成功").put("data", eventType);
    }

    @LogAnnotation(module = "删除事件分类")
    @ApiOperation(value = "删除事件分类")
    @ApiImplicitParam(name = "ids", value = "事件分类信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String ids) {
        eventTypeService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("事件分类删除成功");
    }

    @ApiOperation(value = "查询事件分类（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值(事件分类名)", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<EventType> query(Integer page, Integer limit, String searchValue) {
        if (page != null && limit != null) {
            PageQuery pageQuery = new PageQuery(page, limit);
            BeanValidator.check(pageQuery);
            return eventTypeService.query(pageQuery, StatusEnum.OK.getCode(), searchValue);
        } else {
            return eventTypeService.query(null, StatusEnum.OK.getCode(), searchValue);
        }
    }

    @ApiOperation(value = "查询所有事件分类(树形结构)")
    @GetMapping("/tree")
    public JsonResult findAllWithTree() {
        return JsonResult.ok("查找成功").put("data", eventTypeService.findAllWithTree(StatusEnum.OK.getCode()));
    }

    @ApiOperation(value = "查询子事件分类(根据ID)")
    @ApiImplicitParam(name = "id", value = "事件分类ID(id没有表示查找一级事件分类)", dataType = "String")
    @GetMapping("/findChildren")
    public JsonResult findChildren(String id) {
        return JsonResult.ok("查找成功").put("data", eventTypeService.findChildren(StatusEnum.OK.getCode(), id));
    }
}