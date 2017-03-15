package com.dwqb.tenant.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by zhangqiang on 17/3/15.
 */
public class IdGenerator {

    public  RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public  void setRedisTemplate(RedisTemplate redisTemplate) {
        IdGenerator.redisTemplate = redisTemplate;
    }

    @Autowired
    private static RedisTemplate redisTemplate;

    public IdGenerator() {
    }

    public static long getId(){
        BoundValueOperations<String,Long> op =  redisTemplate.boundValueOps("tenant:id");
        Long i = op.increment(1L);
        return i;
    }

}
