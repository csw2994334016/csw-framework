package com.three.authserver.controller;

import com.three.authserver.param.ChangePwdParam;
import com.three.authserver.service.RedisClientDetailsService;
import com.three.common.constants.SystemClientConstant;
import com.three.common.log.LogAnnotation;
import com.three.common.utils.StringUtil;
import com.three.common.vo.JsonResult;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.ParameterException;
import com.three.commonclient.utils.BeanValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * client管理功能
 * 2018.07.10 添加
 */
@Slf4j
@Api(value = "客户端管理", tags = "客户端管理")
@RestController
@RequestMapping("auth/clients")
public class ClientController {

    @Autowired
    private RedisClientDetailsService clientDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @LogAnnotation(module = "添加客户端")
    @ApiOperation(value = "添加客户端", notes = "")
    @ApiImplicitParam(name = "BaseClientDetails", value = "客户端信息", required = true, dataType = "clientDetails")
    @PostMapping
    public JsonResult save(@RequestBody BaseClientDetails clientDetails) {
        ClientDetails client = getAndCheckClient(clientDetails.getClientId(), false);
        if (client != null) {
            throw new ParameterException(clientDetails.getClientId() + "已存在");
        }

        // 密码加密
        clientDetails.setClientSecret(passwordEncoder.encode("123456"));

        clientDetailsService.addClientDetails(clientDetails);
        log.info("添加客户端信息：{}", clientDetails);
        return JsonResult.ok("添加客户端成功");
    }

    @LogAnnotation(module = "修改客户端")
    @ApiOperation(value = "修改客户端")
    @ApiImplicitParam(name = "BaseClientDetails", value = "客户端信息", required = true, dataType = "clientDetails")
    @PutMapping
    public JsonResult update(@RequestBody BaseClientDetails clientDetails) {
        getAndCheckClient(clientDetails.getClientId(), true);
        clientDetailsService.updateClientDetails(clientDetails);
        log.info("修改客户端信息：{}", clientDetails);
        return JsonResult.ok("修改客户端成功");
    }

    @LogAnnotation(module = "修改客户端密码")
    @ApiOperation(value = "修改客户端密码")
    @ApiImplicitParam(name = "ChangePwdParam", value = "密码信息", required = true, dataType = "changePwdParam")
    @PutMapping("/updateSecret")
    public JsonResult updateSecret(ChangePwdParam changePwdParam) {
        BeanValidator.check(changePwdParam);

        ClientDetails clientDetails = getAndCheckClient(changePwdParam.getClientId(), true);
        checkSystemClient(changePwdParam.getClientId());

        // 新密码与确认密码是否一致
        if (!StringUtils.equals(changePwdParam.getNewPwd(), changePwdParam.getRePwd())) {
            throw new ParameterException("新密码与确认密码不一致");
        }
        // 原始密码是否正确
        if (!clientDetails.getClientSecret().equals(passwordEncoder.encode(changePwdParam.getOldPwd()))) {
            throw new ParameterException("原始密码不正确");
        }

        String secret = passwordEncoder.encode(changePwdParam.getNewPwd());
        clientDetailsService.updateClientSecret(changePwdParam.getClientId(), secret);
        log.info("修改客户端密码：{},{}", changePwdParam.getClientId(), secret);
        return JsonResult.ok("修改客户端密码成功");
    }

    @ApiOperation(value = "查询所有客户端", notes = "")
    @GetMapping
    public PageResult<ClientDetails> findClients() {
        List<ClientDetails> clientDetails = clientDetailsService.listClientDetails();
        clientDetails.parallelStream().forEach(this::isSystemClient);
        return new PageResult<>(clientDetails.size(), clientDetails);
    }

    @ApiOperation(value = "查询客户端（按clientId）", notes = "")
    @ApiImplicitParam(name = "clientId", value = "客户端Id", required = true, dataType = "String")
    @GetMapping("/getByClientId")
    public ClientDetails getById(String clientId) {
        return getAndCheckClient(clientId, true);
    }

    @LogAnnotation(module = "删除客户端")
    @ApiOperation(value = "删除客户端")
    @ApiImplicitParam(name = "clientIds", value = "客户端ids", required = true, dataType = "String")
    @DeleteMapping()
    public JsonResult delete(String clientIds) {
        Set<String> clientIdSet = StringUtil.getStrToIdSet1(clientIds);

        for (String clientId : clientIdSet) {
            getAndCheckClient(clientId, true);
            checkSystemClient(clientId);

            clientDetailsService.removeClientDetails(clientId);
            log.info("删除客户端：{}", clientId);
        }
        return JsonResult.ok("删除客户端成功");
    }

    /**
     * 根据id获取client信息
     *
     * @param clientId
     * @param check    是否校验存在性
     * @return
     */
    private ClientDetails getAndCheckClient(String clientId, boolean check) {
        ClientDetails clientDetails = null;
        try {
            clientDetails = clientDetailsService.loadClientByClientId(clientId);
            isSystemClient(clientDetails);
        } catch (NoSuchClientException e) {
            if (check) {
                throw new ParameterException(clientId + "不存在");
            }
        }

        return clientDetails;
    }

    private void checkSystemClient(String clientId) {
        if (SystemClientConstant.CLIENT_ID.equals(clientId)) {
            throw new ParameterException("不能操作系统数据");
        }
    }

    /**
     * 判断是否是我们自己系统内部用的client<br>
     * 在扩展字段里放一个isSystem标注一下
     *
     * @param clientDetails
     * @see SystemClientConstant
     */
    private boolean isSystemClient(ClientDetails clientDetails) {
        BaseClientDetails baseClientDetails = (BaseClientDetails) clientDetails;
        Map<String, Object> additionalInformation = baseClientDetails.getAdditionalInformation();
        if (additionalInformation == null) {
            additionalInformation = new HashMap<>();
            baseClientDetails.setAdditionalInformation(additionalInformation);
        }

        boolean isSystem = SystemClientConstant.CLIENT_ID.equalsIgnoreCase(baseClientDetails.getClientId());
        baseClientDetails.addAdditionalInformation("isSystem", isSystem);

        return isSystem;
    }

}
