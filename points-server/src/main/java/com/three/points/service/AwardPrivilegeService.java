package com.three.points.service;


import com.three.common.enums.StatusEnum;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.points.entity.AwardPrivilege;
import com.three.points.entity.AwardPrivilegeEmp;
import com.three.points.param.AwardPrivilegeEmpParam;
import com.three.points.param.AwardPrivilegeParam;
import com.three.points.repository.AwardPrivilegeEmpRepository;
import com.three.points.repository.AwardPrivilegeRepository;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */

@Service
public class AwardPrivilegeService extends BaseService<AwardPrivilege, String> {

    @Autowired
    private AwardPrivilegeRepository awardPrivilegeRepository;

    @Autowired
    private AwardPrivilegeEmpRepository awardPrivilegeEmpRepository;

    @Transactional
    public void create(AwardPrivilegeParam awardPrivilegeParam) {
        BeanValidator.check(awardPrivilegeParam);

        AwardPrivilege awardPrivilege = new AwardPrivilege();
        awardPrivilege = (AwardPrivilege) BeanCopyUtil.copyBean(awardPrivilegeParam, awardPrivilege);

        awardPrivilege.setOrganizationId(LoginUserUtil.getLoginUserFirstOrganizationId());

        awardPrivilegeRepository.save(awardPrivilege);
    }

    @Transactional
    public void update(AwardPrivilegeParam awardPrivilegeParam) {
        BeanValidator.check(awardPrivilegeParam);

        AwardPrivilege awardPrivilege = getEntityById(awardPrivilegeRepository, awardPrivilegeParam.getId());
        awardPrivilege = (AwardPrivilege) BeanCopyUtil.copyBean(awardPrivilegeParam, awardPrivilege);

        awardPrivilegeRepository.save(awardPrivilege);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<AwardPrivilege> awardPrivilegeList = new ArrayList<>();
        for (String id : idSet) {
            AwardPrivilege awardPrivilege = getEntityById(awardPrivilegeRepository, String.valueOf(id));
            awardPrivilege.setStatus(code);
            awardPrivilegeList.add(awardPrivilege);
        }

        awardPrivilegeRepository.saveAll(awardPrivilegeList);

        for (AwardPrivilege awardPrivilege : awardPrivilegeList) {
            awardPrivilegeEmpRepository.deleteByAwardPrivilegeId(awardPrivilege.getId());
        }
    }

    public PageResult<AwardPrivilege> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        if (pageQuery != null) {
            return query(awardPrivilegeRepository, pageQuery, sort, code, searchKey, searchValue);
        } else {
            return query(awardPrivilegeRepository, sort, code, searchKey, searchValue);
        }
    }

    @Transactional
    public void bindEmployee(AwardPrivilegeEmpParam awardPrivilegeEmpParam) {
        BeanValidator.check(awardPrivilegeEmpParam);

        AwardPrivilege awardPrivilege = getEntityById(awardPrivilegeRepository, awardPrivilegeEmpParam.getAwardPrivilegeId());

        List<AwardPrivilegeEmp> awardPrivilegeEmpList = new ArrayList<>();
        for (String employeeId : awardPrivilegeEmpParam.getEmployeeIdList()) {
            AwardPrivilegeEmp awardPrivilegeEmp = new AwardPrivilegeEmp();
            awardPrivilegeEmp.setAwardPrivilegeId(awardPrivilegeEmpParam.getAwardPrivilegeId());
            awardPrivilegeEmp.setEmpId(employeeId);
            awardPrivilegeEmpList.add(awardPrivilegeEmp);
        }

        // 修改人数
        awardPrivilege.setEmpNum(awardPrivilegeEmpList.size());
        awardPrivilegeRepository.save(awardPrivilege);

        awardPrivilegeEmpRepository.deleteByAwardPrivilegeId(awardPrivilegeEmpParam.getAwardPrivilegeId());
        if (awardPrivilegeEmpList.size() > 0) {
            awardPrivilegeEmpRepository.saveAll(awardPrivilegeEmpList);
        }
    }

    public Set<String> findAuditor(String attnOrAuditFlag, String attnId, Integer aPosScoreMax, Integer aNegScoreMin, Integer bPosScoreMax, Integer bNegScoreMin) {
        Set<String> empIdSet = new HashSet<>();
        String firstOrganizationId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Set<String> awardPrivilegeIdSet;
        if ("0".equals(attnOrAuditFlag)) { // 查找初审人：有奖扣权限的人员
            awardPrivilegeIdSet = awardPrivilegeRepository.findAllByOrganizationIdAndStatus(firstOrganizationId, StatusEnum.OK.getCode());
        } else { // 查找终审人：
            int aScore = Math.max(aPosScoreMax != null ? aPosScoreMax : 0, aNegScoreMin != null ? Math.abs(aNegScoreMin) : 0);
            int bScore = Math.max(bPosScoreMax != null ? bPosScoreMax : 0, bNegScoreMin != null ? Math.abs(bNegScoreMin) : 0);
            awardPrivilegeIdSet = awardPrivilegeRepository.findAllByAScoreGreaterThanEqualAndBScoreGreaterThanEqualAndOrganizationIdAndStatus(aScore, bScore, firstOrganizationId, StatusEnum.OK.getCode());
        }
        if (awardPrivilegeIdSet.size() > 0) {
            List<AwardPrivilegeEmp> awardPrivilegeEmpList;
            if (StringUtil.isNotBlank(attnId)) {
                awardPrivilegeEmpList = awardPrivilegeEmpRepository.findAllByAwardPrivilegeIdInAndEmpIdNot(awardPrivilegeIdSet, attnId);
            } else {
                awardPrivilegeEmpList = awardPrivilegeEmpRepository.findAllByAwardPrivilegeIdIn(awardPrivilegeIdSet);
            }
            awardPrivilegeEmpList.forEach(e -> empIdSet.add(e.getEmpId()));
        }
        return empIdSet;
    }
}