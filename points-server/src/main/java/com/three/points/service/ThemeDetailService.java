package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.three.common.auth.SysEmployee;
import com.three.common.constants.RedisConstant;
import com.three.common.enums.StatusEnum;
import com.three.commonclient.exception.BusinessException;
import com.three.points.entity.Event;
import com.three.points.entity.ThemeDetail;
import com.three.points.enums.ThemeStatusEnum;
import com.three.points.param.ThemeEmpParam;
import com.three.points.repository.ThemeDetailRepository;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.repository.ThemeRepository;
import com.three.points.vo.*;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */

@Service
public class ThemeDetailService extends BaseService<ThemeDetail, String> {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeDetailRepository themeDetailRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<ThemeDetail> themeDetailList = new ArrayList<>();
        for (String id : idSet) {
            ThemeDetail themeDetail = getEntityById(themeDetailRepository, String.valueOf(id));
            themeDetail.setStatus(code);
            themeDetailList.add(themeDetail);
        }

        themeDetailRepository.saveAll(themeDetailList);
    }

    public PageResult<ThemeDetail> query(PageQuery pageQuery, int code, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<ThemeDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
            String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
            Specification<ThemeDetail> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            Predicate predicate = codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder);
            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = new ArrayList<>();
                Predicate p1 = criteriaBuilder.like(root.get("name"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(new Predicate[0]));
                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (pageQuery != null) {
            return query(themeDetailRepository, pageQuery, sort, specification);
        } else {
            return query(themeDetailRepository, sort, specification);
        }
    }

    public ThemeDetail findById(String id) {
        return getEntityById(themeDetailRepository, id);
    }

    public List<ThemeDetailVo> findByThemeId(String themeId) {
        List<ThemeDetail> themeDetailList = themeDetailRepository.findAllByThemeIdAndStatus(themeId, StatusEnum.OK.getCode());
        Map<String, ThemeDetailVo> themeDetailVoMap = new HashMap<>();
        for (ThemeDetail themeDetail : themeDetailList) {
            // 参与人员
            ThemeEmpParam themeEmpParam = new ThemeEmpParam();
            themeEmpParam = (ThemeEmpParam) BeanCopyUtil.copyBean(themeDetail, themeEmpParam);
            if (themeDetailVoMap.get(themeDetail.getEventName()) == null) {
                ThemeDetailVo themeDetailVo = new ThemeDetailVo();
                themeDetailVo = (ThemeDetailVo) BeanCopyUtil.copyBean(themeDetail, themeDetailVo);
                // 查找事件
                if (themeDetail.getEventId() != null) {
                    Event event = eventService.findById(themeDetail.getEventId());
                    themeDetailVo.setAscoreMin(event.getAscoreMin());
                    themeDetailVo.setAscoreMax(event.getAscoreMax());
                    themeDetailVo.setBscoreMin(event.getBscoreMin());
                    themeDetailVo.setBscoreMax(event.getBscoreMax());
                }
                themeDetailVoMap.put(themeDetail.getEventName(), themeDetailVo);
            }
            themeDetailVoMap.get(themeDetail.getEventName()).getThemeEmpParamList().add(themeEmpParam);
        }
        return new ArrayList<>(themeDetailVoMap.values());
    }

    /**
     * 我的积分，日常奖扣信息，查找（当前登录）用户参与的审核通过的积分奖扣主题详情
     *
     * @param pageQuery
     * @param code
     * @param themeDate
     * @param empId
     * @return
     */
    public PageResult<ThemeDetailDailyVo> themeDetailDaily(PageQuery pageQuery, int code, Long themeDate, String empId) {
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        if (empId != null) {
            loginUserEmpId = empId;
        }
        if (StringUtil.isBlank(loginUserEmpId)) {
            throw new BusinessException("没有用户信息，无法查找日常奖扣记录");
        }
        Date date = new Date();
        if (themeDate != null) {
            date = new Date(themeDate);
        }
        Date stM = DateUtil.beginOfMonth(date); // 查找任务日期月份第一天
        Date etM = DateUtil.endOfMonth(date); // 任务月份最后时间
        if (pageQuery != null) {
            Pageable pageable = PageRequest.of(pageQuery.getPageNo(), pageQuery.getPageSize(), new Sort(Sort.Direction.DESC, "createDate"));
            Page<ThemeDetailDailyVo> resultPage = themeDetailRepository.findAllByStatusAndEmpIdAndThemeStatusAndThemeDatePageable(code, loginUserEmpId, ThemeStatusEnum.SUCCESS.getCode(), stM, etM, pageable);
            return new PageResult<>(resultPage.getTotalElements(), resultPage.getContent());
        } else {
            List<ThemeDetailDailyVo> themeDetailDailyVoList = themeDetailRepository.findAllByStatusAndEmpIdAndThemeStatusAndThemeDateSort(code, loginUserEmpId, ThemeStatusEnum.SUCCESS.getCode(), stM, etM, new Sort(Sort.Direction.DESC, "createDate"));
            return new PageResult<>(themeDetailDailyVoList.size(), themeDetailDailyVoList);
        }
    }

    /**
     * 我的积分，人员积分总览(包括月度、年度、累计积分趋势)
     *
     * @param themeDate
     * @param empId
     * @return
     */
    public MyPointsScoreTrendVo totalScore(Long themeDate, String empId) {
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        if (empId != null) {
            loginUserEmpId = empId;
        }
        if (StringUtil.isBlank(loginUserEmpId)) {
            throw new BusinessException("没有用户信息，无法查找人员积分总览(包括月度、年度、累计积分趋势)");
        }
        Date date = new Date();
        if (themeDate != null) {
            date = new Date(themeDate);
        }
        // 趋势数值
        Map<String, DateVo> monthDateMap = new LinkedHashMap<>();
        Map<String, Integer> monthAwardValueMap = new LinkedHashMap<>();
        Map<String, Integer> monthDeductValueMap = new LinkedHashMap<>();
        Map<String, DateVo> yearDateMap = new LinkedHashMap<>();
        Map<String, Integer> yearAwardValueMap = new LinkedHashMap<>();
        Map<String, Integer> yearDeductValueMap = new LinkedHashMap<>();
        Map<String, Integer> totalAwardValueMap = new LinkedHashMap<>();
        Map<String, Integer> totalDeductValueMap = new LinkedHashMap<>();
        // 系统开始使用年份-输入事件年份
        Date st = DateUtil.parse("2019-01-01 00:00:00");
        Date et = DateUtil.endOfYear(date);
        for (int i = DateUtil.year(st); i <= DateUtil.year(et); i++) {
            Date date1 = DateUtil.parseDate(i + "-01-01 00:00:00");
            DateVo dateVo = new DateVo(DateUtil.beginOfYear(date1), DateUtil.endOfYear(date1));
            yearDateMap.put(i + "", dateVo);
            yearAwardValueMap.put(i + "", 0);
            yearDeductValueMap.put(i + "", 0);
        }
        // 输入时间年份1月-12月，12个月份，一年有多少个月
        Date stM = DateUtil.beginOfYear(date);
        for (int i = DateUtil.month(stM); i <= DateUtil.month(et); i++) {
            Date date1 = DateUtil.offsetMonth(st, i);
            DateVo dateVo = new DateVo(DateUtil.beginOfMonth(date1), DateUtil.endOfMonth(date1));
            monthDateMap.put(String.format("%02d", i + 1), dateVo);
            monthAwardValueMap.put(String.format("%02d", i + 1), 0);
            monthDeductValueMap.put(String.format("%02d", i + 1), 0);
        }
        // 日常奖扣信息
        List<ThemeDetailDailyVo> themeDetailDailyVoList = themeDetailRepository.findAllByStatusAndEmpIdAndThemeStatusAndThemeDateSort(StatusEnum.OK.getCode(), loginUserEmpId, ThemeStatusEnum.SUCCESS.getCode(), st, et, new Sort(Sort.Direction.DESC, "createDate"));
        SysEmployee sysEmployee = (SysEmployee) redisTemplate.opsForValue().get(StringUtil.getRedisKey(RedisConstant.EMPLOYEE, loginUserEmpId));
        if (sysEmployee == null) {
            throw new BusinessException("无法从redis中获取用户缓存信息(" + loginUserEmpId + ")，请管理员重新加载缓存");
        }
        MyPointsScoreTrendVo myPointsScoreTrendVo = new MyPointsScoreTrendVo();
        myPointsScoreTrendVo.setEmpId(loginUserEmpId);
        myPointsScoreTrendVo.setEmpFullName(sysEmployee.getFullName());
        myPointsScoreTrendVo.setEmpOrgId(sysEmployee.getOrganizationId());
        myPointsScoreTrendVo.setEmpOrgName(sysEmployee.getOrgName());
        for (ThemeDetailDailyVo themeDetailDailyVo : themeDetailDailyVoList) {
            for (Map.Entry<String, DateVo> entry : monthDateMap.entrySet()) {
                if (themeDetailDailyVo.getThemeDate().compareTo(entry.getValue().getStartD()) >= 0 && themeDetailDailyVo.getThemeDate().compareTo(entry.getValue().getEndD()) <= 0) {
                    if (themeDetailDailyVo.getBscore() > 0) { // 奖分
                        myPointsScoreTrendVo.setMonthAwardScore(myPointsScoreTrendVo.getMonthAwardScore() + themeDetailDailyVo.getBscore());
                        monthAwardValueMap.put(entry.getKey(), monthAwardValueMap.get(entry.getKey()) + themeDetailDailyVo.getBscore());
                    }
                    if (themeDetailDailyVo.getBscore() < 0) { // 扣分
                        myPointsScoreTrendVo.setMonthDeductScore(myPointsScoreTrendVo.getMonthDeductScore() + themeDetailDailyVo.getBscore());
                        monthDeductValueMap.put(entry.getKey(), monthDeductValueMap.get(entry.getKey()) + themeDetailDailyVo.getBscore());
                    }
                    break;
                }
            }
            for (Map.Entry<String, DateVo> entry : yearDateMap.entrySet()) {
                if (themeDetailDailyVo.getThemeDate().compareTo(entry.getValue().getStartD()) >= 0 && themeDetailDailyVo.getThemeDate().compareTo(entry.getValue().getEndD()) <= 0) {
                    if (themeDetailDailyVo.getBscore() > 0) { // 奖分
                        myPointsScoreTrendVo.setYearAwardScore(myPointsScoreTrendVo.getYearAwardScore() + themeDetailDailyVo.getBscore());
                        yearAwardValueMap.put(entry.getKey(), yearAwardValueMap.get(entry.getKey()) + themeDetailDailyVo.getBscore());
                    }
                    if (themeDetailDailyVo.getBscore() < 0) { // 扣分
                        myPointsScoreTrendVo.setYearDeductScore(myPointsScoreTrendVo.getYearDeductScore() + themeDetailDailyVo.getBscore());
                        yearDeductValueMap.put(entry.getKey(), yearDeductValueMap.get(entry.getKey()) + themeDetailDailyVo.getBscore());
                    }
                    break;
                }
            }
        }
        // todo: 奖扣任务
        myPointsScoreTrendVo.setMonthSeriesList(new ArrayList<>(monthDateMap.keySet()));
        myPointsScoreTrendVo.setMonthAwardValueTrendList(new ArrayList<>(monthAwardValueMap.values()));
        myPointsScoreTrendVo.setMonthDeductValueTrendList(new ArrayList<>(monthDeductValueMap.values()));
        myPointsScoreTrendVo.setYearSeriesList(new ArrayList<>(yearDateMap.keySet()));
        myPointsScoreTrendVo.setYearAwardValueTrendList(new ArrayList<>(yearAwardValueMap.values()));
        myPointsScoreTrendVo.setYearDeductValueTrendList(new ArrayList<>(yearDeductValueMap.values()));
        // 累计积分
        for (Map.Entry<String, Integer> entry : yearAwardValueMap.entrySet()) {
            myPointsScoreTrendVo.setTotalAwardScore(myPointsScoreTrendVo.getTotalAwardScore() + entry.getValue());
            totalAwardValueMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Integer> entry : yearDeductValueMap.entrySet()) {
            myPointsScoreTrendVo.setTotalDeductScore(myPointsScoreTrendVo.getTotalDeductScore() + entry.getValue());
            totalDeductValueMap.put(entry.getKey(), entry.getValue());
        }
        myPointsScoreTrendVo.setTotalSeriesList(new ArrayList<>(yearDateMap.keySet()));
        myPointsScoreTrendVo.setTotalAwardValueTrendList(new ArrayList<>(totalAwardValueMap.values()));
        myPointsScoreTrendVo.setTotalDeductValueTrendList(new ArrayList<>(totalDeductValueMap.values()));
        return myPointsScoreTrendVo;
    }
}