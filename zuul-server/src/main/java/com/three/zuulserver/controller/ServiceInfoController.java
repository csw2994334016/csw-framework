package com.three.zuulserver.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.three.common.constants.ServiceConstant;
import com.three.common.vo.PageResult;
import com.three.zuulserver.vo.ServiceInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/serviceInfos")
public class ServiceInfoController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EurekaClient eurekaClient;

    /**
     * 获取各个服务的信息
     *
     * @return
     */
    @GetMapping
    public Map<String, Object> map() {
        Map<String, Object> map = new HashMap<>();
        List<String> services = discoveryClient.getServices();
        services.forEach(serviceId -> {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            map.put(serviceId, instances);
        });

        return map;
    }

    @ApiOperation(value = "查询角色（分页）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段(船名)", dataType = "String"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String")
    })
    @PostMapping("/getServiceInfo")
    public PageResult<ServiceInfo> getServiceInfo(Integer page, Integer limit, String searchKey, String searchValue) {
        List<ServiceInfo> serviceInfoList = new ArrayList<>();
        List<String> services = discoveryClient.getServices();
        services.forEach(serviceId -> {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            if (instances.size() > 0) {
                ServiceInstance serviceInstance = instances.get(0);
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServerId(serviceInstance.getServiceId());
                serviceInfo.setHost(serviceInstance.getHost());
                serviceInfo.setPort(serviceInstance.getPort());
                List<InstanceInfo> instanceInfos = eurekaClient.getInstancesByVipAddress(serviceId, false);
                if (instanceInfos.size() > 0) {
                    serviceInfo.setInstanceId(instanceInfos.get(0).getInstanceId());
                }
                serviceInfo.setZuulPath(ServiceConstant.API_PRE + serviceId);
                serviceInfoList.add(serviceInfo);
            }
        });

        return new PageResult<>(serviceInfoList);
    }
}
