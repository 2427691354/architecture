package com.lixing.service;

import com.lixing.User;
import com.lixing.util.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cc
 * @date 2020/06/05
 **/
@FeignClient(name = "user-provider")
public interface MemberService {

    /**
     * 调取登录的方法
     * @return
     */
    @RequestMapping("/login")
    JsonResult login(@RequestBody User user);

}

