package com.three.user.repository;

import com.three.user.entity.RoleServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;


/**
 * Created by csw on 2019/03/27.
 * Description:
 */
public interface RoleServerRepository extends JpaRepository<RoleServer, String>, JpaSpecificationExecutor<RoleServer> {

    List<RoleServer> findAllByRoleId(String roleId);
}
