package com.three.user.repository;


import com.three.user.entity.Employee;
import com.three.user.entity.Role;
import com.three.user.entity.User;
import com.three.resource_jpa.jpa.base.repository.BaseRepository;

import java.util.List;

/**
 * Created by csw on 2019/03/27.
 * Description:
 */

public interface UserRepository extends BaseRepository<User, String> {

    User findByUsername(String username);

    User findByIsAdmin(int code);

    User findByEmployee(Employee employee);
}
