package com.example.springboot01.Service;

import com.example.springboot01.dao.InformationMapper;
import com.example.springboot01.entity.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/7/30  13:51
 */
@Service
public class InformationService {

    @Resource
    private InformationMapper informationMapper;

    public Information getInformation(String id) {
        return informationMapper.selectById(id);
    }
}
