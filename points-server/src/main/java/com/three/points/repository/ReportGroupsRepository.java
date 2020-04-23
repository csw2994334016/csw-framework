package com.three.points.repository;

import com.three.points.entity.ReportGroups;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

/**
 * Created by csw on 2020-01-11.
 * Description:
 */
public interface ReportGroupsRepository extends BaseRepository<ReportGroups, String> {

    int countByReportGroupsNameAndOrganizationId(String reportGroupsName, String firstOrganizationId);

    int countByReportGroupsNameAndOrganizationIdAndIdNot(String reportGroupsName, String firstOrganizationId, String id);
}