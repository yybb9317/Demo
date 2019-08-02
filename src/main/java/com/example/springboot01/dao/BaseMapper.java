package com.example.springboot01.dao;

import com.example.springboot01.entity.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<T,String> {
    int deleteByPrimaryKey(String id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
