package com.three.points.service;


import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.points.entity.AwardPrivilege;
import com.three.points.param.AwardPrivilegeParam;
import com.three.points.repository.AwardPrivilegeRepository;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-09-29.
 * Description:
 */

@Service
public class AwardPrivilegeService extends BaseService<AwardPrivilege,  String> {

    @Autowired
    private AwardPrivilegeRepository awardPrivilegeRepository;

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
    }

    public PageResult<AwardPrivilege> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return query(awardPrivilegeRepository, pageQuery, sort, code, searchKey, searchValue);
    }

}