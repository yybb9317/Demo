package com.example.springboot01.pojo;

import com.example.springboot01.constant.DataBaseEnum.ActivityState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/1  10:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO implements Dto{
    //id
    private String id;

    //封面图id
    private String cover;

    //标题
    private String activityName;

    //状态
    private ActivityState state;

    //举办地点-省
    private String province;

    //举办地点-市
    private String city;

    //举办地点-区
    private String district;

    //开始时间
    private LocalDateTime startTime;
}
