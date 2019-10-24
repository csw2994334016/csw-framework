package com.three.points.controller;

import com.three.points.entity.Event;
import com.three.points.param.EventParam;
import com.three.points.param.MoveEventParam;
import com.three.points.service.EventService;
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

@Api(value = "事件", tags = "事件")
@RestController
@RequestMapping("/points/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @LogAnnotation(module = "添加事件")
    @ApiOperation(value = "添加事件")
    @ApiImplicitParam(name = "eventParam", value = "事件信息", required = true, dataType = "EventParam")
    @PostMapping()
    public JsonResult create(@RequestBody EventParam eventParam) {
        eventService.create(eventParam);
        return JsonResult.ok("事件添加成功");
    }

    @LogAnnotation(module = "修改事件")
    @ApiOperation(value = "修改事件")
    @ApiImplicitParam(name = "eventParam", value = "事件信息", required = true, dataType = "EventParam")
    @PutMapping()
    public JsonResult update(@RequestBody EventParam eventParam) {
        eventService.update(eventParam);
        return JsonResult.ok("事件修改成功");
    }

    @LogAnnotation(module = "删除事件")
    @ApiOperation(value = "删除事件")
    @ApiImplicitParam(name = "ids", value = "事件信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        eventService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("事件删除成功");
    }

    @ApiOperation(value = "查询事件（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "typeId", value = "事件分类ID", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值(事件名称)", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<Event> query(Integer page, Integer limit, String typeId, String searchValue) {
        if (page != null && limit != null) {
            PageQuery pageQuery = new PageQuery(page, limit);
            BeanValidator.check(pageQuery);
            return eventService.query(pageQuery, StatusEnum.OK.getCode(), typeId, searchValue);
        } else {
            return eventService.query(null, StatusEnum.OK.getCode(), typeId, searchValue);
        }
    }

    @LogAnnotation(module = "移库")
    @ApiOperation(value = "移库")
    @ApiImplicitParam(name = "moveEventParam", value = "事件信息", required = true, dataType = "MoveEventParam")
    @PutMapping("moveEvent")
    public JsonResult moveEvent(@RequestBody MoveEventParam moveEventParam) {
        eventService.moveEvent(moveEventParam);
        return JsonResult.ok("移库成功");
    }
}