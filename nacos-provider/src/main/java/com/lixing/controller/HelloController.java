package com.lixing.controller;

import com.lixing.filter.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cc
 * @date 2020/06/04
 **/

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@CurrentUser String userName){
        return "Hello," + userName;
    }

}
