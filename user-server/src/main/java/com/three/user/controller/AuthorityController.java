package com.three.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.three.common.log.LogAnnotation;
import com.three.common.vo.JsonData;
import com.three.user.entity.Authority;
import com.three.user.param.AuthSyncParam;
import com.three.user.param.AuthorityParam;
import com.three.user.service.AuthorityService;
import com.three.common.enums.StatusEnum;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Api(value = "权限管理", tags = "权限管理")
@RestController
@RequestMapping("/sys/authorities")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @LogAnnotation(module = "添加权限")
    @ApiOperation(value = "添加权限")
    @ApiImplicitParam(name = "authorityParam", value = "权限信息", required = true, dataType = "AuthorityParam")
    @PostMapping()
    public JsonResult create(@RequestBody AuthorityParam authorityParam) {
        authorityService.create(authorityParam);
        return JsonResult.ok("添加成功");
    }

    @LogAnnotation(module = "修改权限")
    @ApiOperation(value = "修改权限")
    @ApiImplicitParam(name = "authorityParam", value = "权限信息", required = true, dataType = "AuthorityParam")
    @PutMapping()
    public JsonResult update(@RequestBody AuthorityParam authorityParam) {
        authorityService.update(authorityParam);
        return JsonResult.ok("修改成功");
    }

    @LogAnnotation(module = "删除权限")
    @ApiOperation(value = "删除权限")
    @ApiImplicitParam(name = "id", value = "权限id", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String id) {
        authorityService.delete(id, StatusEnum.DELETE.getCode());
        return JsonResult.ok("删除成功");
    }

    @LogAnnotation(module = "同步权限")
    @ApiOperation(value = "同步权限")
    @ApiImplicitParam(name = "authSyncParam", value = "接口列表", required = true, dataType = "AuthSyncParam")
    @PostMapping("/sync")
    public JsonResult sync(@RequestBody AuthSyncParam authSyncParam) {
        try {
            List<Authority> authorityList = Lists.newArrayList();
            JSONObject jsonObject = JSON.parseObject(authSyncParam.getJson());
            JSONObject paths = jsonObject.getJSONObject("paths");
            Set<String> pathsKeys = paths.keySet();
            for (String pathKey : pathsKeys) {
                JSONObject apiObject = paths.getJSONObject(pathKey);
                Set<String> apiKeys = apiObject.keySet();
                for (String apiKey : apiKeys) {
                    JSONObject methodObject = apiObject.getJSONObject(apiKey);
                    Authority authority = new Authority();
                    authority.setAuthorityUrl(apiKey + ":" + pathKey);
                    authority.setAuthorityName(methodObject.getString("summary"));
                    authority.setParentName(methodObject.getJSONArray("tags").getString(0));
                    authorityList.add(authority);
                }
            }
            authorityService.sync(authorityList);
            return JsonResult.ok("同步成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("同步失败");
        }
    }

    @ApiOperation(value = "查询所有权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String")
    })
    @GetMapping()
    public PageResult<Authority> findAll(String searchKey, String searchValue) {
        return authorityService.findAll(StatusEnum.OK.getCode(), searchKey, searchValue);
    }

    @ApiOperation(value = "查询所有权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "上级权限Id，为空表示查所有权限树", dataType = "String")
    })
    @GetMapping("/findAllAuthTree")
    public JsonData<List<Authority>> findAllAuthTree(String parentId) {
        return new JsonData<>(authorityService.findAllAuthTree(StatusEnum.OK.getCode(), parentId));
    }

    @ApiOperation(value = "查询所有菜单权限(上级权限)")
    @GetMapping("/menuAuth")
    public JsonData<List<Authority>> menuAuth() {
        return new JsonData<>(authorityService.findMenuAuth()).success();
    }

}
