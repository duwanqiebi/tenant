package com.dwqb.tenant.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by zhangqiang on 17/3/15.
 */
@Component
public class IdGenerator {

    @Autowired
    private RedisTemplate redisTemplate;

    public  long getId(){
        BoundValueOperations<String,Long> op =  redisTemplate.boundValueOps("tenant:id");
        Long i = op.increment(1L);
        return i;
    }

}
