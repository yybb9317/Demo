package com.qunzhi.yespmp.dao;

import com.qunzhi.yespmp.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo, String>{
    UserInfo selectByPhone(@Param("phone") String phone);
}