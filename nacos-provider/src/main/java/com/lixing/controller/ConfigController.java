package com.lixing.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cc
 * @date 2020/06/02
 **/
@RestController
@RefreshScope
public class ConfigController {

    @Value("${username}")
    private String username;

    @RequestMapping("/username")
    public String get() {
        return username;
    }
}

