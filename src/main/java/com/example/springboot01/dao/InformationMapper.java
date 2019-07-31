package com.example.springboot01.dao;

import com.example.springboot01.entity.Information;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface InformationMapper {

    /**
     * @Author Bob
     * @date 2019/7/30 12:05
     * @Param id
     * @return List<Information>
            */
//    List<Information> selectByStudentId(String id);

    Information selectById(String id);
}
