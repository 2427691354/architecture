package com.lixing.service.impl;

import com.lixing.dao.UserRepository;
import com.lixing.entity.User;
import com.lixing.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author cc
 * @date 2020/06/03
 **/
@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    UserRepository userRepository;

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public String login(User user) {

        String msg="";
        // 1、获取Subject实例对象
        Subject currentUser  = SecurityUtils.getSubject();

//        // 2、判断当前用户是否登录
//        if (currentUser.isAuthenticated() == false) {
//
//        }

        // 3、将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());

        // 4、认证
        try {
            currentUser.login(token);// 传到MyAuthorizingRealm类中的方法进行认证
            Session session = currentUser.getSession();
            session.setAttribute("userName", user.getUserName());
            return "成功";
        }catch (UnknownAccountException e)
        {
            e.printStackTrace();
            return "UnknownAccountException -- > 账号不存在：";
        }
        catch (IncorrectCredentialsException e)
        {
            return  "IncorrectCredentialsException -- > 密码不正确：";
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
            return "用户验证失败";
        }
    }
}
