package com.qunzhi.yespmp.utility.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis util only for objects
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisUtil extends AbstractRedisUtil {

  @Resource
  public void setTemplate(RedisTemplate<String, Object> template) {
    super.template = template;
  }


}
