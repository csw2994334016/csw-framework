package com.three.points.service;

import com.three.common.auth.SysEmployee;
import com.three.common.constants.RedisConstant;
import com.three.common.exception.BusinessException;
import com.three.common.exception.ParameterException;
import com.three.points.enums.EventEnum;
import com.three.points.enums.EventFlagEnum;
import com.three.points.enums.ThemeStatusEnum;
import com.three.points.entity.Theme;
import com.three.points.entity.ThemeDetail;
import com.three.points.enums.ThemeTypeEnum;
import com.three.points.feign.UserClient;
import com.three.points.param.ThemeEmpParam;
import com.three.points.param.ThemeEventParam;
import com.three.points.param.ThemeParam;
import com.three.points.repository.ThemeDetailRepository;
import com.three.points.repository.ThemeRepository;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.common.utils.BeanValidator;
import com.three.points.vo.ThemeApprovalVo;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by csw on 2019-10-24.
 * Description: lzqgdgs176
 */

@Service
public class ThemeService extends BaseService<Theme, String> {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeDetailRepository themeDetailRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Transactional
    public void createDraft(ThemeParam themeParam) {
        // 创建主题
        Theme theme = new Theme();

        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        theme.setThemeStatus(ThemeStatusEnum.DRAFT.getCode());

        saveThemeAndThemeDetailList("draft", theme, themeDetailList);
    }

    @Transactional
    public void create(ThemeParam themeParam) {
        BeanValidator.check(themeParam);
        checkAttnIdAndAuditId(themeParam);

        Theme theme = new Theme();

        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        theme.setThemeStatus(ThemeStatusEnum.ATTN.getCode());

        saveThemeAndThemeDetailList(null, theme, themeDetailList);
    }

    private void saveThemeAndThemeDetailList(String draft, Theme theme, List<ThemeDetail> themeDetailList) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        theme.setOrganizationId(firstOrganizationId);

        String empId = LoginUserUtil.getLoginUserEmpId();
        String empFullName = LoginUserUtil.getLoginUserEmpFullName();
        theme.setRecorderId(empId);
        theme.setRecorderName(empFullName);

        if (!"draft".equals(draft)) {
            theme.setSubmitterDate(new Date());
            if (theme.getAttnId().equals(theme.getRecorderId())) {
                theme.setThemeStatus(ThemeStatusEnum.AUDIT.getCode());
            }
        }

        saveThemeAndThemeDetail(theme, themeDetailList);
    }

    @Transactional
    public void update(ThemeParam themeParam) {
        BeanValidator.check(themeParam);
        checkAttnIdAndAuditId(themeParam);

        Theme theme = getEntityById(themeRepository, themeParam.getId());
        if (theme.getThemeStatus() != ThemeStatusEnum.DRAFT.getCode() && theme.getThemeStatus() != ThemeStatusEnum.SAVE.getCode()) {
            throw new BusinessException("记录只有在草稿或保存状态下才能编辑");
        }

        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        theme.setThemeStatus(ThemeStatusEnum.ATTN.getCode());
        theme.setSubmitterDate(new Date());
        if (theme.getAttnId().equals(theme.getRecorderId())) {
            theme.setThemeStatus(ThemeStatusEnum.AUDIT.getCode());
        }

        // 删除原有记录
        themeDetailRepository.deleteByThemeId(theme.getId());

        saveThemeAndThemeDetail(theme, themeDetailList);
    }

    private void saveThemeAndThemeDetail(Theme theme, List<ThemeDetail> themeDetailList) {
        theme = themeRepository.save(theme);
        for (ThemeDetail themeDetail : themeDetailList) {
            themeDetail.setThemeId(theme.getId());
            themeDetail.setOrganizationId(theme.getOrganizationId());
        }
        themeDetailRepository.saveAll(themeDetailList);
    }

    private void checkAttnIdAndAuditId(ThemeParam themeParam) {
        if (themeParam.getAttnId().equals(themeParam.getAuditId())) {
            throw new BusinessException("终审人不能是初审人");
        }
    }

    private void createTheme(Theme theme, List<ThemeDetail> themeDetailList, ThemeParam themeParam) {
        theme = (Theme) BeanCopyUtil.copyBean(themeParam, theme);

        theme.setThemeDate(new Date(themeParam.getThemeDate()));
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
                themeDetail.setThemeType(theme.getThemeType());
                themeDetail.setEventTypeId(themeEventParam.getEventTypeId());
                themeDetail.setEventTypeName(themeEventParam.getEventTypeName());
                themeDetail.setEventId(themeEventParam.getEventId());
                themeDetail.setEventName(themeEventParam.getEventName());
                themeDetail.setEventFlag(EventFlagEnum.TEMPORARY.getCode());
                if (StringUtil.isNotBlank(themeEventParam.getEventId())) {
                    themeDetail.setEventFlag(EventFlagEnum.STANDARD.getCode());
                }
                themeDetail.setPrizeFlag(themeEventParam.getPrizeFlag());
                themeDetail.setCountFlag(themeEventParam.getCountFlag());
                themeDetail.setAuditFlag(themeEventParam.getAuditFlag());
                themeDetail.setEmpId(themeEmpParam.getEmpId());
                themeDetail.setEmpFullName(themeEmpParam.getEmpFullName());
                themeDetail.setEmpNum(themeEmpParam.getEmpNum());
                themeDetail.setEmpOrgId(themeEmpParam.getEmpOrgId());
                themeDetail.setEmpOrgName(themeEmpParam.getEmpOrgName());
                themeDetail.setAscore(themeEmpParam.getAscore());
                themeDetail.setBscore(themeEmpParam.getBscore());
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

        if (empId != null && empId.equals(theme.getAuditId())) {
            throw new BusinessException("终审人不能是记录人");
        }

        theme.setEmpCount(themeDetailList.size());
        int aPosScore = 0;
        int aNegScore = 0;
        int bPosScore = 0;
        int bNegScore = 0;
        for (ThemeDetail themeDetail : themeDetailList) {
            if (themeDetail.getAscore() > 0) {
                aPosScore += themeDetail.getAscore();
            } else {
                aNegScore += themeDetail.getAscore();
            }
            if (themeDetail.getBscore() > 0) {
                bPosScore += themeDetail.getBscore();
            } else {
                bNegScore += themeDetail.getBscore();
            }
            if (themeDetail.getEmpId().equals(theme.getAuditId())) {
                throw new BusinessException("终审人不能是奖扣对象");
            }
        }
        theme.setAposScore(aPosScore);
        theme.setAnegScore(aNegScore);
        theme.setBposScore(bPosScore);
        theme.setBnegScore(bNegScore);
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
                                   Long recordDateSt, Long recordDateEt, Long themeDateSt, Long themeDateEt,
                                   String attnName, String auditName, String recorderName, Integer themeStatus) {
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        List<String> themeIdList = new ArrayList<>();
        if ("2".equals(whoFlag)) { // 我参与的奖扣、并且状态是审核通过
            themeIdList = themeDetailRepository.findThemeIdByEmpIdAndThemeStatus(loginUserEmpId, ThemeStatusEnum.SUCCESS.getCode());
            if (themeIdList == null || themeIdList.size() == 0) { // 没有我参与的奖扣
                return new PageResult<>(new ArrayList<>());
            }
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        List<String> finalThemeIdList = themeIdList;
        Specification<Theme> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            Specification<Theme> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, LoginUserUtil.getLoginUserFirstOrganizationId());
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));

            if (loginUserEmpId != null) {
                if ("2".equals(whoFlag)) { // 我参与的奖扣
                    predicateList.add(root.get("id").in(finalThemeIdList));
                } else { // 我提交的奖扣
                    predicateList.add(criteriaBuilder.equal(root.get("recorderId"), loginUserEmpId));
                }
            }
            addPredicateToList(predicateList, criteriaBuilder, root, themeName, attnName, auditName, recorderName, themeStatus, recordDateSt, recordDateEt, themeDateSt, themeDateEt);

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
        if (pageQuery != null) {
            return query(themeRepository, pageQuery, sort, specification);
        } else {
            return query(themeRepository, sort, specification);
        }
    }

    public PageResult<Theme> queryApproval(PageQuery pageQuery, int code, String whoFlag, String themeName,
                                           Long recordDateSt, Long recordDateEt, Long themeDateSt, Long themeDateEt,
                                           String attnName, String auditName, String recorderName, Integer themeStatus) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<Theme> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
            Specification<Theme> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            predicateList.add(codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder));

            addPredicateToList(predicateList, criteriaBuilder, root,
                    themeName, attnName, auditName, recorderName, themeStatus, recordDateSt, recordDateEt, themeDateSt, themeDateEt);

            String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
            if (loginUserEmpId != null) {
                if ("3".equals(whoFlag)) { // 抄送给我
                    predicateList.add(criteriaBuilder.like(root.get("copyPersonId"), "%" + loginUserEmpId + "%"));
                } else {
                    List<Predicate> predicateList1 = new ArrayList<>();
                    List<Predicate> predicateList2 = new ArrayList<>();
                    List<Predicate> predicateList3 = new ArrayList<>();
                    if ("2".equals(whoFlag)) { // 我已审核：当前登录用户是初审人、状态码大于ATTN(2, "待初审")；当前登录用户是终审人、状态码大于AUDIT(3, "待终审")
                        predicateList2.add(criteriaBuilder.equal(root.get("attnId"), loginUserEmpId));
                        predicateList2.add(criteriaBuilder.greaterThan(root.get("themeStatus"), ThemeStatusEnum.ATTN.getCode()));
                        predicateList3.add(criteriaBuilder.equal(root.get("auditId"), loginUserEmpId));
                        predicateList3.add(criteriaBuilder.greaterThan(root.get("themeStatus"), ThemeStatusEnum.AUDIT.getCode()));
                    } else { // 待我审核：当前登录用户是初审人、状态码等于ATTN(2, "待初审")；当前登录用户是终审人、状态码等于AUDIT(3, "待终审")
                        predicateList2.add(criteriaBuilder.equal(root.get("attnId"), loginUserEmpId));
                        predicateList2.add(criteriaBuilder.equal(root.get("themeStatus"), ThemeStatusEnum.ATTN.getCode()));
                        predicateList3.add(criteriaBuilder.equal(root.get("auditId"), loginUserEmpId));
                        predicateList3.add(criteriaBuilder.equal(root.get("themeStatus"), ThemeStatusEnum.AUDIT.getCode()));
                    }
                    predicateList1.add(criteriaBuilder.and(predicateList2.toArray(new Predicate[0])));
                    predicateList1.add(criteriaBuilder.and(predicateList3.toArray(new Predicate[0])));
                    return criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[0])), criteriaBuilder.or(predicateList1.toArray(new Predicate[0]))).getRestriction();
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
                                    Long recordDateSt, Long recordDateEt, Long themeDateSt, Long themeDateEt) {
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
        if (recordDateSt != null) {
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), new Date(recordDateSt)));
            if (recordDateEt != null) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), new Date(recordDateEt)));
            } else {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), StringUtil.sdf.format(new Date())));
            }
        }
        if (themeDateSt != null) {
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("themeDate"), new Date(themeDateSt)));
            if (themeDateEt != null) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("themeDate"), new Date(themeDateEt)));
            } else {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("themeDate"), StringUtil.sdf.format(new Date())));
            }
        }
    }


    public Theme findById(String id) {
        return getEntityById(themeRepository, id);
    }

    @Transactional
    public void submit(String ids) {
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<String> errorList = new ArrayList<>();
        for (String id : idSet) {
            Theme theme = getEntityById(themeRepository, id);
            if (theme.getThemeStatus() == ThemeStatusEnum.SAVE.getCode()) { // 只有保存状态才能提交
                // 当前用户是初审人,则直接到待终审状态
                theme.setThemeStatus(ThemeStatusEnum.ATTN.getCode());
                if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeStatusEnum.AUDIT.getCode());
                }
                theme.setSubmitterDate(new Date());
                themeRepository.save(theme);
            } else {
                errorList.add("主题(" + theme.getThemeName() + ")不是拟稿状态，不能提交");
            }
        }
        if (errorList.size() > 0) {
            throw new BusinessException("以下记录操作失败：" + errorList.toString());
        }
    }

    @Transactional
    public void retreat(String ids) {
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<String> errorList = new ArrayList<>();
        for (String id : idSet) {
            Theme theme = getEntityById(themeRepository, id);
            if (theme.getThemeStatus() != ThemeStatusEnum.LOCK.getCode()) {
                if (theme.getThemeStatus() == ThemeStatusEnum.ATTN.getCode()) { // 记录是待初审状态,只有当前用户是记录人才能撤回
                    if (theme.getRecorderId().equals(loginUserEmpId)) {
                        theme.setThemeStatus(ThemeStatusEnum.SAVE.getCode());
                        themeRepository.save(theme);
                    } else {
                        errorList.add("主题(" + theme.getThemeName() + ")是待初审状态,只有记录人才能撤回");
                    }
                } else if (theme.getThemeStatus() == ThemeStatusEnum.AUDIT.getCode()) { // 记录是待终审状态,只有当前用户是初审人才能撤回
                    if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                        theme.setThemeStatus(ThemeStatusEnum.ATTN.getCode());
                        themeRepository.save(theme);
                    } else {
                        errorList.add("主题(" + theme.getThemeName() + ")是待终审状态,只有初审人才能撤回");
                    }
                } else if (theme.getThemeStatus() == ThemeStatusEnum.REJECT.getCode()) { // 记录是驳回状态,只有当前用户是初审人或记录人才能撤回
                    if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                        theme.setThemeStatus(ThemeStatusEnum.ATTN.getCode());
                        themeRepository.save(theme);
                    }
                    if (StringUtil.isNotBlank(theme.getRecorderId()) && theme.getRecorderId().equals(loginUserEmpId)) {
                        theme.setThemeStatus(ThemeStatusEnum.SAVE.getCode());
                        themeRepository.save(theme);
                    } else {
                        errorList.add("主题(" + theme.getThemeName() + ")是驳回状态,只有初审人或记录人才能撤回");
                    }
                } else if (theme.getThemeStatus() == ThemeStatusEnum.SUCCESS.getCode()) { // 记录是审核通过状态,只有当前用户是终审人才能撤回
                    if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                        theme.setThemeStatus(ThemeStatusEnum.LOCK.getCode());
                        // 撤回之前，如果有，相应的奖分记录要删除
                        Theme theme1 = themeRepository.findByRelationThemeIdAndThemeName(theme.getId(), EventEnum.THEME_NAME_AUDIT_POS.getMessage());
                        if (theme1 != null) {
                            themeRepository.delete(theme1);
                            themeDetailRepository.deleteByThemeId(theme1.getId());
                        }
                        themeRepository.save(theme);
                    } else {
                        errorList.add("主题(" + theme.getThemeName() + ")是审核通过状态,只有终审人才能撤回");
                    }
                } else {
                    errorList.add("主题(" + theme.getThemeName() + ")是状态[" + ThemeStatusEnum.getMessageByCode(theme.getThemeStatus()) + "]不能撤回");
                }
            } else {
                errorList.add("主题(" + theme.getThemeName() + ")是锁定状态不能撤回");
            }
        }
        if (errorList.size() > 0) {
            throw new BusinessException("以下记录操作失败：" + errorList.toString());
        }
    }

    @Transactional
    public void reject(String ids, String opinion, Integer recorderBScore, Integer attnBScore) {
        // 只有终审人驳回才能进行扣分操作
        if (recorderBScore != null && recorderBScore > 0) {
            throw new ParameterException("对记录人扣分数值只能是负数");
        }
        if (attnBScore != null && attnBScore > 0) {
            throw new ParameterException("对初审人扣分数值只能是负数");
        }
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<String> errorList = new ArrayList<>();
        for (String id : idSet) {
            Theme theme = getEntityById(themeRepository, id);
            if (theme.getThemeStatus() == ThemeStatusEnum.ATTN.getCode() || theme.getThemeStatus() == ThemeStatusEnum.REJECT.getCode()) { // 记录是待初审或驳回状态,只有当前用户是初审人才能驳回
                if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeStatusEnum.SAVE.getCode());
                    theme.setAttnOpinion(opinion);
                    themeRepository.save(theme);
                } else {
                    errorList.add("主题(" + theme.getThemeName() + ")是待初审或驳回状态,只有初审人才能驳回");
                }
            } else if (theme.getThemeStatus() == ThemeStatusEnum.AUDIT.getCode() || theme.getThemeStatus() == ThemeStatusEnum.LOCK.getCode()) { // 记录是待终审或锁定状态,只有当前用户是终审人才能驳回
                if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeStatusEnum.ATTN.getCode());
                    theme.setAuditOpinion(opinion);
                    if ((recorderBScore != null && recorderBScore < 0) || (attnBScore != null && attnBScore < 0)) {
                        // 先生成主题
                        Theme themeNew = getThemeNew(theme);
                        themeNew.setThemeName(EventEnum.THEME_NAME_AUDIT_NEG.getMessage());
                        // 汇总分、人次
                        if (recorderBScore != null && recorderBScore < 0) {
                            themeNew.setBposScore(themeNew.getBposScore() + recorderBScore);
                            themeNew.setEmpCount(themeNew.getEmpCount() + 1);
                        }
                        if (attnBScore != null && attnBScore < 0) {
                            themeNew.setBposScore(themeNew.getBposScore() + attnBScore);
                            themeNew.setEmpCount(themeNew.getEmpCount() + 1);
                        }
                        themeNew = themeRepository.save(themeNew);
                        // 再生成详情
                        List<ThemeDetail> themeDetailList = new ArrayList<>();
                        if (recorderBScore != null && recorderBScore < 0) {
                            ThemeDetail themeDetail = new ThemeDetail(themeNew.getOrganizationId(), themeNew.getId(), themeNew.getThemeName(), themeNew.getThemeDate(), themeNew.getThemeType(), EventEnum.EVENT_TYPE_AUDIT_NEG.getMessage(), EventFlagEnum.TEMPORARY.getCode());
                            themeDetail.setEventName(EventEnum.EVENT_NAME_RECORDER_NEG.getMessage());
                            themeDetail.setEmpId(theme.getRecorderId());
                            themeDetail.setEmpFullName(theme.getRecorderName());
                            setOrgInfo(theme.getRecorderId(), themeDetail);
                            themeDetail.setBscore(recorderBScore);
                            themeDetail.setRemark(EventEnum.EVENT_NAME_RECORDER_NEG.getMessage());
                            themeDetailList.add(themeDetail);
                        }
                        if (attnBScore != null && attnBScore < 0) {
                            ThemeDetail themeDetail = new ThemeDetail(themeNew.getOrganizationId(), themeNew.getId(), themeNew.getThemeName(), themeNew.getThemeDate(), themeNew.getThemeType(), EventEnum.EVENT_TYPE_AUDIT_NEG.getMessage(), EventFlagEnum.TEMPORARY.getCode());
                            themeDetail.setEventName(EventEnum.EVENT_NAME_ATTN_NEG.getMessage());
                            themeDetail.setEmpId(theme.getAttnId());
                            themeDetail.setEmpFullName(theme.getAttnName());
                            setOrgInfo(theme.getAttnId(), themeDetail);
                            themeDetail.setBscore(attnBScore);
                            themeDetail.setRemark(EventEnum.EVENT_NAME_ATTN_NEG.getMessage());
                            themeDetailList.add(themeDetail);
                        }
                        if (themeDetailList.size() > 0) {
                            themeDetailRepository.saveAll(themeDetailList);
                        }
                    }
                    themeRepository.save(theme);
                } else {
                    errorList.add("主题(" + theme.getThemeName() + ")是待终审或锁定状态,只有终审人才能驳回");
                }
            } else {
                errorList.add("主题(" + theme.getThemeName() + ")状态(" + ThemeStatusEnum.getMessageByCode(theme.getThemeStatus()) + ")不能撤回");
            }
        }
        if (errorList.size() > 0) {
            throw new BusinessException("以下记录操作失败：" + errorList.toString());
        }
    }

    void setOrgInfo(String recorderId, ThemeDetail themeDetail) {
        String key = StringUtil.getRedisKey(RedisConstant.EMPLOYEE, recorderId);
        SysEmployee sysEmployee = (SysEmployee) redisTemplate.opsForValue().get(key);
        if (sysEmployee != null) {
            themeDetail.setEmpNum(sysEmployee.getEmpNum());
            themeDetail.setEmpOrgId(sysEmployee.getOrganizationId());
            themeDetail.setEmpOrgName(sysEmployee.getOrgName());
        }
    }

    @Transactional
    public void approve(String ids, String opinion, Integer recorderBScore, Integer attnBScore) {
        // 只有终审人驳回才能进行扣分操作
        if (recorderBScore != null && recorderBScore < 0) {
            throw new ParameterException("对记录人奖分数值只能是正数");
        }
        if (attnBScore != null && attnBScore < 0) {
            throw new ParameterException("对初审人奖分数值只能是正数");
        }
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<String> errorList = new ArrayList<>();
        for (String id : idSet) {
            Theme theme = getEntityById(themeRepository, id);
            if (theme.getThemeStatus() == ThemeStatusEnum.ATTN.getCode()) { // 记录是待初审状态,只有当前用户是初审人才能通过
                if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeStatusEnum.AUDIT.getCode());
                    theme.setAttnOpinion(opinion);
                    theme.setAttnDate(new Date());
                    themeRepository.save(theme);
                } else {
                    errorList.add("主题(" + theme.getThemeName() + ")是待初审状态,只有初审人才能通过");
                }
            } else if (theme.getThemeStatus() == ThemeStatusEnum.AUDIT.getCode() || theme.getThemeStatus() == ThemeStatusEnum.REJECT.getCode() || theme.getThemeStatus() == ThemeStatusEnum.LOCK.getCode()) { // 记录是待终审、驳回或锁定状态,只有当前用户是终审人才能通过
                if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeStatusEnum.SUCCESS.getCode());
                    theme.setAuditOpinion(opinion);
                    theme.setAuditDate(new Date());
                    // 只有终审人通过才能进行奖分操作，通过新增一条主题详情记录实现对记录人或初审人的加分

                    if ((recorderBScore != null && recorderBScore > 0) || (attnBScore != null && attnBScore > 0)) {
                        // 先生成主题
                        Theme themeNew = getThemeNew(theme);
                        themeNew.setThemeName(EventEnum.THEME_NAME_AUDIT_POS.getMessage());
                        // 汇总分、人次
                        if (recorderBScore != null && recorderBScore > 0) {
                            themeNew.setBposScore(themeNew.getBposScore() + recorderBScore);
                            themeNew.setEmpCount(themeNew.getEmpCount() + 1);
                        }
                        if (attnBScore != null && attnBScore > 0) {
                            themeNew.setBposScore(themeNew.getBposScore() + attnBScore);
                            themeNew.setEmpCount(themeNew.getEmpCount() + 1);
                        }
                        themeNew = themeRepository.save(themeNew);
                        // 再生成详情
                        List<ThemeDetail> themeDetailList = new ArrayList<>();
                        if (recorderBScore != null && recorderBScore > 0) {
                            ThemeDetail themeDetail = new ThemeDetail(themeNew.getOrganizationId(), themeNew.getId(), themeNew.getThemeName(), themeNew.getThemeDate(), themeNew.getThemeType(), EventEnum.EVENT_TYPE_AUDIT_POS.getMessage(), EventFlagEnum.TEMPORARY.getCode());
                            themeDetail.setEventName(EventEnum.EVENT_NAME_RECORDER_POS.getMessage());
                            themeDetail.setEmpId(theme.getRecorderId());
                            themeDetail.setEmpFullName(theme.getRecorderName());
                            setOrgInfo(theme.getRecorderId(), themeDetail);
                            themeDetail.setBscore(recorderBScore);
                            themeDetail.setRemark(EventEnum.EVENT_NAME_RECORDER_POS.getMessage());
                            themeDetailList.add(themeDetail);
                        }
                        if (attnBScore != null && attnBScore > 0) {
                            ThemeDetail themeDetail = new ThemeDetail(themeNew.getOrganizationId(), themeNew.getId(), themeNew.getThemeName(), themeNew.getThemeDate(), themeNew.getThemeType(), EventEnum.EVENT_TYPE_AUDIT_POS.getMessage(), EventFlagEnum.TEMPORARY.getCode());
                            themeDetail.setEventName(EventEnum.EVENT_NAME_ATTN_POS.getMessage());
                            themeDetail.setEmpId(theme.getAttnId());
                            themeDetail.setEmpFullName(theme.getAttnName());
                            setOrgInfo(theme.getAttnId(), themeDetail);
                            themeDetail.setBscore(attnBScore);
                            themeDetail.setRemark(EventEnum.EVENT_NAME_ATTN_POS.getMessage());
                            themeDetailList.add(themeDetail);
                        }
                        if (themeDetailList.size() > 0) {
                            themeDetailRepository.saveAll(themeDetailList);
                        }
                    }
                    themeRepository.save(theme);
                } else {
                    errorList.add("主题(" + theme.getThemeName() + ")是待终审、驳回或锁定状态,只有终审人才能通过");
                }
            } else {
                errorList.add("主题(" + theme.getThemeName() + ")是状态[" + ThemeStatusEnum.getMessageByCode(theme.getThemeStatus()) + "]不能通过");
            }
        }
        if (errorList.size() > 0) {
            throw new BusinessException("以下记录操作失败：" + errorList.toString());
        }
    }

    private Theme getThemeNew(Theme theme) {
        Theme themeNew = new Theme();
        themeNew.setOrganizationId(theme.getOrganizationId());
        themeNew.setThemeDate(theme.getThemeDate());
        themeNew.setThemeStatus(ThemeStatusEnum.SUCCESS.getCode());
        themeNew.setRelationThemeId(theme.getId());
        themeNew.setThemeType(ThemeTypeEnum.DAILY_POINTS.getCode());
        themeNew.setSysAdminInfo();
        return themeNew;
    }

    public List<ThemeApprovalVo> findApprovalInfo(String id) {
        Theme theme = getEntityById(themeRepository, id);
        List<ThemeApprovalVo> themeApprovalVoList = new ArrayList<>();
        // 记录人
        String state1 = ThemeStatusEnum.getMessageByCode(theme.getThemeStatus());
        if (theme.getThemeStatus() > 1) {
            state1 = "已完成";
        }
        ThemeApprovalVo themeApprovalVo1 = new ThemeApprovalVo("记录人", state1, theme.getRecorderId(), theme.getRecorderName(), theme.getCreateDate());
        themeApprovalVoList.add(themeApprovalVo1);
        // 初审
        String state2 = ThemeStatusEnum.getMessageByCode(theme.getThemeStatus());
        if (theme.getThemeStatus() > 2) {
            state2 = "已完成";
        }
        ThemeApprovalVo themeApprovalVo2 = new ThemeApprovalVo("初审", state2, theme.getAttnId(), theme.getAttnName(), theme.getAttnDate());
        themeApprovalVoList.add(themeApprovalVo2);
        // 终审
        String state3 = ThemeStatusEnum.getMessageByCode(theme.getThemeStatus());
        if (theme.getThemeStatus() == 5) {
            state3 = "已完成";
        }
        ThemeApprovalVo themeApprovalVo3 = new ThemeApprovalVo("终审", state3, theme.getAuditId(), theme.getAuditName(), theme.getAuditDate());
        themeApprovalVoList.add(themeApprovalVo3);
        return themeApprovalVoList;
    }
}