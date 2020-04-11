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


    List<Organization> findAllByOrganizationIdAndStatus(String firstParentId, int code);

    List<Organization> findAllByParentIdsLike(String orgId);

    int countByOrganizationIdAndOrgNameAndStatusAndIdNot(String firstParentId, String orgName, int code, String id);

    int countByOrganizationIdAndOrgCodeAndStatusAndIdNot(String firstParentId, String orgCode, int code, String id);

    int countByOrganizationIdAndOrgNameAndStatus(String firstParentId, String orgName, int code);

    int countByOrganizationIdAndOrgCodeAndStatus(String firstParentId, String orgCode, int code);

    @Query("select max(o.sort) from Organization o where o.parentId = :parentId")
    Integer findMaxSortByParentId(String parentId);
}