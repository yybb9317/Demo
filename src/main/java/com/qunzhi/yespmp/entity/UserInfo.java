package com.qunzhi.yespmp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    private String id;

    private String phone;

    private String password;

    private String nickname;

    private String avatar;

    private String gender;

    private Byte utype;

    private Byte state;

    private String token;

    private String frozenReason;

    private LocalDateTime frozenOn;

    private LocalDateTime createdOn;
}