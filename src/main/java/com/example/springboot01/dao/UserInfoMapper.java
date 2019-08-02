package com.example.springboot01.dao;

import com.example.springboot01.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo, String>{
    UserInfo selectByPhone(@Param("phone") String phone);
}