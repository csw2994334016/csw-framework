package com.three.user.repository;

import com.three.user.entity.Organization;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by csw on 2019-09-25.
 * Description:
 */
public interface OrganizationRepository extends BaseRepository<Organization, String> {

    List<Organization> findAllByParentId(String parentId);

    List<Organization> findAllByFirstParentIdAndStatus(String firstParentId, int code);

    List<Organization> findAllByParentIdsLike(String orgId);

    int countByFirstParentIdAndOrgNameAndIdNot(String firstParentId, String orgName, String id);

    int countByFirstParentIdAndOrgCodeAndIdNot(String firstParentId, String orgCode, String id);

    int countByFirstParentIdAndOrgName(String firstParentId, String orgName);

    int countByFirstParentIdAndOrgCode(String firstParentId, String orgCode);

    @Query("select max(o.sort) from Organization o where o.parentId = :parentId")
    Integer findMaxSortByParentId(String parentId);
}