package com.three.user.service;

import com.three.common.utils.StringUtil;
import com.three.user.entity.Authority;
import com.three.user.entity.Role;
import com.three.common.enums.AuthorityEnum;
import com.three.user.param.AuthorityParam;
import com.three.user.repository.AuthorityRepository;
import com.three.common.enums.StatusEnum;
import com.three.common.exception.BusinessException;
import com.three.common.utils.BeanCopyUtil;
import com.three.common.utils.BeanValidator;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.common.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by csw on 2019/03/31.
 * Description:
 */
@Service
public class AuthorityService extends BaseService<Authority, String> {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Transactional
    public void create(AuthorityParam authorityParam) {
        BeanValidator.check(authorityParam);

        Authority authority = new Authority();
        authority = (Authority) BeanCopyUtil.copyBean(authorityParam, authority);

        // 设置权限parentIds
        authority.setParentIds(setParentIds(authorityParam));

        authorityRepository.save(authority);
    }

    @Transactional
    public void update(AuthorityParam authorityParam) {
        BeanValidator.check(authorityParam);

        Authority authority = getEntityById(authorityRepository, authorityParam.getId());
        authority = (Authority) BeanCopyUtil.copyBean(authorityParam, authority);

        // 设置权限parentIds
        authority.setParentIds(setParentIds(authorityParam));

        authorityRepository.save(authority);
    }

    private String setParentIds(AuthorityParam authorityParam) {
        String parentIds = null;
        if (StringUtil.isNotBlank(authorityParam.getParentId()) && !"-1".equals(authorityParam.getParentId())) {
            Authority parent = getEntityById(authorityRepository, authorityParam.getParentId());
            parentIds = StringUtil.isNotBlank(parent.getParentIds()) ? parent.getParentIds() + "," + parent.getId() : parent.getId();
        }
        return parentIds;
    }

    @Transactional
    public void delete(String id, int code) {
        Authority authority = getEntityById(authorityRepository, id);

        Set<Role> roleSet = authority.getRoles();
        if (roleSet.size() > 0) {
            StringBuilder str = new StringBuilder();
            for (Role role : roleSet) {
                str.append(role.getRoleName()).append("、");
            }
            str.deleteCharAt(str.length() - 1);
            throw new BusinessException("该权限(id:" + id + ")已被角色(" + str.toString() + ")绑定，不能删除，请先解绑");
        }

        authority.setStatus(code);
        List<Authority> authorityList = authorityRepository.findAllByParentId(authority.getId());
        for (Authority authority1 : authorityList) {
            authority1.setStatus(code);
        }

        authorityList.add(authority);

        authorityRepository.deleteAll(authorityList);
    }

    @Transactional
    public void sync(List<Authority> authorityList) {
        int num = 3;
        for (Authority authority : authorityList) {
            Authority authorityParent = authorityRepository.findByAuthorityName(authority.getParentName());
            if (authorityParent == null) { // 生成父权限节点，菜单
                Authority authorityMenu = new Authority();
                authorityMenu.setAuthorityName(authority.getParentName());
                authorityMenu.setAuthorityType(AuthorityEnum.MENU.getCode());
                authorityMenu.setSort(num);
                num = num + 3;
                authorityMenu.setParentId("-1");
                authorityParent = authorityRepository.save(authorityMenu);
            }
            Authority authorityNew = authorityRepository.findByAuthorityNameOrAuthorityUrl(authority.getAuthorityName(), authority.getAuthorityUrl());
            if (authorityNew != null) { // 根据权限名称或者url修改原有的权限
                authorityNew.setAuthorityName(authority.getAuthorityName());
                authorityNew.setAuthorityUrl(authority.getAuthorityUrl());
                authorityNew.setParentId(authorityParent.getId());
                authorityRepository.save(authorityNew);
            } else { // 生成新的权限节点，按钮
                authority.setAuthorityType(AuthorityEnum.BUTTON.getCode());
                authority.setSort(num);
                num = num + 3;
                authority.setParentId(authorityParent.getId());
                authorityRepository.save(authority);
            }
        }
    }

    public PageResult<Authority> findAll(int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        return query(authorityRepository, sort, code, searchKey, searchValue);
    }

    public List<Authority> findMenuAuth() {
        return authorityRepository.findAllByStatusAndAuthorityType(StatusEnum.OK.getCode(), AuthorityEnum.MENU.getCode());
    }

    public List<Authority> findAllAuthTree(int code, String authorityName) {
        List<Authority> authTreeVoList = new ArrayList<>();
        List<Authority> authorityList;
        if (StringUtil.isNotBlank(authorityName)) {
            authorityList = authorityRepository.findAllByStatusAndAuthorityNameLike(code, "%" + authorityName + "%");
        } else {
            authorityList = authorityRepository.findAllByStatus(code);
        }

        Map<String, Authority> authTreeVoMap = new HashMap<>();
        for (Authority authority : authorityList) {
            if (!authority.getAuthorityUrl().contains(":/internal/")) {
                authTreeVoMap.put(authority.getId(), authority);
                if ("-1".equals(authority.getParentId())) {
                    authTreeVoList.add(authority);
                }
            }
        }
        for (Map.Entry<String, Authority> entry : authTreeVoMap.entrySet()) {
            if (!"-1".equals(entry.getValue().getParentId())) {
                Authority voParent = authTreeVoMap.get(entry.getValue().getParentId());
                voParent.getChildren().add(entry.getValue());
            }
        }
        sortAuthTreeVoList(authTreeVoList);
        return authTreeVoList;
    }

    private void sortAuthTreeVoList(List<Authority> authTreeVoList) {
        authTreeVoList.sort(Comparator.comparing(Authority::getSort));
        for (Authority authority : authTreeVoList) {
            sortAuthTreeVoList(authority.getChildren());
        }
    }
}
