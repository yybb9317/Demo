package com.qunzhi.yespmp.controller;

import com.qunzhi.yespmp.Service.AuthService;
import com.qunzhi.yespmp.response.TResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @title:   登录注册等权限接口
 * @author: Yuanbo
 * @date: 2019/8/1  14:31
 */
@RestController
@RequestMapping("/authentication")
@CrossOrigin
public class AuthController {
    @Autowired
    AuthService userInfoService;

    /**
     * @description:  登录
     * @Author Bob
     * @date 2019/8/6 15:14
     */
    @PostMapping("/signup")
    public ResponseEntity Signup(
            @RequestParam String phone,
            @RequestParam String password
    ) {
        return TResponse.success(userInfoService.signup(phone, password));
    }

    /**
     * @description:  注册
     * @Author Bob
     * @date 2019/8/6 15:14
     */
    @PostMapping("/login")
    public ResponseEntity Login(
            @RequestParam String phone,
            @RequestParam String password
    ) {
        return TResponse.success(userInfoService.login(phone, password));
    }
}
