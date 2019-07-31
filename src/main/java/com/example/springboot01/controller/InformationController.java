package com.example.springboot01.controller;

import com.example.springboot01.Service.InformationService;
import com.example.springboot01.response.TResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @GetMapping(value = "/get")
    public ResponseEntity getInformation(String id) {
        return TResponse.success(informationService.getInformation(id));
    }



}
