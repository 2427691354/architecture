package com.lixing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cc
 * @date 2020/06/02
 **/

@RestController
public class ProviderController {
    @GetMapping("/hi")
    public String hi(@RequestParam(value = "name",defaultValue = "forezp",required = false)String name){
        return "hi "+name;
    }
}

