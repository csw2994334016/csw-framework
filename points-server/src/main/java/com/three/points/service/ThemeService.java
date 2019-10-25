package com.three.points.service;

import com.three.commonclient.exception.BusinessException;
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
import java.util.*;

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
        checkAttnIdAndAuditId(themeParam);

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
        checkAttnIdAndAuditId(themeParam);

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

    private void checkAttnIdAndAuditId(ThemeParam themeParam) {
        if (themeParam.getAttnId().equals(themeParam.getAuditId())) {
            throw new BusinessException("终审人和初审人不能相同");
        }
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
        List<ThemeDetail> themeDetailList = new ArrayList<>();
        for (String id : idSet) {
            Theme theme = getEntityById(themeRepository, String.valueOf(id));
            theme.setStatus(code);
            themeList.add(theme);
            List<ThemeDetail> themeDetails = themeDetailRepository.findAllByThemeId(theme.getId());
            themeDetails.forEach(e -> {
                e.setStatus(code);
                themeDetailList.add(e);
            });
        }
        themeRepository.saveAll(themeList);
        themeDetailRepository.saveAll(themeDetailList);
    }

    public PageResult<Theme> query(PageQuery pageQuery, int code, String whoFlag, String themeName,
                                   String recordDateSt, String recordDateEt, String themeDateSt, String themeDateEt,
                                   String attnName, String auditName, String recorderName, Integer themeStatus) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Theme> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Specification<Theme> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));

            String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
            if (loginUserEmpId != null) {
                if ("2".equals(whoFlag)) { // 我参与的奖扣
                    List<String> themeIdList = themeDetailRepository.findThemeIdByEmpId(loginUserEmpId);
                    if (themeIdList != null) {
                        predicateList.add(root.get("id").in(themeIdList));
                    }
                } else { // 我提交的奖扣
                    predicateList.add(criteriaBuilder.equal(root.get("submitterId"), loginUserEmpId));
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

    public PageResult<Theme> queryApproval(PageQuery pageQuery, int code, String whoFlag, String themeName,
                                           String recordDateSt, String recordDateEt, String themeDateSt, String themeDateEt,
                                           String attnName, String auditName, String recorderName, Integer themeStatus) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Theme> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Specification<Theme> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));

            addPredicateToList(predicateList, criteriaBuilder, root,
                    themeName, attnName, auditName, recorderName, themeStatus, recordDateSt, recordDateEt, themeDateSt, themeDateEt);

            String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
            if (loginUserEmpId != null) {
                if ("3".equals(whoFlag)) { // 抄送给我
                    predicateList.add(criteriaBuilder.equal(root.get("copyPersonId"), loginUserEmpId));
                } else {
                    List<Predicate> predicateList1 = new ArrayList<>();
                    // 初审人或终审人
                    Predicate p1 = criteriaBuilder.equal(root.get("attnId"), loginUserEmpId);
                    predicateList1.add(criteriaBuilder.or(p1));
                    Predicate p2 = criteriaBuilder.equal(root.get("auditId"), loginUserEmpId);
                    predicateList1.add(criteriaBuilder.or(p2));
                    if ("2".equals(whoFlag)) { // 我已审核
                        predicateList.add(root.get("themeStatus").in(Arrays.asList(ThemeEnum.AUDIT.getCode(), ThemeEnum.SUCCESS.getCode())));
                    } else { // 待我审核
                        predicateList.add(root.get("themeStatus").in(Arrays.asList(ThemeEnum.ATTN.getCode(), ThemeEnum.AUDIT.getCode())));
                    }
                    return criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[0])), criteriaBuilder.and(predicateList1.toArray(new Predicate[0]))).getRestriction();
                }
            }
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

    @Transactional
    public void submit(String id) {
        Theme theme = getEntityById(themeRepository, id);
        if (theme.getThemeStatus() == ThemeEnum.SAVE.getCode()) { // 只有保存状态才能提交
            // 当前用户是初审人,则直接到待终审状态
            theme.setThemeStatus(ThemeEnum.ATTN.getCode());
            if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(LoginUserUtil.getLoginUserEmpId())) {
                theme.setThemeStatus(ThemeEnum.AUDIT.getCode());
            }
            themeRepository.save(theme);
        } else {
            throw new BusinessException("只有保存状态才能提交");
        }
    }

    @Transactional
    public void retreat(String id) {
        Theme theme = getEntityById(themeRepository, id);
        if (theme.getThemeStatus() != ThemeEnum.LOCK.getCode()) {
            String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
            if (theme.getThemeStatus() == ThemeEnum.ATTN.getCode()) { // 记录是待初审状态,只有当前用户是记录人才能撤回
                if (theme.getRecorderId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeEnum.SAVE.getCode());
                    themeRepository.save(theme);
                } else {
                    throw new BusinessException("记录是待初审状态,只有当前用户是记录人才能撤回");
                }
            } else if (theme.getThemeStatus() == ThemeEnum.AUDIT.getCode() || theme.getThemeStatus() == ThemeEnum.REJECT.getCode()) { // 记录是待终审/驳回状态,只有当前用户是初审人才能撤回
                if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeEnum.ATTN.getCode());
                    themeRepository.save(theme);
                } else {
                    throw new BusinessException("记录是待终审/驳回状态,只有当前用户是初审人才能撤回");
                }
            } else if (theme.getThemeStatus() == ThemeEnum.SUCCESS.getCode()) { // 记录是审核通过状态,只有当前用户是终审人才能撤回
                if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeEnum.LOCK.getCode());
                    themeRepository.save(theme);
                } else {
                    throw new BusinessException("记录是审核通过状态,只有当前用户是终审人才能撤回");
                }
            } else {
                throw new BusinessException("该状态[" + theme.getThemeStatus() + "]不能撤回");
            }
        } else {
            throw new BusinessException("锁定状态不能撤回");
        }
    }

    @Transactional
    public void reject(String id, String opinion, Integer recorderBScore, Integer attnBScore) {
        Theme theme = getEntityById(themeRepository, id);
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        if (theme.getThemeStatus() == ThemeEnum.ATTN.getCode()) { // 记录是待初审状态,只有当前用户是初审人才能驳回
            if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                theme.setThemeStatus(ThemeEnum.SAVE.getCode());
                theme.setAttnOpinion(opinion);
                themeRepository.save(theme);
            } else {
                throw new BusinessException("记录是待初审状态,只有当前用户是初审人才能驳回");
            }
        } else if (theme.getThemeStatus() == ThemeEnum.AUDIT.getCode() || theme.getThemeStatus() == ThemeEnum.LOCK.getCode()) { // 记录是待终审/锁定状态,只有当前用户是终审人才能驳回
            if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                theme.setThemeStatus(ThemeEnum.ATTN.getCode());
                theme.setAuditOpinion(opinion);
                // todo: 只有终审人驳回才能进行扣分操作
                themeRepository.save(theme);
            } else {
                throw new BusinessException("记录是待终审/锁定状态,只有当前用户是终审人才能驳回");
            }
        } else {
            throw new BusinessException("该状态[" + theme.getThemeStatus() + "]不能撤回");
        }
    }

    @Transactional
    public void approve(String id, String opinion, Integer recorderBScore, Integer attnBScore) {
        Theme theme = getEntityById(themeRepository, id);
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        if (theme.getThemeStatus() == ThemeEnum.ATTN.getCode()) { // 记录是待初审状态,只有当前用户是初审人才能通过
            if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                theme.setThemeStatus(ThemeEnum.AUDIT.getCode());
                theme.setAttnOpinion(opinion);
                themeRepository.save(theme);
            } else {
                throw new BusinessException("记录是待初审状态,只有当前用户是初审人才能通过");
            }
        } else if (theme.getThemeStatus() == ThemeEnum.AUDIT.getCode()) { // 记录是待终审状态,只有当前用户是终审人才能通过
            if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                theme.setThemeStatus(ThemeEnum.SUCCESS.getCode());
                theme.setAuditOpinion(opinion);
                // todo: 只有终审人驳回才能进行奖分操作
                // todo: 事件参与人员积分实时结算
                themeRepository.save(theme);
            } else {
                throw new BusinessException("记录是待终审状态,只有当前用户是终审人才能通过");
            }
        } else {
            throw new BusinessException("该状态[" + theme.getThemeStatus() + "]不能通过");
        }
    }
}