package com.example.springboot01;

import org.apache.commons.io.FileUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication
@MapperScan("com.example.springboot01.dao")  // Mybatis扫描dao接口路径
//public class Springboot01Application extends SpringBootServletInitializer {
public class Springboot01Application {
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
//        return builder.sources(Springboot01Application.class);
//    }

    public static void main(String[] args) {
        initDefaultHeadImg();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Springboot01Application.class, args);
    }

    /**
     * @description: 初始化默认头像
     * @Author Bob
     * @date 2019/8/2 18:34
     */
    private static void initDefaultHeadImg() {
        File file = new File("/usr/local/springboot01/files/image/ea8abb81864ef0409a606d88d7d6feb6.jpg");
        File folder = file.getParentFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!file.exists()){
            try {
                ClassPathResource resource = new ClassPathResource("image/home_icon_head.png");
                FileUtils.copyInputStreamToFile(resource.getInputStream(),file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
