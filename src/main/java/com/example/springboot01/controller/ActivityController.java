package com.example.springboot01.controller;

import com.example.springboot01.Service.ActivityService;
import com.example.springboot01.response.TResponse;
import com.example.springboot01.security.anno.CheckToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping(value = "/get")
    public ResponseEntity getActivity(@RequestParam String id) {
        return TResponse.success(activityService.getActivity(id));
    }

    @GetMapping(value = "/list")
    @CheckToken
    public ResponseEntity listActivity(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "15") Integer size
    ) {
        return TResponse.success(activityService.listActivity(title, page, size));
    }



}
