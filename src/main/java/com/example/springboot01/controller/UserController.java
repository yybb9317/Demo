package com.example.springboot01.controller;

import com.example.springboot01.Service.UserService;
import com.example.springboot01.constant.DataBaseEnum.*;
import com.example.springboot01.response.TResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @title:    个人中心
 * @author: Yuanbo
 * @date: 2019/8/6  12:22
 */
@Controller
@CrossOrigin
@RequestMapping("/user/{uid}")
public class UserController {
    @Autowired
    private UserService userService;


}
