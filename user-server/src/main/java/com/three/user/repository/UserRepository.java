package com.three.user.repository;


import com.three.user.entity.User;
import com.three.commonjpa.base.repository.BaseRepository;

/**
 * Created by csw on 2019/03/27.
 * Description:
 */

public interface UserRepository extends BaseRepository<User, String> {

    User findByUsername(String username);

}
