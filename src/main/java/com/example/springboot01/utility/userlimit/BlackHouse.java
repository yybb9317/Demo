package com.example.springboot01.utility.userlimit;

import com.example.springboot01.constant.TConst;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * black house for users and admins
 */
public class BlackHouse {

  private static final LoadingCache<String, Object> BLACK_HOUSE;
  private static final Object VALUE = new Object();

  static {
    BLACK_HOUSE = CacheBuilder.newBuilder()
        // default concurrency level
        .concurrencyLevel(4)
        // house's max limited
        .maximumSize(10000)
//         .set MAX(app, web) as expire time
        .expireAfterWrite(TConst.APP_EXPIRE, TimeUnit.MILLISECONDS)
        .build(
            new CacheLoader<String, Object>() {
              @Override
              public Object load(String key) throws Exception {
                return VALUE;
              }
            }
        );
  }

  /**
   * add user to house
   *
   * @param id user id
   */
  public static void put(String id) {
    BLACK_HOUSE.put(id, VALUE);
  }

  /**
   * put selected users to house
   *
   * @param ids user ids
   */
  public static void put(Collection<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return;
    }
    for (String id : ids) {
      BLACK_HOUSE.put(id, VALUE);
    }
  }

  /**
   * release user
   *
   * @param id user id
   */
  public static void release(String id) {
    BLACK_HOUSE.invalidate(id);
  }

  /**
   * check user is in house
   */
  public static boolean exist(String id) {
    return BLACK_HOUSE.getIfPresent(id) != null;
  }

}
