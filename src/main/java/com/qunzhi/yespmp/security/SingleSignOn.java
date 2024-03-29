package com.qunzhi.yespmp.security;

import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @title:   utility class for SSO
 * @author: Yuanbo
 * @date: 2019/8/2  14:37
 */
@UtilityClass
public final class SingleSignOn {

    private static final Map<String, Date> SSO = new ConcurrentHashMap<>();

    /**
     * check if current request is valid
     *
     * @param id user id
     * @param expire jwt expire time
     * @return true if check passed
     */
    public static boolean check(String id, Date expire) {
        Date date;
        if ((date = SSO.get(id)) == null || !date.after(expire)) {
            SSO.put(id, expire);
            return true;
        }
        return false;
    }
}
