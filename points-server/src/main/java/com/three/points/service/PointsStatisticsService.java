package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import com.three.common.auth.SysEmployee;
import com.three.common.constants.RedisConstant;
import com.three.common.enums.StatusEnum;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.BusinessException;
import com.three.points.entity.CustomGroupEmp;
import com.three.points.enums.ThemeStatusEnum;
import com.three.points.enums.ThemeTypeEnum;
import com.three.points.feign.UserClient;
import com.three.points.repository.CustomGroupEmpRepository;
import com.three.points.repository.ThemeDetailRepository;
import com.three.points.vo.PointsRankVo;
import com.three.points.vo.PointsStatisticsVo;
import com.three.points.vo.ThemeDetailDailyVo;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PointsStatisticsService {

    @Autowired
    private ThemeDetailRepository themeDetailRepository;

    @Autowired
    private CustomGroupEmpRepository customGroupEmpRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public PageResult<PointsStatisticsVo> themeDetailStatistics(PageQuery pageQuery, int code, String orgId, Long themeDateSt, Long themeDateEt, String searchValue) {
        // 根据orgId和searchValue确定统计人员
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        if (StringUtil.isBlank(orgId)) {
            orgId = firstOrganizationId;
        }
        Date st = DateUtil.beginOfMonth(new Date());
        if (themeDateSt != null) {
            st = DateUtil.beginOfMonth(new Date(themeDateSt));
        }
        Date et = DateUtil.endOfMonth(st);
        if (themeDateEt != null) {
            et = DateUtil.endOfMonth(new Date(themeDateEt));
        }
        // 按人员分组
        Map<String, Map<Date, PointsStatisticsVo>> empStatisticsMap = new HashMap<>();
        // 日常奖扣和管理任务得分信息
        List<ThemeDetailDailyVo> themeDetailDailyVoList = themeDetailRepository.findAllByStatusAndEmpOrgIgAndEmpIdAndThemeStatusAndThemeDateSort(code, orgId, searchValue, ThemeStatusEnum.SUCCESS.getCode(), st, et, new Sort(Sort.Direction.DESC, "createDate"));
        for (ThemeDetailDailyVo themeDetailDailyVo : themeDetailDailyVoList) {
            Date themeMonDate = DateUtil.beginOfMonth(themeDetailDailyVo.getThemeDate());
            if (empStatisticsMap.get(themeDetailDailyVo.getEmpId()) == null) {
                empStatisticsMap.put(themeDetailDailyVo.getEmpId(), new HashMap<>());
                if (empStatisticsMap.get(themeDetailDailyVo.getEmpId()).get(themeMonDate) == null) {
                    PointsStatisticsVo pointsStatisticsVo = new PointsStatisticsVo(themeMonDate, themeDetailDailyVo.getEmpId(), themeDetailDailyVo.getEmpNum(), themeDetailDailyVo.getEmpFullName());
                    empStatisticsMap.get(themeDetailDailyVo.getEmpId()).put(themeMonDate, pointsStatisticsVo);
                }
            }
            PointsStatisticsVo pointsStatisticsVo = empStatisticsMap.get(themeDetailDailyVo.getEmpId()).get(themeMonDate);
            if (ThemeTypeEnum.DAILY_POINTS.getCode() == themeDetailDailyVo.getThemeType()) {
                pointsStatisticsVo.setBscoreAll(pointsStatisticsVo.getBscoreAll() + themeDetailDailyVo.getBscore());
            } else if (ThemeTypeEnum.MANAGER_TASK.getCode() == themeDetailDailyVo.getThemeType()) {
                pointsStatisticsVo.setManagerTaskScore(pointsStatisticsVo.getManagerTaskScore() + themeDetailDailyVo.getBscore());
            }
        }
        List<PointsStatisticsVo> pointsStatisticsVoList = new ArrayList<>();
        for (Map.Entry<String, Map<Date, PointsStatisticsVo>> entry : empStatisticsMap.entrySet()) {
            pointsStatisticsVoList.addAll(entry.getValue().values());
        }
        PageResult<PointsStatisticsVo> pageResult = new PageResult<>();
        if (pageQuery != null) {
            pageResult.setCount(pointsStatisticsVoList.size());
            for (int i = 0; i < pointsStatisticsVoList.size(); i++) {
                if (i >= pageQuery.getPageNo() * pageQuery.getPageSize() && i < (pageQuery.getPageNo() + 1) * pageQuery.getPageSize()) {
                    pageResult.getData().add(pointsStatisticsVoList.get(i));
                }
            }
        } else {
            pageResult.setData(pointsStatisticsVoList);
        }
        return new PageResult<>(pointsStatisticsVoList);
    }

    public PageResult<PointsRankVo> findRankByGroupId(String groupId, Long themeDateSt, Long themeDateEt, String cumulativeFlag) {
        // 根据分组查看分组下的人员
        List<CustomGroupEmp> customGroupEmpList = customGroupEmpRepository.findAllByGroupId(groupId);
        Set<String> empIdSet = new HashSet<>();
        for (CustomGroupEmp customGroupEmp : customGroupEmpList) {
            empIdSet.add(customGroupEmp.getEmpId());
        }
        return findRankByEmpIds(empIdSet, themeDateSt, themeDateEt, cumulativeFlag);
    }

    public PageResult<PointsRankVo> queryOrgRank(String orgId, Long themeDateSt, Long themeDateEt, String containChildFlag, String cumulativeFlag) {
        Set<String> empIdSet = userClient.findSysEmployeeSet(orgId, containChildFlag);
        return findRankByEmpIds(empIdSet, themeDateSt, themeDateEt, cumulativeFlag);
    }

    private PageResult<PointsRankVo> findRankByEmpIds(Set<String> empIdSet, Long themeDateSt, Long themeDateEt, String cumulativeFlag) {
        Date st = DateUtil.beginOfMonth(new Date());
        if (themeDateSt != null) {
            st = DateUtil.beginOfMonth(new Date(themeDateSt));
        }
        Date et = themeDateEt != null ? DateUtil.endOfMonth(new Date(themeDateEt)) : DateUtil.endOfMonth(st);
        if ("1".equals(cumulativeFlag)) {
            st = DateUtil.parse("2020-01-01 00:00:00");
        }
        // 按人员分组
        Map<String, PointsRankVo> empRankMap = new HashMap<>();
        for (String empId : empIdSet) {
            SysEmployee sysEmployee = (SysEmployee) redisTemplate.opsForValue().get(StringUtil.getRedisKey(RedisConstant.EMPLOYEE, empId));
            if (empRankMap.get(empId) == null) {
                PointsRankVo pointsRankVo = new PointsRankVo(empId);
                if (sysEmployee != null) {
                    pointsRankVo.setEmpNum(sysEmployee.getEmpNum());
                    pointsRankVo.setEmpFullName(sysEmployee.getFullName());
                    pointsRankVo.setEmpOrgName(sysEmployee.getOrgName());
                }
                empRankMap.put(empId, pointsRankVo);
            }
            PointsRankVo pointsRankVo = empRankMap.get(empId);
            // 日常奖扣得分信息
            List<ThemeDetailDailyVo> themeDetailDailyVoList = themeDetailRepository.findAllByStatusAndEmpIdAndThemeStatusAndThemeDate(StatusEnum.OK.getCode(), empId, ThemeStatusEnum.SUCCESS.getCode(), st, et);
            for (ThemeDetailDailyVo themeDetailDailyVo : themeDetailDailyVoList) {
                pointsRankVo.setBscoreAll(pointsRankVo.getBscoreAll() + themeDetailDailyVo.getBscore());
            }
        }
        List<PointsRankVo> pointsRankVoList = new ArrayList<>(empRankMap.values());
        pointsRankVoList.sort(Comparator.comparing(PointsRankVo::getBscoreAll).reversed());
        for (int i = 0; i < pointsRankVoList.size(); i++) {
            pointsRankVoList.get(i).setSort(i + 1);
        }
        return new PageResult<>(pointsRankVoList);
    }
}
