package com.three.user.service;

import com.three.common.auth.LoginUser;
import com.three.common.enums.AdminEnum;
import com.three.common.enums.StatusEnum;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import com.three.user.entity.Organization;
import com.three.user.repository.OrganizationRepository;
import com.three.user.param.OrganizationParam;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.StringUtil;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.user.vo.OrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.Predicate;
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
        String firstParentId = LoginUserUtil.getLoginUserFirstOrganizationId();
        Specification<Organization> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            Specification<Organization> codeAndSearchKeySpec = getCodeAndSearchKeySpec(code, searchKey, searchValue);
            if (firstParentId != null) { // 根据当前登录用户，查找该公司的所有组织机构
                predicateList.add(criteriaBuilder.equal(root.get("firstParentId"), firstParentId));
            }
            predicateList.add(codeAndSearchKeySpec.toPredicate(root, criteriaQuery, criteriaBuilder));
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        };
        if (pageQuery != null) {
            return query(organizationRepository, pageQuery, sort, specification);
        } else {
            return query(organizationRepository, sort, specification);
        }

    }

    public PageResult<Organization> findAll(int code, String searchKey, String searchValue) {
        return query(null, code, searchKey, searchValue);
    }

    public List<OrgVo> findAllWithTree(int code) {
        List<Organization> organizationList = new ArrayList<>();

        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser != null) {
            if (loginUser.getSysOrganization() != null) {
                organizationList = organizationRepository.findAllByFirstParentIdAndStatus(loginUser.getSysOrganization().getFirstParentId(), code);
            } else {
                if (loginUser.getIsAdmin().equals(AdminEnum.YES.getCode())) {
                    organizationList = organizationRepository.findAllByStatus(code); // 默认admin账号可以查看全库所有组织机构
                }
            }
        }

        Map<String, OrgVo> orgVoMap = new HashMap<>();
        for (Organization organization : organizationList) {
            OrgVo orgVo = OrgVo.builder().title(organization.getOrgName()).id(organization.getId()).parentId(organization.getParentId()).sort(organization.getSort()).build();
            orgVoMap.put(orgVo.getId(), orgVo);
        }
        List<OrgVo> parentOrgVoList = new ArrayList<>();
        for (OrgVo orgVo : orgVoMap.values()) {
            OrgVo parentOrg = orgVoMap.get(orgVo.getParentId());
            if (parentOrg != null) {
                parentOrg.getChildren().add(orgVo);
            } else {
                parentOrgVoList.add(orgVo);
            }
        }
        // 排序
        sortBySort(parentOrgVoList);
        return parentOrgVoList;
    }

    private void sortBySort(List<OrgVo> orgVoList) {
        orgVoList.sort(Comparator.comparing(OrgVo::getSort));
        for (OrgVo orgVo : orgVoList) {
            if (orgVo.getChildren().size() > 0) {
                sortBySort(orgVo.getChildren());
            }
        }
    }

    Organization getEntityById(String organizationId) {
        return getEntityById(organizationRepository, organizationId);
    }

    List<Organization> getChildOrganizationListByOrgId(String orgId) {
        List<Organization> childOrganizationList = new ArrayList<>();
        List<Organization> organizationList = findAll(StatusEnum.OK.getCode(), null, null).getData();
        Map<String, Organization> organizationMap = new HashMap<>();
        for (Organization organization : organizationList) {
            organizationMap.put(organization.getId(), organization);
        }
        for (Organization organization : organizationList) {
            Organization parentOrg = organizationMap.get(organization.getParentId());
            if (parentOrg != null) {
                parentOrg.getChildren().add(organization);
            }
        }
        getChildren(orgId, organizationMap, childOrganizationList);
        childOrganizationList.add(organizationMap.get(orgId));
        return childOrganizationList;
    }

    private void getChildren(String parentId, Map<String, Organization> organizationMap, List<Organization> organizationList) {
        for (Organization organization : organizationMap.get(parentId).getChildren()) {
            organizationList.add(organization);
            getChildren(organization.getId(), organizationMap, organizationList);
        }
    }
}