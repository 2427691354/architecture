package com.lixing.service;

import com.lixing.entity.Permission;
import com.lixing.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService{
     User findByUserName(String userName);

     String login(User user);
}
