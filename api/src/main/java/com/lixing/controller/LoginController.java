package com.lixing.controller;

import com.lixing.User;
import com.lixing.service.MemberService;
import com.lixing.util.JsonResult;
import com.lixing.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cc
 * @date 2020/06/05
 **/
@RestController
@RequestMapping("/route-api")
public class LoginController {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private MemberService memberService;

    /**
     * 登录方法
     * @param userName
     * @param password
     * @return
     */
    @GetMapping(value = "/login")
    public JsonResult login(@RequestParam("userName") String userName, @RequestParam("password") String password){
        Map<String, String> map = new HashMap<>();
        //账号密码校验
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        JsonResult login = memberService.login(user);
        if(login.getState() == true) {
            //  生成token
            return JsonResult.renderSuccess(tokenUtil.getToken(userName, 2));
        }
        return JsonResult.renderError(login.getMessage());
    }

    /**
     * 刷新JWT
     * @param refreshToken
     * @return
     */
    @GetMapping("/refresh")
    public Map<String, String> refreshToken(@RequestParam("refreshToken") String refreshToken, @RequestParam("userName") String userName){
        return tokenUtil.refreshToken(userName, 2, refreshToken);
    }

    /**
     * 退出时删除key
     * @param phone
     * @return
     */
    @GetMapping("/logout")
    public String logout( @RequestParam("userName") String userName){
        tokenUtil.removeToken(userName, 2);
        return "退出成功";
    }

}

