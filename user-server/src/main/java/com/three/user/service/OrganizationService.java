package com.three.user.service;

import com.three.common.auth.LoginUser;
import com.three.common.auth.SysOrganization;
import com.three.common.constants.RedisConstant;
import com.three.common.enums.AdminEnum;
import com.three.common.enums.StatusEnum;
import com.three.commonclient.exception.BusinessException;
import com.three.commonclient.exception.ParameterException;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import com.three.user.entity.Employee;
import com.three.user.entity.Organization;
import com.three.user.repository.EmployeeRepository;
import com.three.user.repository.OrganizationRepository;
import com.three.user.param.OrganizationParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.user.vo.OrgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by csw on 2019-09-25.
 * Description:
 */

@Service
@Slf4j
public class OrganizationService extends BaseService<Organization, String> {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Transactional
    public void create(OrganizationParam organizationParam) {
        BeanValidator.check(organizationParam);

        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();

        Organization organization = new Organization();
        organization = (Organization) BeanCopyUtil.copyBean(organizationParam, organization);

        if (StringUtil.isBlank(firstOrganizationId) && AdminEnum.YES.getCode() == Objects.requireNonNull(LoginUserUtil.getLoginUser()).getIsAdmin()) { // 超级管理员
            // 只允许创建一级公司
            organization.setParentId("-1");
            organization.setSort(100);
            organization = organizationRepository.save(organization);
            organization.setOrganizationId(organization.getId());
            organizationRepository.save(organization);
        } else { // 用户
            if (StringUtil.isBlank(firstOrganizationId)) {
                throw new ParameterException("用户不属于组织机构，不允许操作");
            }
            if (organizationRepository.countByOrganizationIdAndOrgNameAndStatus(firstOrganizationId, organizationParam.getOrgName(), StatusEnum.OK.getCode()) > 0) {
                throw new ParameterException("已存在同名称[" + organizationParam.getOrgName() + "]组织机构");
            }
            if (organizationRepository.countByOrganizationIdAndOrgCodeAndStatus(firstOrganizationId, organizationParam.getOrgCode(), StatusEnum.OK.getCode()) > 0) {
                throw new ParameterException("中已存在同编号[" + organizationParam.getOrgCode() + "]组织机构");
            }
            organization.setOrganizationId(firstOrganizationId);
            // 如果当前登录用户存在一级组织机构、且传参parentId="-1"，则默认该组织机构在一级组织结构下，即不允许创建一级公司
            if ("-1".equals(organization.getParentId())) {
                organization.setParentId(firstOrganizationId);
            }
            // 拼接父级组织机构层级结构
            Organization parentOrg = setParentOrg(organization);
            // 查找sort最大值，然后加10
            Integer maxSort = organizationRepository.findMaxSortByParentId(parentOrg.getId());
            maxSort = maxSort != null ? maxSort + 10 : 100;
            organization.setSort(maxSort);
            organizationRepository.save(organization);
        }

        addOrganizationRedis(organization);
    }

    private Organization setParentOrg(Organization organization) {
        Organization parentOrg = findById(organization.getParentId());
        organization.setParentName(parentOrg.getOrgName());
        organization.setParentIds(StringUtil.isNotBlank(parentOrg.getParentIds()) ? parentOrg.getParentIds() + "," + parentOrg.getId() : parentOrg.getId());
        return parentOrg;
    }

    @Transactional
    public void update(OrganizationParam organizationParam) {
        BeanValidator.check(organizationParam);

        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();

        Organization organization = getEntityById(organizationRepository, organizationParam.getId());
        organization = (Organization) BeanCopyUtil.copyBean(organizationParam, organization);

        if (StringUtil.isBlank(firstOrganizationId) && AdminEnum.YES.getCode() == Objects.requireNonNull(LoginUserUtil.getLoginUser()).getIsAdmin()) { // 超级管理员
            // 只允许修改代码和名称等
            organizationRepository.save(organization);
        } else { // 用户
            if (StringUtil.isBlank(firstOrganizationId)) {
                throw new ParameterException("用户不属于组织机构，不允许操作");
            }
            if (organizationRepository.countByOrganizationIdAndOrgNameAndStatusAndIdNot(firstOrganizationId, organizationParam.getOrgName(), StatusEnum.OK.getCode(), organizationParam.getId()) > 0) {
                throw new ParameterException("已存在同名称[" + organizationParam.getOrgName() + "]组织机构");
            }
            if (organizationRepository.countByOrganizationIdAndOrgCodeAndStatusAndIdNot(firstOrganizationId, organizationParam.getOrgCode(), StatusEnum.OK.getCode(), organizationParam.getId()) > 0) {
                throw new ParameterException("已存在同编号[" + organizationParam.getOrgCode() + "]组织机构");
            }
            if ("-1".equals(organization.getParentId())) {
                throw new BusinessException("父级组织机构不能是一级公司，数据不合法");
            }
            // 拼接父级组织机构层级结构
            setParentOrg(organization);
            organizationRepository.save(organization);
        }

        updateOrganizationRedis(organization);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Organization> organizationList = new ArrayList<>();
        for (String id : idSet) {
            Organization organization = getEntityById(organizationRepository, String.valueOf(id));
            organization.setStatus(code);
            organizationList.add(organization);
        }

        // 判断组织是否为一级组织，如果是、且有员工，则不能删除
        List<String> orgNameList = new ArrayList<>();
        List<Employee> employeeList1 = new ArrayList<>();
        for (Organization organization : organizationList) {
            List<Employee> employeeList = employeeRepository.findAllByOrganizationId(organization.getId());
            if (employeeList.size() > 0 && organization.getParentId().equals("-1")) {
                orgNameList.add(organization.getOrgName());
            } else { // 将员工的组织绑定到上级组织中
                if (employeeList.size() > 0) {
                    for (Employee employee : employeeList) {
                        employee.setOrganizationId(organization.getParentId());
                    }
                    employeeList1.addAll(employeeList);
                }
            }
        }

        if (orgNameList.size() > 0) {
            throw new BusinessException("一级组织[" + orgNameList.toString() + "]有员工信息，无法删除！");
        }

        if (employeeList1.size() > 0) {
            employeeRepository.saveAll(employeeList1);
        }

        organizationRepository.saveAll(organizationList);

        for (Organization organization : organizationList) {
            updateOrganizationRedis(organization);
        }
    }

    public PageResult<Organization> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Specification<Organization> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            Specification<Organization> codeAndSearchKeySpec = getCodeAndSearchKeySpec(code, searchKey, searchValue);
            Specification<Organization> codeAndSearchKeySpec1 = getCodeAndOrganizationSpec(code, firstOrganizationId);
            predicateList.add(codeAndSearchKeySpec.toPredicate(root, criteriaQuery, criteriaBuilder));
            predicateList.add(codeAndSearchKeySpec1.toPredicate(root, criteriaQuery, criteriaBuilder));
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        };
        if (pageQuery != null) {
            return query(organizationRepository, pageQuery, sort, specification);
        } else {
            return query(organizationRepository, sort, specification);
        }

    }

    public List<OrgVo> findAllWithTree(int code) {
        List<OrgVo> parentOrgVoList = new ArrayList<>();

        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();

        List<Organization> organizationList;
        if (StringUtil.isBlank(firstOrganizationId) && AdminEnum.YES.getCode() == Objects.requireNonNull(LoginUserUtil.getLoginUser()).getIsAdmin()) { // 超级管理员
            organizationList = organizationRepository.findAllByStatus(code);
        } else {
            if (StringUtil.isBlank(firstOrganizationId)) {
                throw new ParameterException("用户不属于组织机构，不允许操作");
            }
            organizationList = organizationRepository.findAllByOrganizationIdAndStatus(firstOrganizationId, code);
        }
        Map<String, OrgVo> orgVoMap = new HashMap<>();
        for (Organization organization : organizationList) {
            OrgVo orgVo = OrgVo.builder().title(organization.getOrgName()).id(organization.getId()).key(organization.getId()).
                    parentId(organization.getParentId()).sort(organization.getSort()).parentName(organization.getParentName()).orgCode(organization.getOrgCode()).build();
            orgVoMap.put(orgVo.getId(), orgVo);
        }
        for (OrgVo orgVo : orgVoMap.values()) {
            OrgVo parentOrg = orgVoMap.get(orgVo.getParentId());
            if (parentOrg != null) {
                parentOrg.getChildren().add(orgVo);
            } else {
                parentOrgVoList.add(orgVo);
            }
        }
        if (StringUtil.isBlank(firstOrganizationId) && AdminEnum.YES.getCode() == Objects.requireNonNull(LoginUserUtil.getLoginUser()).getIsAdmin()) { // 超级管理员
            OrgVo orgVo = OrgVo.builder().title("一级公司").id("-1").key("-1").sort(0).orgCode("FIRST_COMPANY").build();
            parentOrgVoList.add(orgVo);
        }
        // 排序
        sortBySort(parentOrgVoList);
        return parentOrgVoList;
    }

    private void sortBySort(List<OrgVo> orgVoList) {
        orgVoList.sort(Comparator.comparing(OrgVo::getSort));
        for (OrgVo orgVo : orgVoList) {
            if (orgVo.getChildren().size() > 0) {
                sortBySort(orgVo.getChildren());
            }
        }
    }

    public Organization findById(String organizationId) {
        return getEntityById(organizationRepository, organizationId);
    }

    public List<Organization> findChildOrganizationListByOrgId(String orgId) {
        return organizationRepository.findAllByParentIdsLike("%" + orgId + "%");
    }

    @Transactional
    public void move(String id, String id1) {
        Organization organization = findById(id);
        Organization organization1 = findById(id1);
        Integer sort = organization1.getSort();
        organization1.setSort(organization.getSort());
        organization.setSort(sort);
        organizationRepository.save(organization);
        organizationRepository.save(organization1);
    }

    public void updateOrganizationRedis(Organization organization) {
        CompletableFuture.runAsync(() -> {
            SysOrganization sysOrganization = new SysOrganization();
            sysOrganization = (SysOrganization) BeanCopyUtil.copyBean(organization, sysOrganization);
            deleteRedis(sysOrganization);
            addRedis(sysOrganization);
        });
    }

    private void addOrganizationRedis(Organization organization) {
        CompletableFuture.runAsync(() -> {
            SysOrganization sysOrganization = new SysOrganization();
            sysOrganization = (SysOrganization) BeanCopyUtil.copyBean(organization, sysOrganization);
            addRedis(sysOrganization);
        });
    }

    private void deleteOrganizationRedis(Organization organization) {
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