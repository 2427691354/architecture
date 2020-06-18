package com.lixing.controller;

import com.lixing.entity.User;
import com.lixing.service.UserService;
import com.lixing.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author cc
 * @date 2020/06/03
 **/
@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("login")
    JsonResult login(@RequestBody User user){
        String msg = userService.login(user);

        if(msg != "成功"){
            return JsonResult.renderFail(msg);
        }
        return JsonResult.renderSuccess(userService.findByUserName(user.getUserName()));
    }

    @GetMapping(value = "tes1t")
    JsonResult unauth() {
        return JsonResult.renderSuccess("test1");
    }
}
