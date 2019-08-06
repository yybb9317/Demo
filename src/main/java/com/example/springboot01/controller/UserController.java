package com.example.springboot01.controller;

import com.example.springboot01.Service.UserInfoService;
import com.example.springboot01.response.TResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin()
    public ResponseEntity Signup(
            @RequestParam String phone,
            @RequestParam String password
    ) {
        return TResponse.success(userInfoService.signup(phone, password));
    }

    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity Login(
            @RequestParam String phone,
            @RequestParam String password
    ) {
        return TResponse.success(userInfoService.login(phone, password));
    }
}
