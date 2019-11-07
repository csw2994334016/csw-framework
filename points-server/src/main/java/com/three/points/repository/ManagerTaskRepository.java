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

    int countByTaskNameAndIdNot(String taskName, String id);

    int countByTaskName(String taskName);

    List<ManagerTask> findAllByTaskNameAndTaskDateAfter(String taskName, Date taskDate);
}