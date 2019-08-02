package com.example.springboot01.controller;

import com.example.springboot01.Service.UserInfoService;
import com.example.springboot01.response.TResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/1  14:31
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/signup")
    public ResponseEntity Signup(
            @RequestParam String phone,
            @RequestParam String password
    ) {
        return TResponse.success(userInfoService.signup(phone, password));
    }

    @PostMapping("/login")
    public ResponseEntity Login(
            @RequestParam String phone,
            @RequestParam String password
    ) {
        return TResponse.success(userInfoService.login(phone, password));
    }
}
