package com.three.user.repository;

import com.three.user.entity.Employee;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import com.three.user.entity.Organization;

import java.util.List;

/**
 * Created by csw on 2019-09-27.
 * Description:
 */
public interface EmployeeRepository extends BaseRepository<Employee, String> {

    List<Employee> findAllByOrganization(Organization organization);
}