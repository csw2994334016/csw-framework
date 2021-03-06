package com.three.points.repository;

import com.three.points.entity.ManagerTask;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2019-11-06.
 * Description:
 */
public interface ManagerTaskRepository extends BaseRepository<ManagerTask, String> {

    int countByTaskNameAndOrganizationId(String taskName, String organizationId);

    List<ManagerTask> findAllByTaskNameAndOrganizationIdAndTaskDateAfter(String taskName, String organizationId, Date taskDate);

    int countByTaskNameAndOrganizationIdAndTaskDate(String taskName, String organizationId, Date taskDate);

    ManagerTask findByOrganizationIdAndTaskNameAndTaskDate(String firstOrganizationId, String taskName, Date taskDate);

    int countByTaskNameAndOrganizationIdAndTaskDateAndIdNot(String taskName, String organizationId, Date taskDate, String id);

    public List<ManagerTask> findAllByStatusAndOrganizationId(int code, String organizationId);

    List<ManagerTask> findAllByStatusAndOrganizationIdAndTaskDate(int code, String firstOrganizationId, Date taskDate);

    List<ManagerTask> findAllByStatusAndTaskDate(int code, Date taskDate);
}