package com.three.points.repository;

import com.three.points.entity.PointsRuleEmpCount;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by csw on 2019-11-12.
 * Description:
 */
public interface PointsRuleEmpCountRepository extends JpaRepository<PointsRuleEmpCount, String> {

    void deleteByPointsRuleId(String pointsRuleId);

    List<PointsRuleEmpCount> findAllByPointsRuleId(String pointsRuleId);
}