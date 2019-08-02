package com.example.springboot01.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/1  17:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser {
    private String id;
    private String username;
    private String password;
    private Date expire;
}
