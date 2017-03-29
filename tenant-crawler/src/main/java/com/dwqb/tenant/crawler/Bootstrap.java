package com.dwqb.tenant.crawler;

import com.dwqb.tenant.core.utils.IdGenerator;
import com.dwqb.tenant.crawler.pageprocessor.ZiruPageProcessor;
import com.dwqb.tenant.crawler.schedule.MyRedisSchedule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhangqiang on 16/11/18.
 */
public class Bootstrap {
    public static void main(String[] args){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/application-crawler.xml");
        ZiruPageProcessor processor = applicationContext.getBean("ziru",ZiruPageProcessor.class);


//        IdGenerator idGenerator = applicationContext.getBean("idGenerator",IdGenerator.class);
//        System.out.println(idGenerator);
        for(int i = 1 ; i <= 10; i ++){
            processor.main(String.valueOf(1));
        }
//        System.out.println(IdGenerator.getId());
    }
}
