package com.qunzhi.yespmp.pojo;

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
