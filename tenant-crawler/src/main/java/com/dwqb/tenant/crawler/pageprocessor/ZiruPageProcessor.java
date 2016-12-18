package com.dwqb.tenant.crawler.pageprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * Created by zhangqiang on 16/12/17.
 */
public class ZiruPageProcessor extends AbstractPageProcessor{

    private static Logger logger = LoggerFactory.getLogger(ZiruPageProcessor.class);

    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();

        if(curUrl.contains("/z/nl")) {         //目录页
            List<String> urls = html.css(".t1").links().all();
            page.addTargetRequests(urls);
        }else{                                  //详情页
            String roomName = html.css(".room_name > h2","text").get();
            String price = html.css(".room_price","text").get();
            String longitude = html.css("#mapsearchText","data-lng").get();     //经度
            String latitude = html.css("#mapsearchText","data-lat").get();       //韦度
            String priceType = html.css(".price .gray-6").get();    //付款方式

            String isShare = html.css(".icons").get();      //整租
            String status;
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

            // TODO: 16/12/18 model类 
        }


    }

    public static void main(String[] args) {
        Spider.create(new ZiruPageProcessor()).addUrl("http://www.ziroom.com/z/nl/?p=3").thread(1).run();
    }
}
