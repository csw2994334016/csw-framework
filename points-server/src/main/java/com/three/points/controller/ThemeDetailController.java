package com.three.points.controller;

import com.three.points.entity.ThemeDetail;
import com.three.points.service.ThemeDetailService;
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
 * Created by csw on 2019-10-24.
 * Description:
 */

@Api(value = "积分奖扣主题详情", tags = "积分奖扣主题详情")
@RestController
@RequestMapping("/points/themeDetails")
public class ThemeDetailController {

    @Autowired
    private ThemeDetailService themeDetailService;

    @LogAnnotation(module = "删除积分奖扣主题详情")
    @ApiOperation(value = "删除积分奖扣主题详情")
    @ApiImplicitParam(name = "ids", value = "积分奖扣主题详情信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        themeDetailService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("积分奖扣主题详情删除成功");
    }

    @ApiOperation(value = "查询积分奖扣主题详情（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<ThemeDetail> query(Integer page, Integer limit, String searchValue) {
        if (page != null && limit != null) {
            return themeDetailService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchValue);
        } else {
            return themeDetailService.query(null, StatusEnum.OK.getCode(), searchValue);
        }
    }

    @ApiOperation(value = "查询积分奖扣主题详情（根据ID查找）")
    @ApiImplicitParam(name = "id", value = "积分奖扣主题详情信息id", required = true, dataType = "String")
    @GetMapping()
    public JsonResult findById(@RequestParam(required = true) String id) {
        return JsonResult.ok().put("data", themeDetailService.findById(id));
    }
}