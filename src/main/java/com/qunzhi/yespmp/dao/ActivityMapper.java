package com.qunzhi.yespmp.dao;

import com.qunzhi.yespmp.constant.DataBaseEnum.ActivityState;
import com.qunzhi.yespmp.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity, String>{
    List<Activity> listByName(@Param("title") String title, @Param("page") Integer page,
                              @Param("size")Integer size, @Param("state") ActivityState...state);
}