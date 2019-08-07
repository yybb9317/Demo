package com.qunzhi.yespmp.Service;

import com.qunzhi.yespmp.dao.ActivityMapper;
import com.qunzhi.yespmp.entity.Activity;
import com.qunzhi.yespmp.pojo.ActivityDTO;
import com.qunzhi.yespmp.response.TPage;
import com.qunzhi.yespmp.utility.BeanUtil;
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
