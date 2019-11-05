package com.three.points.service;

import com.three.common.auth.LoginUser;
import com.three.commonclient.exception.BusinessException;
import com.three.points.enums.EventEnum;
import com.three.points.enums.ThemeEnum;
import com.three.points.entity.Theme;
import com.three.points.entity.ThemeDetail;
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
import com.three.commonclient.utils.BeanValidator;
import com.three.points.vo.ThemeApprovalVo;
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

    @Autowired
    private UserClient userClient;

    @Transactional
    public void createDraft(ThemeParam themeParam) {
        // 创建主题
        Theme theme = new Theme();

        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        theme.setThemeStatus(ThemeEnum.DRAFT.getCode());

        saveThemeAndThemeDetailList("draft", theme, themeDetailList);
    }

    @Transactional
    public void create(ThemeParam themeParam) {
        BeanValidator.check(themeParam);
        checkAttnIdAndAuditId(themeParam);

        Theme theme = new Theme();

        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        theme.setThemeStatus(ThemeEnum.ATTN.getCode());

        saveThemeAndThemeDetailList(null, theme, themeDetailList);
    }

    private void saveThemeAndThemeDetailList(String draft, Theme theme, List<ThemeDetail> themeDetailList) {
        theme.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());
        String empId = LoginUserUtil.getLoginUserEmpId();
        String empFullName = LoginUserUtil.getLoginUserEmpFullName();
        theme.setRecorderId(empId);
        theme.setRecorderName(empFullName);
        if (!"draft".equals(draft)) {
            theme.setSubmitterId(empId);
            theme.setSubmitterName(empFullName);
            theme.setSubmitterDate(new Date());
            if (theme.getAttnId().equals(theme.getRecorderId())) {
                theme.setThemeStatus(ThemeEnum.AUDIT.getCode());
            }
        }

        theme = themeRepository.save(theme);
        for (ThemeDetail themeDetail : themeDetailList) {
            themeDetail.setThemeId(theme.getId());
            themeDetail.setOrganizationId(theme.getOrganizationId());
        }
        themeDetailRepository.saveAll(themeDetailList);
    }

    @Transactional
    public void update(ThemeParam themeParam) {
        BeanValidator.check(themeParam);
        checkAttnIdAndAuditId(themeParam);

        Theme theme = getEntityById(themeRepository, themeParam.getId());
        if (theme.getThemeStatus() != ThemeEnum.DRAFT.getCode() && theme.getThemeStatus() != ThemeEnum.SAVE.getCode()) {
            throw new BusinessException("记录只有在草稿或保存状态下才能编辑");
        }

        List<ThemeDetail> themeDetailList = new ArrayList<>();
        createTheme(theme, themeDetailList, themeParam);

        String empId = LoginUserUtil.getLoginUserEmpId();
        String empFullName = LoginUserUtil.getLoginUserEmpFullName();
        theme.setSubmitterId(empId);
        theme.setSubmitterName(empFullName);
        theme.setSubmitterDate(new Date());
        if (theme.getAttnId().equals(theme.getRecorderId())) {
            theme.setThemeStatus(ThemeEnum.AUDIT.getCode());
        }

        // 删除原有记录
        themeDetailRepository.deleteByThemeId(theme.getId());

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

        if (empId != null && empId.equals(theme.getAuditId())) {
            throw new BusinessException("终审人不能是记录人");
        }

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
            if (themeDetail.getEmpId().equals(theme.getAuditId())) {
                throw new BusinessException("终审人不能是奖扣对象");
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
                                   Long recordDateSt, Long recordDateEt, Long themeDateSt, Long themeDateEt,
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
                                           Long recordDateSt, Long recordDateEt, Long themeDateSt, Long themeDateEt,
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
                    predicateList.add(criteriaBuilder.like(root.get("copyPersonId"), "%" + loginUserEmpId + "%"));
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
    public void submit(String id) {
        Theme theme = getEntityById(themeRepository, id);
        if (theme.getThemeStatus() == ThemeEnum.SAVE.getCode()) { // 只有保存状态才能提交
            // 当前用户是初审人,则直接到待终审状态
            theme.setThemeStatus(ThemeEnum.ATTN.getCode());
            String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
            if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                theme.setThemeStatus(ThemeEnum.AUDIT.getCode());
            }
            theme.setSubmitterId(loginUserEmpId);
            theme.setSubmitterName(LoginUserUtil.getLoginUserEmpFullName());
            theme.setSubmitterDate(new Date());
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
                    throw new BusinessException("记录是待初审状态,只有记录人才能撤回");
                }
            } else if (theme.getThemeStatus() == ThemeEnum.AUDIT.getCode()) { // 记录是待终审状态,只有当前用户是初审人才能撤回
                if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeEnum.ATTN.getCode());
                    themeRepository.save(theme);
                } else {
                    throw new BusinessException("记录是待终审状态,只有初审人才能撤回");
                }
            } else if (theme.getThemeStatus() == ThemeEnum.REJECT.getCode()) { // 记录是驳回状态,只有当前用户是初审人或记录人才能撤回
                if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeEnum.ATTN.getCode());
                    themeRepository.save(theme);
                }
                if (StringUtil.isNotBlank(theme.getRecorderId()) && theme.getRecorderId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeEnum.SAVE.getCode());
                    themeRepository.save(theme);
                } else {
                    throw new BusinessException("记录是驳回状态,只有初审人或记录人才能撤回");
                }
            } else if (theme.getThemeStatus() == ThemeEnum.SUCCESS.getCode()) { // 记录是审核通过状态,只有当前用户是终审人才能撤回
                if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                    theme.setThemeStatus(ThemeEnum.LOCK.getCode());
                    // 撤回之前，要删除相应的奖分记录
                    Theme theme1 = themeRepository.findByRelationThemeIdAndThemeName(theme.getId(), EventEnum.AUDIT_POS_THEME.getMessage());
                    themeRepository.delete(theme1);
                    themeDetailRepository.deleteByThemeId(theme1.getId());

                    themeRepository.save(theme);
                } else {
                    throw new BusinessException("记录是审核通过状态,只有终审人才能撤回");
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
        if (theme.getThemeStatus() == ThemeEnum.ATTN.getCode() || theme.getThemeStatus() == ThemeEnum.REJECT.getCode()) { // 记录是待初审或驳回状态,只有当前用户是初审人才能驳回
            if (StringUtil.isNotBlank(theme.getAttnId()) && theme.getAttnId().equals(loginUserEmpId)) {
                theme.setThemeStatus(ThemeEnum.SAVE.getCode());
                theme.setAttnOpinion(opinion);
                themeRepository.save(theme);
            } else {
                throw new BusinessException("记录是待初审或驳回状态,只有初审人才能驳回");
            }
        } else if (theme.getThemeStatus() == ThemeEnum.AUDIT.getCode() || theme.getThemeStatus() == ThemeEnum.LOCK.getCode()) { // 记录是待终审或锁定状态,只有当前用户是终审人才能驳回
            if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                theme.setThemeStatus(ThemeEnum.ATTN.getCode());
                theme.setAuditOpinion(opinion);
                // 只有终审人驳回才能进行扣分操作
                if ((recorderBScore != null && recorderBScore < 0) || (attnBScore != null && attnBScore < 0)) {
                    // 先生成主题
                    Theme themeNew = getThemeNew(theme);
                    themeNew.setThemeName(EventEnum.AUDIT_NEG_THEME.getMessage());
                    // 汇总分、人次
                    if (recorderBScore != null && recorderBScore < 0) {
                        themeNew.setBPosScore(themeNew.getBPosScore() + recorderBScore);
                        themeNew.setEmpCount(themeNew.getEmpCount() + 1);
                    }
                    if (attnBScore != null && attnBScore < 0) {
                        themeNew.setBPosScore(themeNew.getBPosScore() + attnBScore);
                        themeNew.setEmpCount(themeNew.getEmpCount() + 1);
                    }
                    themeNew = themeRepository.save(themeNew);
                    // 再生成详情
                    List<ThemeDetail> themeDetailList = new ArrayList<>();
                    if (recorderBScore != null && recorderBScore < 0) {
                        ThemeDetail themeDetail = new ThemeDetail(themeNew.getOrganizationId(), themeNew.getId(), themeNew.getThemeName(), themeNew.getThemeDate(), EventEnum.AUDIT_NEG_TYPE.getMessage());
                        themeDetail.setEventName(EventEnum.RECORDER_NEG.getMessage());
                        themeDetail.setEmpId(theme.getRecorderId());
                        themeDetail.setEmpFullName(theme.getRecorderName());
                        themeDetail.setBScore(recorderBScore);
                        themeDetail.setRemark(EventEnum.RECORDER_NEG.getMessage());
                        themeDetailList.add(themeDetail);
                    }
                    if (attnBScore != null && attnBScore < 0) {
                        ThemeDetail themeDetail = new ThemeDetail(themeNew.getOrganizationId(), themeNew.getId(), themeNew.getThemeName(), themeNew.getThemeDate(), EventEnum.AUDIT_NEG_TYPE.getMessage());
                        themeDetail.setEventName(EventEnum.ATTN_NEG.getMessage());
                        themeDetail.setEmpId(theme.getAttnId());
                        themeDetail.setEmpFullName(theme.getAttnName());
                        themeDetail.setBScore(attnBScore);
                        themeDetail.setRemark(EventEnum.ATTN_NEG.getMessage());
                        themeDetailList.add(themeDetail);
                    }
                    if (themeDetailList.size() > 0) {
                        themeDetailRepository.saveAll(themeDetailList);
                    }
                }
                themeRepository.save(theme);
            } else {
                throw new BusinessException("记录是待终审或锁定状态,只有终审人才能驳回");
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
                theme.setAttnDate(new Date());
                themeRepository.save(theme);
            } else {
                throw new BusinessException("记录是待初审状态,只有初审人才能通过");
            }
        } else if (theme.getThemeStatus() == ThemeEnum.AUDIT.getCode() || theme.getThemeStatus() == ThemeEnum.REJECT.getCode() || theme.getThemeStatus() == ThemeEnum.LOCK.getCode()) { // 记录是待终审、驳回或锁定状态,只有当前用户是终审人才能通过
            if (StringUtil.isNotBlank(theme.getAuditId()) && theme.getAuditId().equals(loginUserEmpId)) {
                theme.setThemeStatus(ThemeEnum.SUCCESS.getCode());
                theme.setAuditOpinion(opinion);
                theme.setAuditDate(new Date());
                // 只有终审人通过才能进行奖分操作，通过新增一条主题详情记录实现对记录人或初审人的加分
                if ((recorderBScore != null && recorderBScore > 0) || (attnBScore != null && attnBScore > 0)) {
                    // 先生成主题
                    Theme themeNew = getThemeNew(theme);
                    themeNew.setThemeName(EventEnum.AUDIT_POS_THEME.getMessage());
                    // 汇总分、人次
                    if (recorderBScore != null && recorderBScore > 0) {
                        themeNew.setBPosScore(themeNew.getBPosScore() + recorderBScore);
                        themeNew.setEmpCount(themeNew.getEmpCount() + 1);
                    }
                    if (attnBScore != null && attnBScore > 0) {
                        themeNew.setBPosScore(themeNew.getBPosScore() + attnBScore);
                        themeNew.setEmpCount(themeNew.getEmpCount() + 1);
                    }
                    themeNew = themeRepository.save(themeNew);
                    // 再生成详情
                    List<ThemeDetail> themeDetailList = new ArrayList<>();
                    if (recorderBScore != null && recorderBScore > 0) {
                        ThemeDetail themeDetail = new ThemeDetail(themeNew.getOrganizationId(), themeNew.getId(), themeNew.getThemeName(), themeNew.getThemeDate(), EventEnum.AUDIT_POS_TYPE.getMessage());
                        themeDetail.setEventName(EventEnum.RECORDER_POS.getMessage());
                        themeDetail.setEmpId(theme.getRecorderId());
                        themeDetail.setEmpFullName(theme.getRecorderName());
                        themeDetail.setBScore(recorderBScore);
                        themeDetail.setRemark(EventEnum.RECORDER_POS.getMessage());
                        themeDetailList.add(themeDetail);
                    }
                    if (attnBScore != null && attnBScore > 0) {
                        ThemeDetail themeDetail = new ThemeDetail(themeNew.getOrganizationId(), themeNew.getId(), themeNew.getThemeName(), themeNew.getThemeDate(), EventEnum.AUDIT_POS_TYPE.getMessage());
                        themeDetail.setEventName(EventEnum.ATTN_POS.getMessage());
                        themeDetail.setEmpId(theme.getAttnId());
                        themeDetail.setEmpFullName(theme.getAttnName());
                        themeDetail.setBScore(attnBScore);
                        themeDetail.setRemark(EventEnum.ATTN_POS.getMessage());
                        themeDetailList.add(themeDetail);
                    }
                    if (themeDetailList.size() > 0) {
                        themeDetailRepository.saveAll(themeDetailList);
                    }
                }
                themeRepository.save(theme);
            } else {
                throw new BusinessException("记录是待终审、驳回或锁定状态,只有终审人才能通过");
            }
        } else {
            throw new BusinessException("该状态[" + theme.getThemeStatus() + "]不能通过");
        }
    }

    private Theme getThemeNew(Theme theme) {
        LoginUser sysUser = userClient.findByAdmin();
        Theme themeNew = new Theme();
        themeNew.setOrganizationId(theme.getOrganizationId());
        themeNew.setThemeDate(theme.getThemeDate());
        themeNew.setThemeStatus(ThemeEnum.SUCCESS.getCode());
        themeNew.setRelationThemeId(theme.getId());
        themeNew.setRecorderId(sysUser.getId());
        themeNew.setRecorderName(sysUser.getFullName());
        themeNew.setSubmitterId(sysUser.getId());
        themeNew.setSubmitterName(sysUser.getFullName());
        Date date = new Date();
        themeNew.setSubmitterDate(date);
        themeNew.setAttnId(sysUser.getId());
        themeNew.setAttnName(sysUser.getFullName());
        themeNew.setAttnDate(date);
        themeNew.setAuditId(sysUser.getId());
        themeNew.setAuditName(sysUser.getFullName());
        themeNew.setAuditDate(date);
        return themeNew;
    }

    public List<ThemeApprovalVo> findApprovalInfo(String id) {
        Theme theme = getEntityById(themeRepository, id);
        List<ThemeApprovalVo> themeApprovalVoList = new ArrayList<>();
        // 记录人
        String state1 = ThemeEnum.getMessageByCode(theme.getThemeStatus());
        if (theme.getThemeStatus() > 1) {
            state1 = "已完成";
        }
        ThemeApprovalVo themeApprovalVo1 = new ThemeApprovalVo("记录人", state1, theme.getRecorderId(), theme.getRecorderName(), theme.getCreateDate());
        themeApprovalVoList.add(themeApprovalVo1);
        // 初审
        String state2 = ThemeEnum.getMessageByCode(theme.getThemeStatus());
        if (theme.getThemeStatus() > 2) {
            state2 = "已完成";
        }
        ThemeApprovalVo themeApprovalVo2 = new ThemeApprovalVo("初审", state2, theme.getAttnId(), theme.getAttnName(), theme.getAttnDate());
        themeApprovalVoList.add(themeApprovalVo2);
        // 终审
        String state3 = ThemeEnum.getMessageByCode(theme.getThemeStatus());
        if (theme.getThemeStatus() == 5) {
            state3 = "已完成";
        }
        ThemeApprovalVo themeApprovalVo3 = new ThemeApprovalVo("终审", state3, theme.getAuditId(), theme.getAuditName(), theme.getAuditDate());
        themeApprovalVoList.add(themeApprovalVo3);
        return themeApprovalVoList;
    }
}