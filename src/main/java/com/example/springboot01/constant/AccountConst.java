package com.example.springboot01.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @title:   账户常量
 * @author: Yuanbo
 * @date: 2019/8/2  18:26
 */
@UtilityClass
public final class AccountConst {
    public static final AccountConfig CONFIG;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class AccountConfig {
        //微信
        @Value("wx.app-id")
        public String wxAppId;
        @Value("wx.app-key")
        public String wxAppKey;
        @Value("wx.mch-id")
        public String wxMchId;
        @Value("wx.secret-key")
        public String wxSecretKey;
        @Value("wx.cert.path")
        public String wxCertPath;
    }

    static {
        try {
            CONFIG = load();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private static final AccountConfig load() throws IllegalAccessException, IOException {
        AccountConfig config = new AccountConfig();
        InputStream in = AccountConst.class.getResourceAsStream("/account.properties");
        InputStreamReader reader = new InputStreamReader(in,"utf-8");
        Properties prop = new Properties();
        prop.load(reader);
        Class<AccountConfig> clz = AccountConfig.class;
        Field[] fields = clz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String key = field.getAnnotation(Value.class).value();
            String value = prop.getProperty(key);
            notNull(value,"The key '" + key + "' in account.properties file must exist and can not be empty value!");
            field.set(config,value);
        }
        return config;
    }

    private static void notNull(@Nullable String obj, String message) {
        if (obj == null || obj.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
