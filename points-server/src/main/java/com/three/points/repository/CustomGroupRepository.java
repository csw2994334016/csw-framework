package com.three.points.repository;

import com.three.points.entity.CustomGroup;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

/**
 * Created by csw on 2020-04-06.
 * Description:
 */
public interface CustomGroupRepository extends BaseRepository<CustomGroup, String> {

    int countByOrganizationIdAndGroupNameAndStatusAndIdNot(String firstOrganizationId, String groupName, int code, String id);

    int countByOrganizationIdAndGroupNameAndStatus(String firstOrganizationId, String groupName, int code);
}