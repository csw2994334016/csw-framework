package com.three.points.service;

import com.three.points.enums.ThemeEnum;
import com.three.points.entity.Theme;
import com.three.points.entity.ThemeDetail;
import com.three.points.param.ThemeEmpParam;
import com.three.points.param.ThemeEventParam;
import com.three.points.param.ThemeParam;
import com.three.points.repository.ThemeDetailRepository;
import com.three.points.repository.ThemeRepository;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-10-24.
 * Description:
 */

@Service
public class ThemeService extends BaseService<Theme, String> {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeDetailRepository themeDetailRepository;

    @Transactional
    public void createDraft(ThemeParam themeParam) {
        // 创建主题
        Theme theme = new Theme();

        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        theme.setThemeStatus(ThemeEnum.DRAFT.getCode());

        saveThemeAndThemeDetailList(theme, themeDetailList);
    }

    @Transactional
    public void create(ThemeParam themeParam) {
        BeanValidator.check(themeParam);

        Theme theme = new Theme();

        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        theme.setThemeStatus(ThemeEnum.SAVE.getCode());

        saveThemeAndThemeDetailList(theme, themeDetailList);
    }

    private void saveThemeAndThemeDetailList(Theme theme, List<ThemeDetail> themeDetailList) {
        theme.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());
        String empId = LoginUserUtil.getLoginUserEmpId();
        String empFullName = LoginUserUtil.getLoginUserEmpFullName();
        theme.setRecorderId(empId);
        theme.setRecorderName(empFullName);
        theme = themeRepository.save(theme);

        for (ThemeDetail themeDetail : themeDetailList) {
            themeDetail.setThemeId(theme.getId());
            themeDetail.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());
        }
        themeDetailRepository.saveAll(themeDetailList);
    }

    @Transactional
    public void update(ThemeParam themeParam) {
        BeanValidator.check(themeParam);

        Theme theme = getEntityById(themeRepository, themeParam.getId());
        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        theme = themeRepository.save(theme);

        for (ThemeDetail themeDetail : themeDetailList) {
            themeDetail.setThemeId(theme.getId());
        }
        // 删除原有记录
        themeDetailRepository.deleteByThemeId(theme.getId());

        themeDetailRepository.saveAll(themeDetailList);
    }

    private void createTheme(Theme theme, List<ThemeDetail> themeDetailList, ThemeParam themeParam) {
        theme = (Theme) BeanCopyUtil.copyBean(themeParam, theme);

        // 设置主题详情
        setThemeDetailList(theme, themeDetailList, themeParam.getThemeEventParamList());

        // 设置主题属性
        setTheme(theme, themeDetailList);
    }

    private void setThemeDetailList(Theme theme, List<ThemeDetail> themeDetailList, List<ThemeEventParam> themeEventParamList) {
        for (ThemeEventParam themeEventParam : themeEventParamList) {
            // 事件
            BeanValidator.check(themeEventParam);
            // 事件参与人
            for (ThemeEmpParam themeEmpParam : themeEventParam.getThemeEmpParamList()) {
                BeanValidator.check(themeEmpParam);
                // 主题详情
                ThemeDetail themeDetail = new ThemeDetail();
                themeDetail.setThemeName(theme.getThemeName());
                themeDetail.setThemeDate(theme.getThemeDate());
                themeDetail.setEventTypeId(themeEventParam.getEventTypeId());
                themeDetail.setEventTypeName(themeEventParam.getEventTypeName());
                themeDetail.setEventId(themeEventParam.getEventId());
                themeDetail.setEventName(themeEventParam.getEventName());
                themeDetail.setPrizeFlag(themeEventParam.getPrizeFlag());
                themeDetail.setCountFlag(themeEventParam.getCountFlag());
                themeDetail.setAuditFlag(themeEventParam.getAuditFlag());
                themeDetail.setEmpId(themeEmpParam.getEmpId());
                themeDetail.setEmpFullName(themeEmpParam.getEmpFullName());
                themeDetail.setAScore(themeEmpParam.getAScore());
                themeDetail.setBScore(themeEmpParam.getBScore());
                themeDetail.setRemark(themeEventParam.getRemark());
                themeDetailList.add(themeDetail);
            }
        }
    }

    private void setTheme(Theme theme, List<ThemeDetail> themeDetailList) {
        String empId = LoginUserUtil.getLoginUserEmpId();
        String empFullName = LoginUserUtil.getLoginUserEmpFullName();
        theme.setLastEditUserID(empId);
        theme.setLastEditUserName(empFullName);

        theme.setEmpCount(themeDetailList.size());
        int aPosScore = 0;
        int aNegScore = 0;
        int bPosScore = 0;
        int bNegScore = 0;
        for (ThemeDetail themeDetail : themeDetailList) {
            if (themeDetail.getAScore() > 0) {
                aPosScore += themeDetail.getAScore();
            } else {
                aNegScore += themeDetail.getAScore();
            }
            if (themeDetail.getBScore() > 0) {
                bPosScore += themeDetail.getBScore();
            } else {
                bNegScore += themeDetail.getBScore();
            }
        }
        theme.setAPosScore(aPosScore);
        theme.setANegScore(aNegScore);
        theme.setBPosScore(bPosScore);
        theme.setBNegScore(bNegScore);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Theme> themeList = new ArrayList<>();
        for (String id : idSet) {
            Theme theme = getEntityById(themeRepository, String.valueOf(id));
            theme.setStatus(code);
            themeList.add(theme);
        }

        themeRepository.saveAll(themeList);
    }

    public PageResult<Theme> query(PageQuery pageQuery, int code, String whoFlag, String themeName,
                                   String recordDateSt, String recordDateEt, String themeDateSt, String themeDateEt,
                                   String attnName, String auditName, String recorderName, Integer themeStatus) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Theme> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Specification<Theme> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));

            // 我提交的奖扣
            String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
            if ("1".equals(whoFlag) && loginUserEmpId != null) {
                predicateList.add(criteriaBuilder.equal(root.get("submitterId"), loginUserEmpId));
            }
            // 我参与的奖扣
            if ("2".equals(whoFlag) && loginUserEmpId != null) {
                List<String> themeIdList = themeDetailRepository.findThemeIdByEmpId(loginUserEmpId);
                if (themeIdList != null) {
                    predicateList.add(root.get("id").in(themeIdList));
                }
            }

            addPredicateToList(predicateList, criteriaBuilder, root,
                    themeName, attnName, auditName, recorderName, themeStatus, recordDateSt, recordDateEt, themeDateSt, themeDateEt);

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        if (pageQuery != null) {
            return query(themeRepository, pageQuery, sort, specification);
        } else {
            return query(themeRepository, sort, specification);
        }
    }

    private void addPredicateToList(List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Theme> root,
                                    String themeName, String attnName, String auditName, String recorderName, Integer themeStatus,
                                    String recordDateSt, String recordDateEt, String themeDateSt, String themeDateEt) {
        if (StringUtil.isNotBlank(themeName)) {
            predicateList.add(criteriaBuilder.like(root.get("themeName"), "%" + themeName + "%"));
        }
        if (StringUtil.isNotBlank(attnName)) {
            predicateList.add(criteriaBuilder.like(root.get("attnName"), "%" + attnName + "%"));
        }
        if (StringUtil.isNotBlank(auditName)) {
            predicateList.add(criteriaBuilder.like(root.get("auditName"), "%" + auditName + "%"));
        }
        if (StringUtil.isNotBlank(recorderName)) {
            predicateList.add(criteriaBuilder.like(root.get("recorderName"), "%" + recorderName + "%"));
        }
        if (themeStatus != null) {
            predicateList.add(criteriaBuilder.equal(root.get("themeStatus"), themeStatus));
        }
        // 记录时间、奖扣时间
        if (StringUtil.isNotBlank(recordDateSt)) {
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), StringUtil.getStrToDate(recordDateSt)));
            if (StringUtil.isNotBlank(recordDateEt)) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), StringUtil.getStrToDate(recordDateEt)));
            } else {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), StringUtil.sdf.format(new Date())));
            }
        }
        if (StringUtil.isNotBlank(themeDateSt)) {
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("themeDate"), StringUtil.getStrToDate(themeDateSt)));
            if (StringUtil.isNotBlank(themeDateEt)) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("themeDate"), StringUtil.getStrToDate(themeDateEt)));
            } else {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("themeDate"), StringUtil.sdf.format(new Date())));
            }
        }
    }


    public Theme findById(String id) {
        return getEntityById(themeRepository, id);
    }
}