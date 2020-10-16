package com.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class TestOauth2Controller {

    @GetMapping("/addOrder")
    public String order(){
        return "成功！";
    }
}
