package com.three.user.service;

import com.three.common.auth.SysAuthority;
import com.three.common.auth.LoginUser;
import com.three.commonclient.exception.BusinessException;
import com.three.resource_security.utils.LoginUserUtil;
import com.three.user.entity.Role;
import com.three.user.entity.User;
import com.three.user.param.UserParam;
import com.three.user.repository.RoleRepository;
import com.three.user.repository.UserRepository;
import com.three.user.vo.MenuVo;
import com.three.common.enums.AuthorityEnum;
import com.three.commonjpa.base.service.BaseService;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.commonclient.exception.ParameterException;
import com.three.common.utils.BeanCopyUtil;
import com.three.commonclient.utils.BeanValidator;
import com.three.common.utils.StringUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.util.*;

/**
 * Created by csw on 2019/03/22.
 * Description:
 */
@Service
public class UserService extends BaseService<User, String> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public void create(UserParam userParam) {
        BeanValidator.check(userParam);

        User user = new User();
        user = (User) BeanCopyUtil.copyBean(userParam, user);

        String finalSecret = new BCryptPasswordEncoder().encode("123456");
        user.setPassword(finalSecret);

        // 添加角色
        Set<Role> roleSet = getRoleSet(userParam.getRoleIds());
        user.setRoles(roleSet);

        userRepository.save(user);
    }

    @Transactional
    public void update(UserParam userParam) {
        BeanValidator.check(userParam);

        User user = getEntityById(userRepository, userParam.getId());
        user = (User) BeanCopyUtil.copyBean(userParam, user);

        // 修改角色
        Set<Role> roleSet = getRoleSet(userParam.getRoleIds());
        user.setRoles(roleSet);

        userRepository.save(user);
    }

    private Set<Role> getRoleSet(String roleIds) {
        Set<String> roleIdSet = StringUtil.getStrToIdSet1(roleIds);
        return new HashSet<>(roleRepository.findAllById(roleIdSet));
    }

    public PageResult<User> query(PageQuery pageQuery, int code, String searchKey, String searchValue) {
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        return query(userRepository, pageQuery, sort, code, searchKey, searchValue);
    }

    public PageResult<User> findByRole(PageQuery pageQuery, int code, String searchKey, String searchValue, String roleId) {
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = Lists.newArrayList();
            SetJoin<User, Role> roles = root.join(root.getModel().getSet("roles", Role.class), JoinType.LEFT);
            predicateList.add(roles.in(getEntityById(roleRepository, roleId)));
            Specification<User> codeAndSearchKeySpec = getCodeAndSearchKeySpec(code, searchKey, searchValue);
            predicateList.add(codeAndSearchKeySpec.toPredicate(root, criteriaQuery, criteriaBuilder));
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(predicates));
        };
        return query(userRepository, pageQuery, sort, specification);
    }

    @Transactional
    public void assignRole(UserParam userParam) {
        if (StringUtil.isBlank(userParam.getRoleIds())) {
            throw new ParameterException("角色不可以为空，至少选择一个默认角色");
        }

        User user = getEntityById(userRepository, userParam.getId());

        // 修改角色
        Set<Role> roleSet = getRoleSet(userParam.getRoleIds());
        user.setRoles(roleSet);

        userRepository.save(user);

    }

    @Transactional
    public void updateState(String ids, Integer state) {

        Set<String> idSet = StringUtil.getStrToIdSet1(ids);

        List<User> userList = new ArrayList<>();
        for (String id : idSet) {
            User user = getEntityById(userRepository, id);
            user.setStatus(state);
        }

        userRepository.saveAll(userList);
    }

    @Transactional
    public void updatePsw(String finalSecret, String newPsw) {

    }

    public List<MenuVo> getMenuInfo() {
        List<MenuVo> menuVoList = new ArrayList<>();
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser != null) {
            Map<String, MenuVo> menuVoMap = new HashMap<>();
            for (SysAuthority authority : loginUser.getSysAuthorities()) {
                if (AuthorityEnum.MENU.getCode() == authority.getAuthorityType()) {
                    MenuVo menuVo = new MenuVo();
                    menuVo.setId(authority.getId());
                    menuVo.setParentId(authority.getParentId());
                    menuVo.setName(authority.getAuthorityName());
                    menuVo.setIcon(authority.getAuthorityIcon());
                    menuVo.setUrl(authority.getAuthorityUrl());
                    menuVo.setSort(authority.getSort());
                    menuVoMap.put(menuVo.getId(), menuVo);
                    if ("-1".equals(menuVo.getParentId())) {
                        menuVo.setUrl("javascript:;");
                        menuVoList.add(menuVo);
                    }
                }
            }
            for (Map.Entry<String, MenuVo> entry : menuVoMap.entrySet()) {
                if (!"-1".equals(entry.getValue().getParentId())) {
                    MenuVo menuVo = menuVoMap.get(entry.getValue().getParentId());
                    menuVo.getSubMenus().add(entry.getValue());
                }
            }
            menuVoList.sort(Comparator.comparing(MenuVo::getSort));
            for (MenuVo menuVo : menuVoList) {
                menuVo.getSubMenus().sort(Comparator.comparing(MenuVo::getSort));
            }
        }
        return menuVoList;
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在：" + username);
        }
        return user;
    }
}
