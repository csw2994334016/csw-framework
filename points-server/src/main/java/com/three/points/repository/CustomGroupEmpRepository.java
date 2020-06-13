package com.three.points.repository;

import com.three.points.entity.CustomGroupEmp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by csw on 2020/04/11.
 * Description:
 */
public interface CustomGroupEmpRepository extends JpaRepository<CustomGroupEmp, String> {

    void deleteByGroupId(String id);

    List<CustomGroupEmp> findAllByGroupId(String customGroupId);

    List<CustomGroupEmp> findAllByEmpId(String empId);
}
