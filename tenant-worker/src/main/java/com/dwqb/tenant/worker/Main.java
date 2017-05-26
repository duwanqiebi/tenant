package com.dwqb.tenant.worker;

import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.core.model.RoomBayes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by zhangqiang on 17/4/16.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext  = new ClassPathXmlApplicationContext("classpath:spring/application-worker.xml");
        Bayes bayes = applicationContext.getBean(Bayes.class);
//        bayes.simulateFalseRoom();
//        List<Room> roomBayes = bayes.getAll();
//        bayes.getWeight(roomBayes);
        bayes.test();
    }
}
