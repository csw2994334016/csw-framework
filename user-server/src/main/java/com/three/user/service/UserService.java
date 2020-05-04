package com.three.user.service;

import com.google.common.base.Preconditions;
import com.three.common.auth.SysAuthority;
import com.three.common.auth.LoginUser;
import com.three.common.auth.SysRole;
import com.three.commonclient.exception.BusinessException;
import com.three.resource_jpa.resource.utils.LoginUserUtil;
import com.three.user.entity.*;
import com.three.user.param.UserParam;
import com.three.user.repository.AuthorityRepository;
import com.three.user.repository.RoleRepository;
import com.three.user.repository.UserRepository;
import com.three.user.vo.MenuVo;
import com.three.common.enums.AuthorityEnum;
import com.three.resource_jpa.jpa.base.service.BaseService;
import com.three.common.vo.PageQuery;
import com.three.common.vo.PageResult;
import com.three.common.utils.BeanCopyUtil;
import com.three.commonclient.utils.BeanValidator;
import com.three.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private OrganizationService organizationService;

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

    public List<MenuVo> getMenuInfo() {
        List<MenuVo> menuVoList = new ArrayList<>();
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser != null) {
            Map<String, MenuVo> menuVoMap = new HashMap<>();
            Set<SysRole> sysRoleSet = loginUser.getSysRoles();
            Set<Authority> authoritySet = new HashSet<>();
            sysRoleSet.forEach(e -> {
                authoritySet.addAll(roleRepository.findById(e.getId()).get().getAuthorities());
            });
            for (Authority sysAuthority : authoritySet) {
                if (AuthorityEnum.MENU.getCode() == sysAuthority.getAuthorityType()) {
                    MenuVo menuVo = new MenuVo();
                    menuVo.setId(sysAuthority.getId());
                    menuVo.setParentId(sysAuthority.getParentId());
                    menuVo.setName(sysAuthority.getAuthorityName());
                    menuVo.setIcon(sysAuthority.getAuthorityIcon());
                    menuVo.setUrl(sysAuthority.getAuthorityUrl());
                    menuVo.setPath(sysAuthority.getAuthorityUrl());
                    menuVo.setSort(sysAuthority.getSort());
                    menuVoMap.put(menuVo.getId(), menuVo);
                    if ("-1".equals(menuVo.getParentId())) {
                        menuVo.setUrl("javascript:;");
                        menuVoList.add(menuVo);
                    }
                }
            }
            for (Map.Entry<String, MenuVo> entry : menuVoMap.entrySet()) {
                if (!"-1".equals(entry.getValue().getParentId())) {
                    MenuVo parent = menuVoMap.get(entry.getValue().getParentId());
                    parent.getChildren().add(entry.getValue());
                    parent.setUrl("javascript:;");
                }
            }
            menuVoList.sort(Comparator.comparing(MenuVo::getSort));
            for (MenuVo menuVo : menuVoList) {
                menuVo.getChildren().sort(Comparator.comparing(MenuVo::getSort));
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

    public User findByAdmin(int code, String firstOrganizationId) {
        List<User> userList = userRepository.findAllByIsAdmin(code);
        for (User user : userList) {
            Organization organization = organizationService.findById(user.getEmployee().getOrganizationId());
            if (organization.getOrganizationId().equals(firstOrganizationId)) {
                return user;
            }
        }
        throw new BusinessException("该公司(" + firstOrganizationId + ")不存在系统管理员");
    }

    public User findByEmployee(Employee employee) {
        User user = userRepository.findByEmployee(employee);
        Preconditions.checkNotNull(user, "人员(" + employee.getUsername() + ")账户不存在");
        return user;
    }
}
