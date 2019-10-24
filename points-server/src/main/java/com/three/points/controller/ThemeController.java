package com.three.points.controller;

import com.three.points.entity.Theme;
import com.three.points.param.ThemeParam;
import com.three.points.service.ThemeService;
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

@Api(value = "积分奖扣主题", tags = "积分奖扣主题")
@RestController
@RequestMapping("/points/themes")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @LogAnnotation(module = "保存积分奖扣主题到草稿")
    @ApiOperation(value = "保存积分奖扣主题到草稿")
    @ApiImplicitParam(name = "themeParam", value = "积分奖扣主题信息", required = true, dataType = "ThemeParam")
    @PostMapping("/createDraft")
    public JsonResult createDraft(@RequestBody ThemeParam themeParam) {
        themeService.createDraft(themeParam);
        return JsonResult.ok("积分奖扣主题草稿保存成功");
    }

    @LogAnnotation(module = "添加积分奖扣主题")
    @ApiOperation(value = "添加积分奖扣主题")
    @ApiImplicitParam(name = "themeParam", value = "积分奖扣主题信息", required = true, dataType = "ThemeParam")
    @PostMapping()
    public JsonResult create(@RequestBody ThemeParam themeParam) {
        themeService.create(themeParam);
        return JsonResult.ok("积分奖扣主题添加成功");
    }

    @LogAnnotation(module = "修改积分奖扣主题")
    @ApiOperation(value = "修改积分奖扣主题")
    @ApiImplicitParam(name = "themeParam", value = "积分奖扣主题信息", required = true, dataType = "ThemeParam")
    @PutMapping()
    public JsonResult update(@RequestBody ThemeParam themeParam) {
        themeService.update(themeParam);
        return JsonResult.ok("积分奖扣主题修改成功");
    }

    @LogAnnotation(module = "删除积分奖扣主题")
    @ApiOperation(value = "删除积分奖扣主题")
    @ApiImplicitParam(name = "ids", value = "积分奖扣主题信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        themeService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("积分奖扣主题删除成功");
    }

    @ApiOperation(value = "查询积分奖扣主题（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "searchValue", value = "筛选值", dataType = "String")
    })
    @GetMapping("/query")
    public PageResult<Theme> query(Integer page, Integer limit, String searchValue) {
        if (page != null && limit != null) {
            return themeService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), searchValue);
        } else {
            return themeService.query(null, StatusEnum.OK.getCode(), searchValue);
        }
    }

    @ApiOperation(value = "查询积分奖扣主题（根据ID查找）")
    @ApiImplicitParam(name = "id", value = "积分奖扣主题信息id", required = true, dataType = "String")
    @GetMapping()
    public JsonResult findById(@RequestParam(required = true) String id) {
        return JsonResult.ok().put("data", themeService.findById(id));
    }


}