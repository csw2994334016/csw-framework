package com.three.points.controller;

import com.three.points.entity.Theme;
import com.three.points.param.ThemeParam;
import com.three.points.service.ThemeDetailService;
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

@Api(value = "积分奖扣", tags = "积分奖扣")
@RestController
@RequestMapping("/points/themes")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeDetailService themeDetailService;

    @LogAnnotation(module = "保存积分奖扣到草稿")
    @ApiOperation(value = "保存积分奖扣到草稿")
    @ApiImplicitParam(name = "themeParam", value = "积分奖扣信息", required = true, dataType = "ThemeParam")
    @PostMapping("/createDraft")
    public JsonResult createDraft(@RequestBody ThemeParam themeParam) {
        themeService.createDraft(themeParam);
        return JsonResult.ok("积分奖扣草稿保存成功");
    }

    @LogAnnotation(module = "添加积分奖扣")
    @ApiOperation(value = "添加积分奖扣")
    @ApiImplicitParam(name = "themeParam", value = "积分奖扣信息", required = true, dataType = "ThemeParam")
    @PostMapping()
    public JsonResult create(@RequestBody ThemeParam themeParam) {
        themeService.create(themeParam);
        return JsonResult.ok("积分奖扣添加成功");
    }

    @LogAnnotation(module = "修改积分奖扣")
    @ApiOperation(value = "修改积分奖扣")
    @ApiImplicitParam(name = "themeParam", value = "积分奖扣信息", required = true, dataType = "ThemeParam")
    @PutMapping()
    public JsonResult update(@RequestBody ThemeParam themeParam) {
        themeService.update(themeParam);
        return JsonResult.ok("积分奖扣修改成功");
    }

    @LogAnnotation(module = "删除积分奖扣")
    @ApiOperation(value = "删除积分奖扣")
    @ApiImplicitParam(name = "ids", value = "积分奖扣信息ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(@RequestParam(required = true) String ids) {
        themeService.delete(ids, StatusEnum.DELETE.getCode());
        return JsonResult.ok("积分奖扣删除成功");
    }

    @ApiOperation(value = "查询积分奖扣（分页,page/limit不给表示不分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", dataType = "Integer"),
            @ApiImplicitParam(name = "whoFlag", value = "我提交/参与的奖扣标记,1=我提交;2=我参与(默认1)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "themeName", value = "主题关键词", dataType = "String"),
            @ApiImplicitParam(name = "recordDateSt", value = "记录时间开始(时间戳，毫秒)", dataType = "Long"),
            @ApiImplicitParam(name = "recordDateEt", value = "记录时间结束(时间戳，毫秒,默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "themeDateSt", value = "奖扣时间开始(时间戳，毫秒)", dataType = "Long"),
            @ApiImplicitParam(name = "themeDateEt", value = "奖扣时间结束(时间戳，毫秒),默认为当前时间", dataType = "Long"),
            @ApiImplicitParam(name = "attnName", value = "初审人姓名", dataType = "String"),
            @ApiImplicitParam(name = "auditName", value = "终审人姓名", dataType = "String"),
            @ApiImplicitParam(name = "recorderName", value = "记录人姓名", dataType = "String"),
            @ApiImplicitParam(name = "themeStatus", value = "状态：0=草稿;1=保存;2=待初审;3=待终审;4=驳回;5=审核通过;6=锁定", dataType = "Integer")
    })
    @GetMapping("/query")
    public PageResult<Theme> query(Integer page, Integer limit, @RequestParam(defaultValue = "1") String whoFlag, String themeName,
                                   Long recordDateSt, Long recordDateEt, Long themeDateSt, Long themeDateEt,
                                   String attnName, String auditName, String recorderName, Integer themeStatus) {
        if (page != null && limit != null) {
            return themeService.query(new PageQuery(page, limit), StatusEnum.OK.getCode(), whoFlag, themeName,
                    recordDateSt, recordDateEt, themeDateSt, themeDateEt, attnName, auditName, recorderName, themeStatus);
        } else {
            return themeService.query(null, StatusEnum.OK.getCode(), whoFlag, themeName,
                    recordDateSt, recordDateEt, themeDateSt, themeDateEt, attnName, auditName, recorderName, themeStatus);
        }
    }

    @ApiOperation(value = "查询积分奖扣（根据ID查找）", notes = "")
    @ApiImplicitParam(name = "id", value = "积分奖扣信息id", required = true, dataType = "String")
    @GetMapping("/findById")
    public JsonResult findById(@RequestParam(required = true) String id) {
        return JsonResult.ok().put("data", themeService.findById(id));
    }

    @ApiOperation(value = "查询积分奖扣审批流程", notes = "")
    @ApiImplicitParam(name = "id", value = "积分奖扣id", required = true, dataType = "String")
    @GetMapping("/findApprovalInfo")
    public JsonResult findApprovalInfo(@RequestParam() String id) {
        return JsonResult.ok().put("data", themeService.findApprovalInfo(id));
    }

    @LogAnnotation(module = "提交积分奖扣")
    @ApiOperation(value = "提交积分奖扣", notes = "")
    @ApiImplicitParam(name = "id", value = "积分奖扣id", required = true, dataType = "String")
    @GetMapping("/submit")
    public JsonResult submit(@RequestParam(required = true) String id) {
        themeService.submit(id);
        return JsonResult.ok("提交成功");
    }

    @LogAnnotation(module = "撤回积分奖扣")
    @ApiOperation(value = "撤回积分奖扣", notes = "")
    @ApiImplicitParam(name = "id", value = "积分奖扣主题id", required = true, dataType = "String")
    @GetMapping("/retreat")
    public JsonResult retreat(@RequestParam(required = true) String id) {
        themeService.retreat(id);
        return JsonResult.ok("撤回成功");
    }


    @ApiOperation(value = "查询积分奖扣详情（根据主题ID查找事件及参与人员）", notes = "")
    @ApiImplicitParam(name = "themeId", value = "积分奖扣id", required = true, dataType = "String")
    @GetMapping("/themeDetails/findByThemeId")
    public JsonResult findByThemeId(@RequestParam() String themeId) {
        return JsonResult.ok().put("data", themeDetailService.findByThemeId(themeId));
    }
}