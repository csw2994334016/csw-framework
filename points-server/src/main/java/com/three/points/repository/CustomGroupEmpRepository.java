package com.three.points.repository;

import com.three.points.entity.CustomGroupEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by csw on 2020/04/11.
 * Description:
 */
public interface CustomGroupEmpRepository extends JpaRepository<CustomGroupEmp, String> {

    void deleteByGroupId(String id);

    List<CustomGroupEmp> findAllByGroupId(String customGroupId);

    List<CustomGroupEmp> findAllByEmpId(String empId);

    @Modifying(clearAutomatically = true)
    @Query("update CustomGroupEmp t set t.empNum = :empNum, t.empFullName = :fullName, t.empPicture = :picture, t.empOrgId = :organizationId, t.empOrgName = :orgName where t.empId = :id")
    void updateEmpInfoByEmpId(String empNum, String fullName, String picture, String organizationId, String orgName, String id);

    @Modifying(clearAutomatically = true)
    @Query("update CustomGroupEmp t set t.empOrgName = :orgName where t.empOrgId = :id")
    void updateOrgInfoByEmpId(String orgName, String id);
}
