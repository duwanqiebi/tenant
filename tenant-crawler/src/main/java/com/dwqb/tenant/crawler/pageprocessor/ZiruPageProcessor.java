package com.dwqb.tenant.crawler.pageprocessor;

import com.dwqb.tenant.core.baiduAPI.BaiduMapAPI;
import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.Region;
import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.core.model.RoomType;
import com.dwqb.tenant.core.utils.IdGenerator;
import com.dwqb.tenant.core.utils.JsonUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import java.util.List;
import java.util.Random;

@Component("ziru")
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
            if(price.length() > 0){
                char start = price.charAt(0);
                if(start < 'a' || start > 'Z'){
                    price = price.substring(1);
                }
            }
            Double longitude = Double.parseDouble(html.css("#mapsearchText","data-lng").get()) ;     //经度
            Double latitude = Double.parseDouble(html.css("#mapsearchText","data-lat").get()) ;       //韦度
            Region region = BaiduMapAPI.covertLocation(longitude,latitude);         //区域

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

            String tempType = detailRooms.get(2).css("span","text").get();
            System.out.println(tempType);
            RoomType roomType = null;
            if("整".equals(tempType)){
                roomType = RoomType.parseRoomType(struct);
            }else{
                roomType = RoomType.WO_SHI;
            }

            String floor = detailRooms.get(3).css("li","text").get();     //楼层
            floor = floor.substring(4,floor.length() - 1);


            //图片地址
            List<String> imgList = html.css(".lof-main-outer > ul > li > a > img").xpath("/img/@src").all();
            imgList = this.removeDuplicate(imgList);

            Room room = new Room(roomName,Double.parseDouble(price),longitude,latitude,region.toString(),priceType,status,Double.parseDouble(space),dirction,struct,roomType.toString(),floor,imgList);

            //es
            String json = JsonUtils2.obj2Json(room);
            ESUtils.curl("http://localhost:9200/room/room/" + String.valueOf(IdGenerator.getId()) ,"PUT", JsonUtils2.obj2Json(room));
        }


    }

    public static void main(String pageNum) {
        Spider ziruSpider = Spider.create(new ZiruPageProcessor()).addUrl("http://www.ziroom.com/z/nl/?p=" + pageNum).thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(1000));
        ziruSpider.run();
    }
}
