package com.three.points.repository;

import com.three.points.entity.AwardPrivilegeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by csw on 2019-10-13.
 * Description:
 */
public interface AwardPrivilegeEmployeeRepository extends JpaRepository<AwardPrivilegeEmployee, String> {

    void deleteByAwardPrivilegeId(String id);
}