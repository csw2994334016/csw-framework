package com.three.user.service;

import com.three.common.auth.SysEmployee;
import com.three.common.auth.SysOrganization;
import com.three.common.constants.RedisConstant;
import com.three.common.enums.StatusEnum;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.resource_jpa.autoconfig.SysMqClient;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.user.entity.Authority;
import com.three.user.entity.Employee;
import com.three.user.entity.Organization;
import com.three.user.repository.AuthorityRepository;
import com.three.user.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by csw on 2020/04/18.
 * Description:
 */
@Service
@Slf4j
public class RedisService extends BaseService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SysMqClient sysMqClient;

    // 重新加载组织结构-人员信息缓存
    public void reLoadOrgEmpRedis() {
        String firstOrganizationId = getLoginUserFirstOrganizationId();
        // 查找所有组织机构
        List<Organization> organizationList = organizationService.findChildOrganizationListByOrgId(firstOrganizationId);
        organizationList.add(organizationService.findById(firstOrganizationId));
        for (Organization organization : organizationList) {
            updateOrganizationRedis(organization);
            // 查找所有人员
            List<Employee> employeeList = employeeRepository.findAllByStatusAndOrganizationId(StatusEnum.OK.getCode(), organization.getId());
            employeeList.forEach(this::updateEmployeeRedis);
        }

//        List<Authority> authorityList = authorityService.findAllAuthTree(StatusEnum.OK.getCode(), null);
//        for (Authority authority : authorityList) {
//            if (authority.getChildren().size() >0) {
//                setParentIds(authority, authority.getChildren());
//            }
//        }
//        authorityRepository.saveAll(authorityList);
    }

    private void setParentIds(Authority parent, List<Authority> children) {
        for (Authority authority : children) {
            String parentIds = StringUtil.isNotBlank(parent.getParentIds()) ? parent.getParentIds() + "," + parent.getId() : parent.getId();
            authority.setParentIds(parentIds);
            if (authority.getChildren().size() >0) {
                setParentIds(authority, authority.getChildren());
            }
        }
    }


    // ------以下是人员redis缓存操作------
    public void updateEmployeeRedis(Employee employee) {
        CompletableFuture.runAsync(() -> {
            try {
                SysEmployee sysEmployee = new SysEmployee();
                sysEmployee = (SysEmployee) BeanCopyUtil.copyBean(employee, sysEmployee);
                // 人员信息更新消息
                sysMqClient.sendEmployeeMsg(sysEmployee);
                deleteRedis(sysEmployee);
                addRedis(sysEmployee);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addEmployeeRedis(Employee employee) {
        CompletableFuture.runAsync(() -> {
            SysEmployee sysEmployee = new SysEmployee();
            sysEmployee = (SysEmployee) BeanCopyUtil.copyBean(employee, sysEmployee);
            addRedis(sysEmployee);
        });
    }

    public void deleteEmployeeRedis(Employee employee) {
        CompletableFuture.runAsync(() -> {
            SysEmployee sysEmployee = new SysEmployee();
            sysEmployee = (SysEmployee) BeanCopyUtil.copyBean(employee, sysEmployee);
            deleteRedis(sysEmployee);
        });
    }

    private void addRedis(SysEmployee sysEmployee) {
        try {
            String key = StringUtil.getRedisKey(RedisConstant.EMPLOYEE, sysEmployee.getId());
            redisTemplate.opsForValue().set(key, sysEmployee);
            log.info("从redis中新增人员：{}", sysEmployee.getFullName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteRedis(SysEmployee sysEmployee) {
        try {
            String key = StringUtil.getRedisKey(RedisConstant.EMPLOYEE, sysEmployee.getId());
            // 存在缓存，则删除
            Boolean hasKey = redisTemplate.hasKey(key);
            if (hasKey != null && hasKey) {
                redisTemplate.delete(key);
                log.info("从redis中删除人员：{}", sysEmployee.getFullName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 以下是组织结构redis缓存操作
    public void updateOrganizationRedis(Organization organization) {
        CompletableFuture.runAsync(() -> {
            SysOrganization sysOrganization = new SysOrganization();
            sysOrganization = (SysOrganization) BeanCopyUtil.copyBean(organization, sysOrganization);
            sysMqClient.sendOrganizationMsg(sysOrganization);
            deleteRedis(sysOrganization);
            addRedis(sysOrganization);
        });
    }

    public void addOrganizationRedis(Organization organization) {
        CompletableFuture.runAsync(() -> {
            SysOrganization sysOrganization = new SysOrganization();
            sysOrganization = (SysOrganization) BeanCopyUtil.copyBean(organization, sysOrganization);
            addRedis(sysOrganization);
        });
    }

    public void deleteOrganizationRedis(Organization organization) {
        CompletableFuture.runAsync(() -> {
            SysOrganization sysOrganization = new SysOrganization();
            sysOrganization = (SysOrganization) BeanCopyUtil.copyBean(organization, sysOrganization);
            deleteRedis(sysOrganization);
        });
    }

    private void deleteRedis(SysOrganization sysOrganization) {
        try {
            String key = StringUtil.getRedisKey(RedisConstant.ORGANIZATION, sysOrganization.getId());
            // 存在缓存，则删除
            Boolean hasKey = redisTemplate.hasKey(key);
            if (hasKey != null && hasKey) {
                redisTemplate.delete(key);
                log.info("从redis中删除组织机构：{}", sysOrganization.getOrgName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRedis(SysOrganization sysOrganization) {
        try {
            String key = StringUtil.getRedisKey(RedisConstant.ORGANIZATION, sysOrganization.getId());
            redisTemplate.opsForValue().set(key, sysOrganization);
            log.info("从redis中新组织机构：{}", sysOrganization.getOrgName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
