package com.three.user.service;

import com.three.common.enums.AdminEnum;
import com.three.commonclient.exception.ParameterException;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import com.three.user.entity.Authority;
import com.three.user.entity.Role;
import com.three.user.entity.User;
import com.three.user.param.RoleParam;
import com.three.user.repository.AuthorityRepository;
import com.three.user.repository.RoleRepository;
import com.three.user.repository.UserRepository;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.BusinessException;
import com.three.common.utils.BeanCopyUtil;
import com.three.commonclient.utils.BeanValidator;
import com.three.common.utils.StringUtil;
import com.google.common.base.Preconditions;
import com.three.user.vo.AuthTreeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by csw on 2019/03/30.
 * Description:
 */
@Service
public class RoleService extends BaseService<Role, String> {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    public PageResult<Role> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        return query(roleRepository, pageQuery, sort, code, searchKey, searchValue);
    }

    public List<Role> findAll(int code) {
        return roleRepository.findAllByStatus(code);
    }

    @Transactional
    public void create(RoleParam param) {
        BeanValidator.check(param);

        Role role = new Role();
        role = (Role) BeanCopyUtil.copyBean(param, role);

//        role.setCreateBy(LoginUser.getLoginUser());

        roleRepository.save(role);
    }

    @Transactional
    public void update(RoleParam param) {
        Preconditions.checkNotNull(param.getId(), "修改记录Id不可以为null");

        Role role = getEntityById(roleRepository, param.getId());
        role = (Role) BeanCopyUtil.copyBean(param, role);
//        role.setUpdateBy(LoginUser.getLoginUser());

        roleRepository.save(role);
    }

    @Transactional
    public void delete(String ids, int code) {
        if (StringUtil.isBlank(ids)) {
            throw new ParameterException("删除记录的ids不可以为空");
        }
        Set<String> idSet = StringUtil.getStrToIdSet1(ids);

        List<Role> roleList = new ArrayList<>();
        for (String id : idSet) {
            Role role = getEntityById(roleRepository, id);

            Set<User> userSet = role.getUsers();
            if (userSet.size() > 0) {
                StringBuilder str = new StringBuilder();
                for (User user : userSet) {
                    str.append(user.getUsername()).append("、");
                }
                str.deleteCharAt(str.length() - 1);
                throw new BusinessException("该角色(id:" + id + ")已被用户(" + str.toString() + ")绑定，不能删除，请先解绑");
            }

            role.setStatus(code);
            roleList.add(role);
        }
        roleRepository.deleteAll(roleList);
    }

    public List<Map<String, Object>> findAuthTree(String roleId) {
        Role role = roleRepository.getOne(roleId);

        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        List<Authority> authorityList = authorityRepository.findAll(sort);

        List<Map<String, Object>> authTrees = new ArrayList<>();
        for (Authority authority : authorityList) {
            Map<String, Object> authTree = new HashMap<>();
            authTree.put("id", authority.getId());
            authTree.put("name", getAuthorityName(authority));
            authTree.put("pId", authority.getParentId());
            authTree.put("open", true);
            authTree.put("checked", role.getAuthorities().contains(authority));
            authTrees.add(authTree);
        }
        return authTrees;
    }

    private String getAuthorityName(Authority authority) {
//        if (LoginUserUtil.getLoginUser() != null && LoginUserUtil.getLoginUser().getIsAdmin() == AdminEnum.YES.getCode()) { // 超级管理员
//            return authority.getAuthorityName() + "(" + authority.getAuthorityUrl() + ")";
//        }
        return authority.getAuthorityName();
    }

    public List<AuthTreeVo> findAuthTree1(String roleId) {
        List<AuthTreeVo> authTreeVoList = new ArrayList<>();
        Role role = roleRepository.getOne(roleId);
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        List<Authority> authorityList = authorityRepository.findAll(sort);

        Map<String, AuthTreeVo> authTreeVoMap = new HashMap<>();
        for (Authority authority : authorityList) {
            if (!authority.getAuthorityUrl().contains(":/internal/")) {
                AuthTreeVo authTreeVo = new AuthTreeVo();
                authTreeVo.setId(authority.getId());
                authTreeVo.setParentId(authority.getParentId());
                authTreeVo.setTitle(authority.getAuthorityName());
                authTreeVo.setSort(authority.getSort());
                authTreeVo.setExpand(Boolean.FALSE);
                authTreeVo.setChecked(role.getAuthorities().contains(authority));
                authTreeVoMap.put(authTreeVo.getId(), authTreeVo);
                if ("-1".equals(authTreeVo.getParentId())) {
                    authTreeVoList.add(authTreeVo);
                }
            }
        }
        for (Map.Entry<String, AuthTreeVo> entry : authTreeVoMap.entrySet()) {
            if (!"-1".equals(entry.getValue().getParentId())) {
                AuthTreeVo voParent = authTreeVoMap.get(entry.getValue().getParentId());
                voParent.getChildren().add(entry.getValue());
            }
        }
        authTreeVoList.sort(Comparator.comparing(AuthTreeVo::getSort));
        for (AuthTreeVo vo : authTreeVoList) {
            vo.setExpand(Boolean.TRUE);
            vo.getChildren().sort(Comparator.comparing(AuthTreeVo::getSort));
        }
        return authTreeVoList;
    }

    @Transactional
    public void assignRoleAuth(String roleId, String authIds) {
        Role role = getEntityById(roleRepository, roleId);

        if (StringUtil.isBlank(authIds)) { // 没有绑定权限
            role.setAuthorities(new HashSet<>());
        } else { // 绑定新的权限
            Set<Authority> authoritySet = getAuthoritySet(authIds);
            role.setAuthorities(authoritySet);
        }

        roleRepository.save(role);
    }

    private Set<Authority> getAuthoritySet(String authIds) {
        String[] authIdArray = StringUtils.split(authIds, ",");
        Set<String> authIdSet = StringUtil.getStringArrayToIdSet1(authIdArray);
        if (authIdSet.size() > 0) {
            return new HashSet<>(authorityRepository.findAllById(authIdSet));
        } else {
            return new HashSet<>();
        }
    }

    @Transactional
    public void assignRoleUser(String roleId, String userIds) {
        Role role = getEntityById(roleRepository, roleId);

        Set<String> userIdSet = StringUtil.getStrToIdSet1(userIds);
        Set<User> userSet = new HashSet<>(userRepository.findAllById(userIdSet));

        // 用户删除该角色
        userSet.forEach(user -> user.getRoles().remove(role));

        userRepository.saveAll(userSet);
    }

    public Role getEntityById(String roleId) {
        return getEntityById(roleRepository, roleId);
    }

}
