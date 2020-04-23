package com.three.points.service;

import com.three.commonclient.exception.ParameterException;
import com.three.points.entity.ReportGroups;
import com.three.points.repository.ReportGroupsRepository;
import com.three.points.param.ReportGroupsParam;
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

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2020-01-11.
 * Description:
 */

@Service
public class ReportGroupsService extends BaseService<ReportGroups, String> {

    @Autowired
    private ReportGroupsRepository reportGroupsRepository;

    @Transactional
    public void create(ReportGroupsParam reportGroupsParam) {
        BeanValidator.check(reportGroupsParam);

        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();

        if (reportGroupsRepository.countByReportGroupsNameAndOrganizationId(reportGroupsParam.getReportGroupsName(), firstOrganizationId) > 0) {
            throw new ParameterException("报表分组名称[" + reportGroupsParam.getReportGroupsName() + "]已经存在");
        }

        ReportGroups reportGroups = new ReportGroups();
        reportGroups = (ReportGroups) BeanCopyUtil.copyBean(reportGroupsParam, reportGroups);

        reportGroups.setOrganizationId(firstOrganizationId);

        reportGroupsRepository.save(reportGroups);
    }

    @Transactional
    public void update(ReportGroupsParam reportGroupsParam) {
        BeanValidator.check(reportGroupsParam);

        ReportGroups reportGroups = getEntityById(reportGroupsRepository, reportGroupsParam.getId());
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        if (reportGroupsRepository.countByReportGroupsNameAndOrganizationIdAndIdNot(reportGroupsParam.getReportGroupsName(), firstOrganizationId, reportGroups.getId()) > 0) {
            throw new ParameterException("报表分组名称[" + reportGroupsParam.getReportGroupsName() + "]已经存在");
        }
        reportGroups = (ReportGroups) BeanCopyUtil.copyBean(reportGroupsParam, reportGroups);

        reportGroupsRepository.save(reportGroups);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<ReportGroups> reportGroupsList = new ArrayList<>();
        for (String id : idSet) {
            ReportGroups reportGroups = getEntityById(reportGroupsRepository, String.valueOf(id));
            reportGroups.setStatus(code);
            reportGroupsList.add(reportGroups);
        }

        reportGroupsRepository.saveAll(reportGroupsList);
    }

    public PageResult<ReportGroups> query(PageQuery pageQuery, int code, String searchValue) {
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Specification<ReportGroups> specification = (root, criteriaQuery, criteriaBuilder) -> {

            Specification<ReportGroups> codeAndOrganizationSpec = getCodeAndOrganizationSpec(code, firstOrganizationId);
            Predicate predicate = codeAndOrganizationSpec.toPredicate(root, criteriaQuery, criteriaBuilder);

            if (StringUtil.isNotBlank(searchValue)) {
                List<Predicate> predicateList1 = new ArrayList<>();
                Predicate p1 = criteriaBuilder.like(root.get("reportGroupsName"), "%" + searchValue + "%");
                predicateList1.add(criteriaBuilder.or(p1));
                Predicate[] predicates1 = new Predicate[predicateList1.size()];
                Predicate predicate1 = criteriaBuilder.or(predicateList1.toArray(predicates1));

                return criteriaQuery.where(predicate, predicate1).getRestriction();
            }
            return predicate;
        };
        if (pageQuery != null) {
            return query(reportGroupsRepository, pageQuery, sort, specification);
        } else {
            return query(reportGroupsRepository, sort, specification);
        }
    }

    public ReportGroups findById(String id) {
        return getEntityById(reportGroupsRepository, id);
    }
}