package com.dwqb.tenant.crawler.pageprocessor;

import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.core.utils.JsonUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangqiang on 16/12/17.
 */
public class ZiruPageProcessor extends AbstractPageProcessor{

    private static Logger logger = LoggerFactory.getLogger(ZiruPageProcessor.class);

    AtomicInteger index = new AtomicInteger(1);

    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();

        if(curUrl.contains("/z/nl")) {         //目录页
            List<String> urls = html.css(".t1").links().all();
            page.addTargetRequests(urls);
        }else{                                  //详情页
            String roomName = html.css(".room_name > h2","text").get();
            String price = html.css(".room_price","text").get();
            if(price.length() > 0){
                char start = price.charAt(0);
                if(start < 'a' || start > 'Z'){
                    price = price.substring(1);
                }
            }
            String longitude = html.css("#mapsearchText","data-lng").get();     //经度
            String latitude = html.css("#mapsearchText","data-lat").get();       //韦度
            String priceType = html.css(".price .gray-6").get();    //付款方式

            String isShare = html.css(".icons").get();      //整租
            String status = null;
            if(isShare.equals("整")){
                for (String item : html.css(".current .tags").all()){
                    if ("当前房源".equals(item)){
                        status="可租";
                        break;
                    }
                }
            }else{
                status="可租";
            }


            List<Selectable> detailRooms = html.css(".detail_room > li").nodes();
            String space = detailRooms.get(0).css("li","text").get();   //面积
            space = space.substring(4,space.length() - 1);
            String dirction = detailRooms.get(1).css("li","text").get().substring(4);   //朝向
            String struct = detailRooms.get(2).css("li","text").get().substring(4); //结构
            String floor = detailRooms.get(3).css("li","text").get();     //楼层
            floor = floor.substring(4,floor.length() - 1);


            //图片地址
            List<String> imgList = html.css(".lof-main-outer > ul > li > a > img").xpath("/img/@src").all();



            Room room = new Room(roomName,Double.parseDouble(price),Double.parseDouble(longitude),Double.parseDouble(latitude),priceType,status,Double.parseDouble(space),dirction,struct,floor,imgList);

            //es
            String json = JsonUtils2.obj2Json(room);
            ESUtils.curl("http://localhost:9200/room/room/" + String.valueOf(index.getAndAdd(1)) ,"PUT", JsonUtils2.obj2Json(room));
        }


    }

    public static void main(String[] args) {
        Spider ziruSpider = Spider.create(new ZiruPageProcessor()).addUrl("http://www.ziroom.com/z/nl/?p=3").thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(1000));
        ziruSpider.run();
    }
}
