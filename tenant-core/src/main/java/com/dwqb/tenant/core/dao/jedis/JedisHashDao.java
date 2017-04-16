package com.dwqb.tenant.core.dao.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by zhangqiang on 16/12/8.
 */
@Repository
public class JedisHashDao extends JedisBaseDao{
    private static final Logger logger = LoggerFactory.getLogger(JedisHashDao.class);

    public String hget(final String key, final String field){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.hget(key,field);
        }catch (Throwable e){
            logger.error("redis错误",e);
            throw e;
        }finally {
            try{
                if (null != jedis) {
                    jedis.close();
                }
            }catch (Throwable e){
                logger.error("redis close 错误",e);
            }
        }
    }


    public Long hset(final String key, final String field, final String value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.hset(key,field,value);
        }catch (Throwable e){
            logger.error("redis错误",e);
            return -1L;
        }finally {
            try{
                if (null != jedis) {
                    jedis.close();
                }
            }catch (Throwable e){
                logger.error("redis close 错误",e);
            }
        }
    }

    public Long hincrBy(final String key, final String field, final long value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.hincrBy(key,field,value);
        }catch (Throwable e){
            logger.error("redis错误",e);
            return 0L;
        }finally {
            try{
                if (null != jedis) {
                    jedis.close();
                }
            }catch (Throwable e){
                logger.error("redis close 错误",e);
            }
        }
    }

}
