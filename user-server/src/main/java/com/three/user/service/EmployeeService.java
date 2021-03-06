package com.three.user.service;

import com.google.common.collect.Lists;
import com.three.common.auth.LoginUser;
import com.three.common.enums.AdminEnum;
import com.three.common.exception.BusinessException;
import com.three.common.exception.ParameterException;
import com.three.resource_jpa.jpa.file.entity.FileInfo;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import com.three.user.entity.Employee;
import com.three.user.entity.Organization;
import com.three.user.entity.Role;
import com.three.user.entity.User;
import com.three.user.feign.PointsClient;
import com.three.user.repository.EmployeeRepository;
import com.three.user.param.EmployeeParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.common.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.user.repository.RoleRepository;
import com.three.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Created by csw on 2019-09-27.
 * Description:
 */

@Service
@Slf4j
public class EmployeeService extends BaseService<Employee, String> {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PointsClient pointsClient;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public void create(EmployeeParam employeeParam) {
        BeanValidator.check(employeeParam);

        if (employeeRepository.countByUsername(employeeParam.getEmpNum()) > 0) {
            throw new ParameterException("已经存在相同工号[" + employeeParam.getEmpNum() + "]人员");
        }

        Employee employee = new Employee();
        employee = setEmployeeByParam(employeeParam, employee);

        employee = employeeRepository.save(employee);

        User user = new User();
        user = (User) BeanCopyUtil.copyBean(employeeParam, user);
        user.setUsername(employee.getUsername());
        user.setEmployee(employee);

        String finalSecret = new BCryptPasswordEncoder().encode("123456");
        user.setPassword(finalSecret);

        userRepository.save(user);

        // 新增缓存
        redisService.addEmployeeRedis(employee);
    }

    @Transactional
    public void update(EmployeeParam employeeParam) {
        BeanValidator.check(employeeParam);

        Employee employee = getEntityById(employeeRepository, employeeParam.getId());
        if (employeeRepository.countByUsernameAndIdNot(employeeParam.getEmpNum(), employee.getId()) > 0) {
            throw new ParameterException("已经存在相同工号[" + employeeParam.getEmpNum() + "]人员");
        }
        employee = setEmployeeByParam(employeeParam, employee);

        User user = userService.findByEmployee(employee);

        user.setUsername(employee.getUsername());
        user.setFullName(employeeParam.getFullName());
        user.setCellNum(employeeParam.getCellNum());

        employeeRepository.save(employee);

        // 更新缓存
        redisService.updateEmployeeRedis(employee);
    }

    private Employee setEmployeeByParam(EmployeeParam employeeParam, Employee employee) {
        employee = (Employee) BeanCopyUtil.copyBean(employeeParam, employee);
        employee.setUsername(employee.getEmpNum());

        Organization organization = organizationService.findById(employeeParam.getOrganizationId());
        employee.setOrgName(organization.getOrgName());
        return employee;
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Employee> employeeList = new ArrayList<>();
        for (String id : idSet) {
            Employee employee = getEntityById(employeeRepository, String.valueOf(id));
            employee.setStatus(code);
            employeeList.add(employee);
            User user = userService.findByEmployee(employee);
            user.setStatus(code);
        }

        employeeRepository.saveAll(employeeList);
        // 删除缓存
        employeeList.forEach(employee -> redisService.deleteEmployeeRedis(employee));
    }

    public PageResult<Employee> query(PageQuery pageQuery, int code, String organizationId, String searchValue, String containChildFlag, String taskFilterFlag, String awardPrivilegeFilterFlag) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        String orgId = StringUtil.isBlank(organizationId) ? firstOrganizationId : organizationId;
        if (StringUtil.isBlank(firstOrganizationId) && AdminEnum.YES.getCode() == Objects.requireNonNull(LoginUserUtil.getLoginUser()).getIsAdmin()) { // 超级管理员
            // 超级管理员查找用户，需要选择相应的部门，否则不返回数据
            if (StringUtil.isBlank(orgId)) {
                return new PageResult<>(new ArrayList<>());
            }
        } else {
            if (StringUtil.isBlank(firstOrganizationId)) {
                throw new ParameterException("用户不属于组织机构，不允许操作");
            }
        }
        Specification<Employee> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();

            predicateList.add(criteriaBuilder.equal(root.get("status"), code));

            // 按组织机构查询人员信息
            List<Organization> organizationList = new ArrayList<>();
            if ("0".equals(containChildFlag)) { // 不包含子部门人员
                organizationList.add(organizationService.findById(orgId));
            } else {
                organizationList = organizationService.findChildOrganizationListByOrgId(orgId);
                organizationList.add(organizationService.findById(orgId));
            }
            if (organizationList.size() > 0) {
                Set<String> orgIdSet = new HashSet<>();
                organizationList.forEach(e -> orgIdSet.add(e.getId()));
                predicateList.add(root.get("organizationId").in(orgIdSet));
            }
            Set<String> empIdSet = new HashSet<>();
            // 过滤管理任务已选择的人员
            if ("1".equals(taskFilterFlag)) {
                empIdSet = pointsClient.findCurMonthTaskEmp(firstOrganizationId);
            }
            // 过滤积分奖扣权限已选择的人员
            if ("1".equals(awardPrivilegeFilterFlag)) {
                empIdSet.addAll(pointsClient.findAwardPrivilegeEmp(firstOrganizationId));
            }
            if (empIdSet.size() > 0) {
                predicateList.add(criteriaBuilder.not(root.get("id").in(empIdSet)));
            }
            Predicate predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = Lists.newArrayList();
                Predicate p1 = criteriaBuilder.like(root.get("fullName"), "%" + searchValue + "%");
                Predicate p2 = criteriaBuilder.like(root.get("cellNum"), searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                predicateList1.add(criteriaBuilder.or(p2));
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(new Predicate[0]));
                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (pageQuery != null) {
            return query(employeeRepository, pageQuery, sort, specification);
        } else {
            return query(employeeRepository, sort, specification);
        }
    }

    public PageResult<Employee> queryEmpByOrgId(int code, String orgId, String containChildFlag) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Employee> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();

            predicateList.add(criteriaBuilder.equal(root.get("status"), code));

            // 按组织机构查询人员信息
            if (StringUtil.isNotBlank(orgId)) {
                List<Organization> organizationList = new ArrayList<>();
                organizationList.add(organizationService.findById(orgId));
                if ("1".equals(containChildFlag)) { // 不包含子部门人员
                    organizationList.addAll(organizationService.findChildOrganizationListByOrgId(orgId));
                }
                Set<String> orgIdSet = new HashSet<>();
                if (organizationList.size() > 0) {
                    organizationList.forEach(e -> orgIdSet.add(e.getId()));
                    predicateList.add(root.get("organizationId").in(orgIdSet));
                }
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        return query(employeeRepository, sort, specification);
    }

    @Transactional
    public void assignRole(String id, String roleIds) {
        if (StringUtil.isBlank(roleIds)) {
            throw new ParameterException("角色不可以为空，至少选择一个默认角色");
        }

        Employee employee = getEntityById(employeeRepository, id);
        User user = userService.findByEmployee(employee);

        // 修改角色
        Set<Role> roleSet = getRoleSet(roleIds);
        user.setRoles(roleSet);

        userRepository.save(user);
    }

    @Transactional
    public void updateState(String ids, Integer status) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);

        List<Employee> employeeList = new ArrayList<>();
        for (String id : idSet) {
            Employee employee = getEntityById(employeeRepository, id);
            User user = userService.findByEmployee(employee);
            user.setStatus(status);
            employee.setStatus(status);
        }

        employeeRepository.saveAll(employeeList);

        // 更新缓存
        employeeList.forEach(employee -> redisService.updateEmployeeRedis(employee));
    }

    @Transactional
    public void updatePsw(String oldPsw, String newPsw) {
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            Optional<User> optionalUser = userRepository.findById(loginUser.getId());
            if (!optionalUser.isPresent()) {
                throw new BusinessException("当前登录用户不存在");
            }
            User user = optionalUser.get();
            if (!encoder.matches(oldPsw, user.getPassword())) {
                throw new BusinessException("旧密码不正确");
            }
            user.setPassword(encoder.encode(newPsw));
            userRepository.save(user);
        } else {
            throw new BusinessException("当前用户不存在");
        }
    }

    public PageResult<Employee> findByRole(PageQuery pageQuery, int code, String searchKey, String searchValue, String roleId) {
        // 按角色把用户查出来
        Role role = roleService.getEntityById(roleId);
        Set<String> employeeIdSet = new HashSet<>();
        for (User user : role.getUsers()) {
            if (user.getEmployee() != null) {
                employeeIdSet.add(user.getEmployee().getId());
            }
        }
        if (employeeIdSet.size() > 0) {
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            Specification<Employee> specification = (root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicateList = Lists.newArrayList();
                Specification<Employee> codeAndSearchKeySpec = getCodeAndSearchKeySpec(code, searchKey, searchValue);
                predicateList.add(codeAndSearchKeySpec.toPredicate(root, criteriaQuery, criteriaBuilder));
                predicateList.add(root.<String>get("id").in(employeeIdSet));
                Predicate[] predicates = new Predicate[predicateList.size()];
                return criteriaBuilder.and(predicateList.toArray(predicates));
            };
            return query(employeeRepository, pageQuery, sort, specification);
        } else {
            return new PageResult<>(new ArrayList<>());
        }
    }

    private Set<Role> getRoleSet(String roleIds) {
        Set<String> roleIdSet = StringUtil.getStrToIdSet1(roleIds);
        return new HashSet<>(roleRepository.findAllById(roleIdSet));
    }

    public Employee findById(String id) {
        return getEntityById(employeeRepository, id);
    }

    public List<Employee> findAuditor(String attnOrAuditFlag, String attnId, Integer aPosScoreMax, Integer aNegScoreMin, Integer bPosScoreMax, Integer bNegScoreMin, String themeEmpIds) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Set<String> empIdSet = pointsClient.findAuditor(firstOrganizationId, attnOrAuditFlag, attnId, aPosScoreMax, aNegScoreMin, bPosScoreMax, bNegScoreMin);
        if (empIdSet.size() > 0) {
            if ("1".equals(attnOrAuditFlag) && StringUtil.isNotBlank(themeEmpIds)) {
                Set<String> themeEmpIdSet = StringUtil.getStrToIdSet1(themeEmpIds);
                themeEmpIdSet.forEach(empIdSet::remove);
            }
            return employeeRepository.findAllByIdIn(empIdSet);
        }
        return new ArrayList<>();
    }

    public Set<Role> findRolesById(String id) {
        Employee employee = findById(id);
        User user = userRepository.findByEmployee(employee);
        if (user == null) {
            throw new BusinessException("该员工没有user账号，请联系管理员");
        }
        return user.getRoles();
    }

    @Transactional
    public void updatePicture(Employee employee, FileInfo fileInfo) {
        employee.setPicture(fileInfo.getUrl());
        employeeRepository.save(employee);
        // 更新缓存
        redisService.updateEmployeeRedis(employee);
    }
}