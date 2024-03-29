package com.qunzhi.yespmp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis config <p> spring redis must only annotated for the SERVICE layer!
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  @Bean
  @Primary
  @Autowired
  public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setDefaultSerializer(new JdkSerializationRedisSerializer());
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new JdkSerializationRedisSerializer());
    template.setHashValueSerializer(new JdkSerializationRedisSerializer());
    return template;
  }

//  @Bean
//  @Override
//  public KeyGenerator keyGenerator() {
//    return (target, method, params) -> {
//      StringBuilder sb = new StringBuilder();
//      sb.append(target.getClass().getSimpleName()).append(":");
//      sb.append(method.getName()).append(":");
//      for (Object o : params) {
//        if (o != null) {
//          sb.append(o.toString());
//        }
//        sb.append(",");
//      }
//      return sb.toString();
//    };
//  }

}