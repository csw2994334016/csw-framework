package com.three.points.repository;

import com.three.points.entity.ManagerTaskEmp;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

    @Modifying(clearAutomatically = true)
    @Query("update ManagerTaskEmp t set t.empNum = :empNum, t.empFullName = :fullName, t.empCellName = :cellNum, t.titleLevel = :titleLevel, t.gender = :gender, t.empOrgId = :organizationId, t.empOrgName = :orgName where t.empId = :id")
    void updateEmpInfoByEmpId(String empNum, String fullName, String cellNum, String titleLevel, String gender, String organizationId, String orgName, String id);
}