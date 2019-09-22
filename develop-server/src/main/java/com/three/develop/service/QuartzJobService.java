package com.three.develop.service;

import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.ParameterException;
import com.three.commonclient.utils.BeanValidator;
import com.three.commonjpa.base.service.BaseService;
import com.three.develop.entity.QuartzJob;
import com.three.develop.enums.JobStatusEnum;
import com.three.develop.param.QuartzJobParam;
import com.three.develop.repository.QuartzJobRepository;
import com.three.develop.utils.QuartzManager;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019/09/13.
 * Description:
 */
@Service
public class QuartzJobService extends BaseService<QuartzJob, Long> {

    @Autowired
    private QuartzJobRepository quartzJobRepository;

    @Autowired
    private QuartzManager quartzManager;

    @Transactional
    public void create(QuartzJobParam quartzJobParam) {
        BeanValidator.check(quartzJobParam);
        if (!CronExpression.isValidExpression(quartzJobParam.getCronExpression())) {
            throw new ParameterException("cron表达式格式错误");
        }

        QuartzJob quartzJob = new QuartzJob();
        quartzJob = (QuartzJob) BeanCopyUtil.copyBean(quartzJobParam, quartzJob);

        quartzJob = quartzJobRepository.save(quartzJob);

        quartzManager.addJob(quartzJob);
    }

    @Transactional
    public void update(QuartzJobParam quartzJobParam) {
        BeanValidator.check(quartzJobParam);
        if (!CronExpression.isValidExpression(quartzJobParam.getCronExpression())) {
            throw new ParameterException("cron表达式格式错误");
        }

        QuartzJob quartzJob = getEntityById(quartzJobRepository, quartzJobParam.getId());
        quartzJob = (QuartzJob) BeanCopyUtil.copyBean(quartzJobParam, quartzJob);

        quartzJob = quartzJobRepository.save(quartzJob);

        quartzManager.updateJobCron(quartzJob);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<Long> idSet = StringUtil.getStrToIdSet(ids);
        List<QuartzJob> quartzJobList = new ArrayList<>();
        for (Long id : idSet) {
            QuartzJob quartzJob = getEntityById(quartzJobRepository, id);
            quartzJob.setStatus(code);
            quartzJobList.add(quartzJob);
            quartzManager.deleteJob(quartzJob);
        }
        quartzJobRepository.saveAll(quartzJobList);
    }

    public PageResult<QuartzJob> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        return query(quartzJobRepository, pageQuery, sort, code, searchKey, searchValue);
    }

    @Transactional
    public void updateIsPause(QuartzJob quartzJob) {
        if (quartzJob.getJobStatus() == JobStatusEnum.PAUSE.getCode()) {
            quartzManager.resumeJob(quartzJob);
            quartzJob.setJobStatus(JobStatusEnum.RUNNING.getCode());
        } else {
            quartzManager.pauseJob(quartzJob);
            quartzJob.setJobStatus(JobStatusEnum.PAUSE.getCode());
        }
//        QuartzJob quartzJob1 = quartzJobRepository.findById(1L).get();
        quartzJobRepository.save(quartzJob);
    }

    public void updateJobStatus(String ids, Integer status) {
        Set<Long> idSet = StringUtil.getStrToIdSet(ids);
        for (Long id : idSet) {
            QuartzJob quartzJob = getEntityById(quartzJobRepository, id);
            updateIsPause(quartzJob);
        }
    }

    public void executeJob(String ids) {
        Set<Long> idSet = StringUtil.getStrToIdSet(ids);
        for (Long id : idSet) {
            QuartzJob quartzJob = getEntityById(quartzJobRepository, id);
            quartzManager.runAJobNow(quartzJob);
        }
    }
}
