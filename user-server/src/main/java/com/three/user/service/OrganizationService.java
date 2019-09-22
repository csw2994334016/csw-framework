package com.three.user.service;

import com.three.user.entity.Organization;
import com.three.user.repository.OrganizationRepository;
import com.three.user.param.OrganizationParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.ParameterException;
import com.three.commonclient.utils.BeanValidator;
import com.three.commonjpa.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by csw on 2019-09-22.
 * Description:
 */

@Service
public class OrganizationService extends BaseService<Organization,  String> {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Transactional
    public void create(OrganizationParam organizationParam) {
        BeanValidator.check(organizationParam);

        Organization organization = new Organization();
        organization = (Organization) BeanCopyUtil.copyBean(organizationParam, organization);

        organizationRepository.save(organization);
    }

    @Transactional
    public void update(OrganizationParam organizationParam) {
        BeanValidator.check(organizationParam);

        Organization organization = getEntityById(organizationRepository, organizationParam.getId());
        organization = (Organization) BeanCopyUtil.copyBean(organizationParam, organization);

        organizationRepository.save(organization);
    }

    @Transactional
    public void delete(String ids, int code) {
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);
        List<Organization> organizationList = new ArrayList<>();
        for (String id : idSet) {
            Organization organization = getEntityById(organizationRepository, String.valueOf(id));
            organization.setStatus(code);
            organizationList.add(organization);
        }

        organizationRepository.saveAll(organizationList);
    }

    public PageResult<Organization> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return query(organizationRepository, pageQuery, sort, code, searchKey, searchValue);
    }

}