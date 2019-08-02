package com.example.springboot01.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/1  14:34
 */
@Data
@Builder
@NoArgsConstructor
public class UserLoginDTO implements Dto{
    //用户信息
    private UserInfoDTO userInfoDTO;

    //JWT
    private String token;

    public UserLoginDTO(UserInfoDTO info, String token) {
        this.userInfoDTO = info;
        this.token = token;
    }
}
