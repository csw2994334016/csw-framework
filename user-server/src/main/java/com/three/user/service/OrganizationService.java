package com.three.user.service;

import com.three.user.entity.Organization;
import com.three.user.repository.OrganizationRepository;
import com.three.user.param.OrganizationParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.commonjpa.base.service.BaseService;
import com.three.user.vo.OrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * Created by csw on 2019-09-25.
 * Description:
 */

@Service
public class OrganizationService extends BaseService<Organization, String> {

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

    public PageResult<Organization> findAll(int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        return findAll(organizationRepository, sort, code, searchKey, searchValue);
    }

    public List<OrgVo> findAllWithTree(int code) {
        List<Organization> organizationList = organizationRepository.findAllByStatus(code);

        List<OrgVo> parentList = new ArrayList<>();
        List<OrgVo> orgVoList = new ArrayList<>();
        for (Organization organization : organizationList) {
            OrgVo orgVo = OrgVo.builder().title(organization.getOrgName()).id(organization.getId()).parentId(organization.getParentId()).build();
            orgVoList.add(orgVo);
            if (orgVo.getParentId().equals("-1")) { // 根节点
                parentList.add(orgVo);
            }
        }
        for (OrgVo parent : parentList) {
            generateTree(parent, orgVoList);
        }
        // 排序
        return parentList;
    }

    private void generateTree(OrgVo parent, List<OrgVo> orgVoList) {
        for (OrgVo orgVo : orgVoList) {
            if (parent.getId().equals(orgVo.getParentId())) {
                generateTree(orgVo, orgVoList);
                parent.getChildren().add(orgVo);
            }
        }
    }
}