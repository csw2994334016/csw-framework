package com.three.points.repository;

import com.three.points.entity.AwardPrivilegeEmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-10-13.
 * Description:
 */
public interface AwardPrivilegeEmpRepository extends JpaRepository<AwardPrivilegeEmp, String> {

    void deleteByAwardPrivilegeId(String id);

    List<AwardPrivilegeEmp> findAllByAwardPrivilegeIdInAndEmpIdNot(Set<String> awardPrivilegeIdSet, String attnId);

    List<AwardPrivilegeEmp> findAllByAwardPrivilegeIdIn(Set<String> awardPrivilegeIdSet);
}