package com.example.springboot01.utility.userlimit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * simple rate limiter based on per user
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RateLimiter {

  /**
   * coupon limiter
   */
  public static final RateLimiter COUPON = new RateLimiter(1000);

  /**
   * limit request rate in milliseconds
   */
  private long rate;
  private Map<String, Long> map = new ConcurrentHashMap<>();

  private RateLimiter(long rate) {
    this.rate = rate;
  }

  /**
   * acquire a request
   *
   * @param id user id
   * @return true if success
   */
  public boolean acquire(String id) {
    Long before;
    long now = System.currentTimeMillis();
    if ((before = map.get(id)) == null || before + rate < now) {
      map.put(id, now);
      return true;
    }
    return false;
  }

}
