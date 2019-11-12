package com.three.points.repository;

import com.three.points.entity.ManagerTaskEmp;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-11-07.
 * Description:
 */
public interface ManagerTaskEmpRepository extends BaseRepository<ManagerTaskEmp, String> {

    ManagerTaskEmp findByOrganizationIdAndEmpIdAndTaskIdNotAndTaskDate(String organizationId, String empId, String taskName, Date taskDate);

    void deleteByTaskId(String id);

    List<ManagerTaskEmp> findAllByOrganizationIdAndTaskIdNotAndTaskDateAndEmpIdIn(String organizationId, String id, Date taskDate, Set<String> empIdSet);

    List<ManagerTaskEmp> findAllByTaskId(String taskId);

    List<ManagerTaskEmp> findAllByOrganizationIdAndTaskDate(String firstOrganizationId, Date taskDate);

    ManagerTaskEmp findAllByOrganizationIdAndTaskDateAndEmpId(String firstOrganizationId, Date taskDate, String empId);

    void deleteAllByTaskId(String id);
}