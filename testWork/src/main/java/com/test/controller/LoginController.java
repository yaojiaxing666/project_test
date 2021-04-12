package com.test.controller;

import com.test.model.UserInfo;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
@Log
public class LoginController {

    @GetMapping("/in")
    public String in(HttpServletRequest request,UserInfo userInfo){
        log.info("登录的用户userInfo:==="+userInfo);
        HttpSession session = request.getSession();
        session.setAttribute("userInfo",userInfo.toString());
        return "登录成功！login success!";
    }

    @GetMapping("/out")
    public String out(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("userInfo");
        return "退出成功！logout success!";
    }

    @GetMapping("/getUser")
    public Object getUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
        log.info("getUser:==="+userInfo.toString());
        return userInfo;
    }
}
