package com.three.user.controller;

import com.three.common.log.LogAnnotation;
import com.three.user.entity.User;
import com.three.user.param.UserParam;
import com.three.user.service.UserService;
import com.three.common.enums.StatusEnum;
import com.three.commonclient.utils.BeanValidator;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Api(value = "用户管理", tags = "用户管理")
@RestController
@RequestMapping("/sys/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 这里参数过多，并且参数含有中文，建议用post请求，用restful风格解决不了需求时，建议不要强行使用restful
     * 加了一个/query是避免跟添加用户接口冲突
     */
    @ApiOperation(value = "查询用户（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段(船名)", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String")
    })
    @PostMapping("/query")
    public PageResult<User> query(Integer page, Integer limit, String searchKey, String searchValue) {
        PageQuery pageQuery = new PageQuery(page, limit);
        BeanValidator.check(pageQuery);
        return userService.query(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue);
    }

    @LogAnnotation(module = "添加用户")
    @ApiOperation(value = "添加用户", notes = "")
    @ApiImplicitParam(name = "userParam", value = "用户信息", required = true, dataType = "UserParam")
    @PostMapping()
    public JsonResult create(@RequestBody UserParam userParam) {
        userService.create(userParam);
        return JsonResult.ok("添加成功");
    }

    @LogAnnotation(module = "修改用户")
    @ApiOperation(value = "修改用户", notes = "")
    @ApiImplicitParam(name = "userParam", value = "用户信息", required = true, dataType = "UserParam")
    @PutMapping()
    public JsonResult update(@RequestBody UserParam userParam) {
        userService.update(userParam);
        return JsonResult.ok("修改成功");
    }

    @LogAnnotation(module = "分配角色")
    @ApiOperation(value = "分配角色", notes = "")
    @ApiImplicitParam(name = "userParam", value = "用户信息", required = true, dataType = "UserParam")
    @PutMapping("/assignRole")
    public JsonResult assignRole(@RequestBody UserParam userParam) {
        userService.assignRole(userParam);
        return JsonResult.ok("修改成功");
    }

    @LogAnnotation(module = "修改用户状态")
    @ApiOperation(value = "修改用户状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "用户ids", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态：1正常，2冻结，3删除", required = true, dataType = "Integer")
    })
    @PutMapping("/status")
    public JsonResult updateState(String ids, Integer status) {
        userService.updateState(ids, status);
        return JsonResult.ok();
    }

    @LogAnnotation(module = "修改密码")
    @ApiOperation(value = "修改密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPsw", value = "原密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPsw", value = "新密码", required = true, dataType = "String")
    })
    @PutMapping("/psw")
    public JsonResult updatePsw(String oldPsw, String newPsw) {
        String finalSecret = new BCryptPasswordEncoder().encode(oldPsw);
        userService.updatePsw(finalSecret, newPsw);
        return JsonResult.ok("密码修改成功");
    }

    @ApiOperation(value = "查找所有用户(按角色)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段(账号、用户名、手机号)", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String")
    })
    @PostMapping("/findByRole")
    public PageResult<User> queryByRole(Integer page, Integer limit, String roleId, String searchKey, String searchValue) {
        PageQuery pageQuery = new PageQuery(page, limit);
        return userService.findByRole(pageQuery, StatusEnum.OK.getCode(), searchKey, searchValue, roleId);
    }

}
