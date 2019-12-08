package com.three.points.service;

import cn.hutool.core.date.DateUtil;
import com.netflix.discovery.converters.Auto;
import com.three.common.enums.StatusEnum;
import com.three.commonclient.exception.BusinessException;
import com.three.points.entity.Event;
import com.three.points.entity.Theme;
import com.three.points.entity.ThemeDetail;
import com.three.points.enums.ThemeStatusEnum;
import com.three.points.param.ThemeEmpParam;
import com.three.points.repository.ThemeDetailRepository;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.points.repository.ThemeRepository;
import com.three.points.vo.ThemeDetailDailyVo;
import com.three.points.vo.ThemeDetailStatisticsVo;
import com.three.points.vo.ThemeDetailVo;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
            List<Predicate> predicateList = new ArrayList<>();

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

    public PageResult<ThemeDetailDailyVo> themeDetailDaily(PageQuery pageQuery, int code, Long themeDate) {
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        if (StringUtil.isNotBlank(loginUserEmpId)) {
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
        } else {
            throw new BusinessException("用户没有登录，无法查找日常奖扣记录");
        }
    }

    public PageResult<ThemeDetailStatisticsVo> themeDetailStatistics(PageQuery pageQuery, int code, String orgId, Long themeDateSt, Long themeDateEt, String searchValue) {
        return null;
    }
}