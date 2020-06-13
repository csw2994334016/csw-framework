package com.three.points.service;

import com.three.common.enums.StatusEnum;
import com.three.commonclient.exception.ParameterException;
import com.three.points.entity.CustomGroupEmp;
import com.three.points.entity.CustomReport;
import com.three.points.entity.CustomReportGroup;
import com.three.points.repository.CustomGroupEmpRepository;
import com.three.points.repository.CustomGroupRepository;
import com.three.points.repository.CustomReportGroupRepository;
import com.three.points.repository.CustomReportRepository;
import com.three.points.param.CustomReportParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.points.vo.ReportGroupVo;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2020-04-11.
 * Description:
 */

@Service
public class CustomReportService extends BaseService<CustomReport, String> {

    @Autowired
    private CustomReportRepository customReportRepository;

    @Autowired
    private CustomReportGroupRepository customReportGroupRepository;

    @Autowired
    private CustomGroupEmpRepository customGroupEmpRepository;

    @Transactional
    public void create(CustomReportParam customReportParam) {
        BeanValidator.check(customReportParam);

        String firstOrganizationId = getLoginUserFirstOrganizationId();

        if (customReportRepository.countByOrganizationIdAndReportNameAndStatus(firstOrganizationId, customReportParam.getReportName(), StatusEnum.OK.getCode()) > 0) {
            throw new ParameterException("已存在同名[" + customReportParam.getReportName() + "]的报表名称");
        }

        CustomReport customReport = new CustomReport();
        customReport = (CustomReport) BeanCopyUtil.copyBean(customReportParam, customReport);

        customReport.setOrganizationId(firstOrganizationId);

        customReport = customReportRepository.save(customReport);

        // 设置报表-分组信息
        saveCustomReportGroup(customReport, customReportParam);
    }

    @Transactional
    public void update(CustomReportParam customReportParam) {
        BeanValidator.check(customReportParam);

        String organizationId = getLoginUserFirstOrganizationId();

        CustomReport customReport = getEntityById(customReportRepository, customReportParam.getId());

        if (customReportRepository.countByOrganizationIdAndReportNameAndStatusAndIdNot(organizationId, customReportParam.getReportName(), StatusEnum.OK.getCode(), customReport.getId()) > 0) {
            throw new ParameterException("已存在同名[" + customReportParam.getReportName() + "]的报表名称");
        }

        customReport = (CustomReport) BeanCopyUtil.copyBean(customReportParam, customReport);

        customReportRepository.save(customReport);

        // 删除报表-分组信息
        customReportGroupRepository.deleteByReportId(customReport.getId());
        // 设置报表-分组信息
        saveCustomReportGroup(customReport, customReportParam);
    }

    private void saveCustomReportGroup(CustomReport customReport, CustomReportParam customReportParam) {
        if (StringUtil.isNotBlank(customReportParam.getCustomGroupIds())) {
            List<CustomReportGroup> customReportGroupList = new ArrayList<>();
            Set<String> groupIdSet = StringUtil.getStrToIdSet1(customReportParam.getCustomGroupIds());
            for (String groupId : groupIdSet) {
                CustomReportGroup customReportGroup = new CustomReportGroup();
                customReportGroup.setReportId(customReport.getId());
                customReportGroup.setGroupId(groupId);
            }
            customReportGroupRepository.saveAll(customReportGroupList);
        }
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<CustomReport> customReportList = new ArrayList<>();
        for (String id : idSet) {
            CustomReport customReport = getEntityById(customReportRepository, String.valueOf(id));
            customReport.setStatus(code);
            customReportList.add(customReport);
        }

        customReportRepository.saveAll(customReportList);
    }

    public PageResult<CustomReport> query(Integer page, Integer limit, int code, String searchValue) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<CustomReport> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Specification<CustomReport> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            Predicate predicate = codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder);
            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = new ArrayList<>();
                Predicate p1 = criteriaBuilder.like(root.get("reportName"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(new Predicate[0]));
                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (page != null && limit != null) {
            return query(customReportRepository, new PageQuery(page, limit), sort, specification);
        } else {
            return query(customReportRepository, sort, specification);
        }
    }

    public CustomReport findById(String id) {
        return getEntityById(customReportRepository, id);
    }

    public List<ReportGroupVo> findGroupsById(String id) {
        return customReportGroupRepository.findByReportId(id);
    }

    public List<CustomReport> queryMyReport(String empId) {
        String loginUserEmpId = LoginUserUtil.getLoginUserEmpId();
        if (StringUtil.isBlank(empId)) {
            empId = loginUserEmpId;
        }
        List<CustomGroupEmp> customGroupEmpList = customGroupEmpRepository.findAllByEmpId(empId);
        Set<String> groupIdSet = new HashSet<>();
        for (CustomGroupEmp customGroupEmp : customGroupEmpList) {
            groupIdSet.add(customGroupEmp.getGroupId());
        }
        List<CustomReportGroup> customReportGroupList = customReportGroupRepository.findAllByGroupIdIn(groupIdSet);
        Set<String> reportIdSet = new HashSet<>();
        for (CustomReportGroup customReportGroup : customReportGroupList) {
            reportIdSet.add(customReportGroup.getReportId());
        }
        return customReportRepository.findAllByIdIn(reportIdSet);
    }
}