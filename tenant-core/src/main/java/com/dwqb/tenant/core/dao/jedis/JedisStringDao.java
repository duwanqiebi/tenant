package com.dwqb.tenant.core.dao.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by zhangqiang on 16/12/20.
 */
@Component
public class JedisStringDao  extends JedisBaseDao{
    private static final Logger logger = LoggerFactory.getLogger(JedisStringDao.class);

    public String get(String key){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.get(key);
        }catch (Throwable e){
            logger.error("redis错误",e);
            throw e;
        }finally {
            returnConnection(jedis);
        }
    }

    public String set(final String key, final String value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.set(key,value);
        }catch (Throwable e){
            logger.error("redis错误",e);
            throw e;
        }finally {
            returnConnection(jedis);
        }
    }
}
