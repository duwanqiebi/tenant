package com.dwqb.tenant.crawler;

import com.dwqb.tenant.core.utils.IdGenerator;
import com.dwqb.tenant.crawler.pageprocessor.AnjukePageProcessor;
import com.dwqb.tenant.crawler.pageprocessor.LianjiaPageProcessor;
import com.dwqb.tenant.crawler.pageprocessor.WUBAPageProcessor;
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
//        AnjukePageProcessor processor = applicationContext.getBean(AnjukePageProcessor.class);
////        processor.doCrawer("10");
//        processor.doCrawerByNum("10");   //anjuke

        //58
//        WUBAPageProcessor processor = applicationContext.getBean(WUBAPageProcessor.class);
//        processor.doCrawerByNum("11");

        //ziru
        ZiruPageProcessor processor = applicationContext.getBean(ZiruPageProcessor.class);
        processor.doCrawer("10");

    }


//    public static void main(String[] args) {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/application-crawler.xml");
//        context.start();
//    }
}
