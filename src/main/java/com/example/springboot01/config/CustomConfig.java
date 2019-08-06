package com.example.springboot01.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @title:   习惯配置
 * @author: Yuanbo
 * @date: 2019/8/2  18:21
 */

@Getter
@EnableScheduling
@Configuration
public class CustomConfig {
    /**
     *  图片存放路径
     */
    @Value("${qunzhi.path.image}")
    private String pathImage;

    /**
     *  视频存放路径
     */
    @Value("${qunzhi.path.video}")
    private String pathVideo;

    /**
     *  文档存放路径
     */
    @Value("${qunzhi.path.text}")
    private String pathText;

    /**
     *  音频存放路径
     */
    @Value("${qunzhi.path.audio}")
    private String pathAudio;

    /**
     *  应用程序存放路径
     */
    @Value("${qunzhi.path.application}")
    private String pathApplication;
}
