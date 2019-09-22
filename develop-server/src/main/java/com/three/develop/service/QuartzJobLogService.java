package com.three.develop.service;

import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonjpa.base.service.BaseService;
import com.three.develop.entity.QuartzJobLog;
import com.three.develop.repository.QuartzJobLogRepository;
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
public class QuartzJobLogService extends BaseService<QuartzJobLog, Long> {

    @Autowired
    private QuartzJobLogRepository quartzJobLogRepository;

    @Transactional
    public void delete(String ids, int code) {
        Set<Long> idSet = StringUtil.getStrToIdSet(ids);
        List<QuartzJobLog> quartzJobLogList = new ArrayList<>();
        for (Long id : idSet) {
            QuartzJobLog quartzJobLog = getEntityById(quartzJobLogRepository, id);
            quartzJobLog.setStatus(code);
            quartzJobLogList.add(quartzJobLog);

        }
        quartzJobLogRepository.saveAll(quartzJobLogList);
    }

    public PageResult<QuartzJobLog> query(PageQuery pageQuery, int code, String searchKey, String searchValue, String startDate, String endDate) {
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        return query(quartzJobLogRepository, pageQuery, sort, code, searchKey, searchValue, startDate, endDate);
    }
}
