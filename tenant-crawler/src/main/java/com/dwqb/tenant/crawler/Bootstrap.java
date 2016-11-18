package com.dwqb.tenant.crawler;

import com.dwqb.tenant.crawler.schedule.MyRedisSchedule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhangqiang on 16/11/18.
 */
public class Bootstrap {
    public static void main(String[] args){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/application-crawler.xml");
        MyRedisSchedule schedule = (MyRedisSchedule) applicationContext.getBean("myRedisSchedule");
        System.out.println(schedule);
    }
}
