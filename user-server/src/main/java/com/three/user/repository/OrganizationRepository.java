package com.three.user.repository;

import com.three.user.entity.Organization;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.List;

/**
 * Created by csw on 2019-09-25.
 * Description:
 */
public interface OrganizationRepository extends BaseRepository<Organization, String> {

    List<Organization> findAllByParentId(String parentId);

    List<Organization> findAllByFirstParentIdAndStatus(String firstParentId, int code);
}