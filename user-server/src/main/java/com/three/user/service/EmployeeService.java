package com.three.user.service;

import com.three.user.entity.Employee;
import com.three.user.repository.EmployeeRepository;
import com.three.user.param.EmployeeParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-09-27.
 * Description:
 */

@Service
public class EmployeeService extends BaseService<Employee,  String> {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void create(EmployeeParam employeeParam) {
        BeanValidator.check(employeeParam);

        Employee employee = new Employee();
        employee = (Employee) BeanCopyUtil.copyBean(employeeParam, employee);

        employeeRepository.save(employee);
    }

    @Transactional
    public void update(EmployeeParam employeeParam) {
        BeanValidator.check(employeeParam);

        Employee employee = getEntityById(employeeRepository, employeeParam.getId());
        employee = (Employee) BeanCopyUtil.copyBean(employeeParam, employee);

        employeeRepository.save(employee);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Employee> employeeList = new ArrayList<>();
        for (String id : idSet) {
            Employee employee = getEntityById(employeeRepository, String.valueOf(id));
            employee.setStatus(code);
            employeeList.add(employee);
        }

        employeeRepository.saveAll(employeeList);
    }

    public PageResult<Employee> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return query(employeeRepository, pageQuery, sort, code, searchKey, searchValue);
    }

}