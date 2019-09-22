package com.three.authserver.feign;

import com.three.common.auth.LoginUser;
import com.three.common.auth.SysAuthority;
import com.three.common.constants.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by csw on 2019/07/17.
 * Description:
 */
@FeignClient(name = ServiceConstant.USER_SERVICE)
public interface UserClient {

    @GetMapping(value = "/internal/findByUsername", params = "username")
    LoginUser findByUsername(@RequestParam("username") String username);

    @GetMapping(value = "/internal/findAllAuthorities")
    List<SysAuthority> findAllAuthorities();
}
