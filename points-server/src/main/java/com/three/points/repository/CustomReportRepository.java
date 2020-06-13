package com.three.points.repository;

import com.three.points.entity.CustomReport;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2020-04-11.
 * Description:
 */
public interface CustomReportRepository extends BaseRepository<CustomReport, String> {

    int countByOrganizationIdAndReportNameAndStatus(String organizationId, String reportName, int code);

    int countByOrganizationIdAndReportNameAndStatusAndIdNot(String organizationId, String reportName, int code, String id);

    List<CustomReport> findAllByIdIn(Set<String> reportIdSet);
}