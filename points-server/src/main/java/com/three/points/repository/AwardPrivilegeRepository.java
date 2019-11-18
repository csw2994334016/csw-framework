package com.three.points.repository;

import com.three.points.entity.AwardPrivilege;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */
public interface AwardPrivilegeRepository extends BaseRepository<AwardPrivilege, String> {

    @Query("select distinct t.id from AwardPrivilege t where t.organizationId = :firstOrganizationId and t.status = :code")
    Set<String> findAllByOrganizationIdAndStatus(String firstOrganizationId, int code);

    @Query("select distinct t.id from AwardPrivilege t where t.ascore >= :aScore and t.bscore >= :bScore and t.organizationId = :firstOrganizationId and t.status = :code")
    Set<String> findAllByAScoreGreaterThanEqualAndBScoreGreaterThanEqualAndOrganizationIdAndStatus(int aScore, int bScore, String firstOrganizationId, int code);
}