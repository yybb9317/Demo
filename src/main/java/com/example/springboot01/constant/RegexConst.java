package com.example.springboot01.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * @title:
 * @author: Yuanbo
 * @date: 2019/8/1  14:49
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexConst {
    // 大陆手机
    public static final Pattern CHINA_PHONE = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]{9}$");
    // 大陆身份证
//  public static final Pattern CHINA_ID_CARD = Pattern.compile("(^\\d{18}$)|(^\\d{15}$)");
}
