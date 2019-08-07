package com.qunzhi.yespmp.controller;

import com.qunzhi.yespmp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @title:    个人中心
 * @author: Yuanbo
 * @date: 2019/8/6  12:22
 */
@Controller
@RequestMapping("/user/{uid}")
public class UserController {
    @Autowired
    private UserService userService;


}
