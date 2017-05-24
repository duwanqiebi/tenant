package com.dwqb.tenant.worker;

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
        bayes.simulateFalseRoom();
//        List<RoomBayes> roomBayes = bayes.getAll();
//        for(int i = 0 ; i < roomBayes.size() ; i ++){
//            if(i <= 60){
//                roomBayes.get(i).setStatus("true");
//            }else{
//                roomBayes.get(i).setStatus("false");
//            }
//        }
//        bayes.getWeight(roomBayes);
    }
}
