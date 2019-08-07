package com.qunzhi.yespmp.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/1  14:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO implements Dto{
    private String id;

    private String phone;

    private String nickname;

    private String avatar;

    private String gender;

    private LocalDateTime createdOn;
}
