package com.three.user.repository;

import com.three.user.entity.Employee;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import com.three.user.entity.Organization;

import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-09-27.
 * Description:
 */
public interface EmployeeRepository extends BaseRepository<Employee, String> {

    List<Employee> findAllByIdIn(Set<String> empIdSet);

    List<Employee> findAllByOrganizationId(String organizationId);

    List<Employee> findAllByStatusAndOrganizationId(int code, String organizationId);

    int countByUsername(String empNum);

    int countByUsernameAndIdNot(String empNum, String id);
}