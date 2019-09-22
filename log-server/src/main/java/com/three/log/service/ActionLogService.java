package com.three.log.service;

import com.three.common.enums.LogEnum;
import com.three.common.log.Log;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonjpa.base.service.BaseService;
import com.three.log.entity.ActionLog;
import com.three.log.repository.ActionLogRepository;
import com.three.resource_security.utils.LoginUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019/03/31.
 * Description:
 */
@Service
public class ActionLogService extends BaseService<ActionLog, Long> {

    @Autowired
    private ActionLogRepository actionLogRepository;

    public PageResult<ActionLog> query(PageQuery pageQuery, int code, String searchKey, String searchValue, String startDate, String endDate) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return query(actionLogRepository, pageQuery, sort, code, searchKey, searchValue, startDate, endDate);
    }

    @Transactional
    public void saveLog(Log log) {

        ActionLog actionLog = new ActionLog();
        if (log.getUsername() == null) {
            log.setUsername(LoginUserUtil.getLoginUsername());
        }
        actionLog.setUsername(log.getUsername());
        actionLog.setLogType(log.getFlag() ? LogEnum.INFO.getCode() : LogEnum.ERROR.getCode());
        actionLog.setMessage(log.getMessage());
        actionLog.setTime(log.getTime());
        actionLog.setDescription(log.getModule());
        actionLog.setMethod(log.getMethod());
        actionLog.setParams(log.getParams());
        actionLog.setIpAddress(log.getIpAddress());
        actionLog.setOsName(log.getOsName());
        actionLog.setDevice(log.getDevice());
        actionLog.setBrowserType(log.getBrowserType());

        actionLogRepository.save(actionLog);
    }

    @Transactional
    public void delete(String ids) {
        String[] roleIdArray = StringUtils.split(ids, ",");
        Set<Long> idSet = StringUtil.getStringArrayToIdSet(roleIdArray);

        List<ActionLog> actionLogList = actionLogRepository.findAllById(idSet);

        actionLogRepository.deleteAll(actionLogList);
    }
}
