package com.example.springboot01.Service;

import com.example.springboot01.constant.DataBaseEnum.ActivityState;
import com.example.springboot01.dao.ActivityMapper;
import com.example.springboot01.entity.Activity;
import com.example.springboot01.pojo.ActivityDTO;
import com.example.springboot01.response.TPage;
import com.example.springboot01.utility.BeanUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/7/30  13:51
 */
@Service
public class ActivityService {

    @Resource
    private ActivityMapper activityMapper;

    /**
     * @description: 根据id获取活动
     * @Author Bob
     * @date 2019/8/2 10:39
     */
    public Activity getActivity(String id) {
        return activityMapper.selectByPrimaryKey(id);
    }

    /**
     * @description: 获取活动列表(模糊查询，不传则查询所有)
     * @Author Bob
     * @date 2019/8/2 10:39
     */
    public TPage<ActivityDTO> listActivity(String title, Integer  page, Integer  size) {
        page = page - 1;
        List<Activity> result = activityMapper.listByName(title, page, size, null);

        return TPage.of(BeanUtil.copy(result, ActivityDTO.class), result.size());
    }
}
