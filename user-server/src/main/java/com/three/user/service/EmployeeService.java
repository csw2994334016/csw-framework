package com.three.user.service;

import com.google.common.collect.Lists;
import com.three.commonclient.exception.ParameterException;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import com.three.user.entity.Employee;
import com.three.user.entity.Organization;
import com.three.user.entity.Role;
import com.three.user.entity.User;
import com.three.user.repository.EmployeeRepository;
import com.three.user.param.EmployeeParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.user.repository.RoleRepository;
import com.three.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Created by csw on 2019-09-27.
 * Description:
 */

@Service
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

    @Transactional
    public void create(EmployeeParam employeeParam) {
        BeanValidator.check(employeeParam);

        Employee employee = new Employee();
        employee = (Employee) BeanCopyUtil.copyBean(employeeParam, employee);

        Organization organization = organizationService.getEntityById(employeeParam.getOrganizationId());
        employee.setOrganization(organization);

        employee = employeeRepository.save(employee);

        User user = new User();
        user = (User) BeanCopyUtil.copyBean(employeeParam, user);
        user.setEmployee(employee);

        String finalSecret = new BCryptPasswordEncoder().encode("123456");
        user.setPassword(finalSecret);

        userRepository.save(user);
    }

    @Transactional
    public void update(EmployeeParam employeeParam) {
        BeanValidator.check(employeeParam);

        Employee employee = getEntityById(employeeRepository, employeeParam.getId());
        employee = (Employee) BeanCopyUtil.copyBean(employeeParam, employee);

        Organization organization = organizationService.getEntityById(employeeParam.getOrganizationId());
        employee.setOrganization(organization);

        employee.getUser().setUsername(employeeParam.getUsername());
        employee.getUser().setFullName(employeeParam.getFullName());
        employee.getUser().setCellNum(employeeParam.getCellNum());

        employeeRepository.save(employee);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Employee> employeeList = new ArrayList<>();
        for (String id : idSet) {
            Employee employee = getEntityById(employeeRepository, String.valueOf(id));
            employee.setStatus(code);
            employee.getUser().setStatus(code);
            employeeList.add(employee);
        }

        employeeRepository.saveAll(employeeList);
    }

    public PageResult<Employee> query(PageQuery pageQuery, int code, String organizationId, String searchValue, String containChildFlag) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Employee> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();

            predicateList.add(criteriaBuilder.equal(root.get("status"), code));

            // 按组织机构查询人员信息
            List<Organization> organizationList = new ArrayList<>();
            if (StringUtil.isNotBlank(organizationId)) {
                organizationList = organizationService.getChildOrganizationListByOrgId(organizationId);
            } else {
                String firstParentId = LoginUserUtil.getLoginUserFirstOrganizationId();
                if (firstParentId != null) {
                    organizationList = organizationService.getChildOrganizationListByOrgId(firstParentId);
                }
            }
            if ("0".equals(containChildFlag)) { // 不包含子部门人员
                Organization organization = null;
                if (StringUtil.isNotBlank(organizationId)) {
                    organization = organizationService.getEntityById(organizationId);
                } else {
                    String firstParentId = LoginUserUtil.getLoginUserFirstOrganizationId();
                    if (firstParentId != null) {
                        organization = organizationService.getEntityById(firstParentId);
                    }
                }
                if (organization != null) {
                    organizationList.clear();
                    organizationList.add(organization);
                }
            }
            if (organizationList.size() > 0) {
                CriteriaBuilder.In<Organization> in = criteriaBuilder.in(root.get("organization"));
                organizationList.forEach(in::value);
                predicateList.add(in);
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            Predicate predicate = criteriaBuilder.and(predicateList.toArray(predicates));

            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = Lists.newArrayList();
                Predicate p1 = criteriaBuilder.like(root.get("fullName"), "%" + searchValue + "%");
                Predicate p2 = criteriaBuilder.like(root.get("cellNum"), searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                predicateList1.add(criteriaBuilder.or(p2));
                Predicate[] predicates1 = new Predicate[predicateList1.size()];
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(predicates1));

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

    @Transactional
    public void assignRole(EmployeeParam employeeParam) {
        if (StringUtil.isBlank(employeeParam.getRoleIds())) {
            throw new ParameterException("角色不可以为空，至少选择一个默认角色");
        }

        Employee employee = getEntityById(employeeRepository, employeeParam.getId());
        User user = employee.getUser();

        // 修改角色
        Set<Role> roleSet = getRoleSet(employeeParam.getRoleIds());
        user.setRoles(roleSet);

        userRepository.save(user);
    }

    @Transactional
    public void updateState(String ids, Integer status) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);

        List<Employee> employeeList = new ArrayList<>();
        for (String id : idSet) {
            Employee employee = getEntityById(employeeRepository, id);
            employee.getUser().setStatus(status);
            employee.setStatus(status);
        }

        employeeRepository.saveAll(employeeList);
    }

    public void updatePsw(String finalSecret, String newPsw) {

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
    }

    private Set<Role> getRoleSet(String roleIds) {
        Set<String> roleIdSet = StringUtil.getStrToIdSet1(roleIds);
        return new HashSet<>(roleRepository.findAllById(roleIdSet));
    }

    public List<Employee> findAllByOrgId(String orgId) {
        Organization organization = organizationService.getEntityById(orgId);
        return employeeRepository.findAllByOrganization(organization);
    }

    public Employee findById(String id) {
        return getEntityById(employeeRepository, id);
    }
}