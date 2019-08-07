package com.qunzhi.yespmp.controller;

import com.qunzhi.yespmp.Service.ActivityService;
import com.qunzhi.yespmp.response.TResponse;
import com.qunzhi.yespmp.security.anno.CheckToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@CrossOrigin
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
