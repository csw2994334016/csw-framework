package com.three.develop.repository;

import com.three.commonjpa.base.repository.BaseRepository;
import com.three.develop.entity.QuartzJob;

import java.util.List;

/**
 * Created by csw on 2019/09/13.
 * Description:
 */
public interface QuartzJobRepository extends BaseRepository<QuartzJob, Long> {

    List<QuartzJob> findByJobStatusAndStatus(int code, int status);
}
