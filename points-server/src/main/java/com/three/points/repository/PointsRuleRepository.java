package com.three.points.repository;

import com.three.points.entity.PointsRule;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

/**
 * Created by csw on 2019-11-12.
 * Description:
 */
public interface PointsRuleRepository extends BaseRepository<PointsRule, String> {

    PointsRule findByOrganizationIdAndStatus(String loginUserFirstOrganizationId, int code);
}