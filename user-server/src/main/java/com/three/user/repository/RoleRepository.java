package com.three.user.repository;

import com.three.user.entity.Role;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by csw on 2019/03/27.
 * Description:
 */
public interface RoleRepository extends BaseRepository<Role, String>, JpaSpecificationExecutor<Role> {

}
