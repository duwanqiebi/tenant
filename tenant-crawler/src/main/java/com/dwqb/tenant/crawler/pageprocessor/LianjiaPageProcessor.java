package com.dwqb.tenant.crawler.pageprocessor;

import com.dwqb.tenant.core.baiduAPI.BaiduMapAPI;
import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.Region;
import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.core.model.RoomOrigin;
import com.dwqb.tenant.core.model.RoomType;
import com.dwqb.tenant.core.utils.IdGenerator;
import com.dwqb.tenant.core.utils.JsonUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class LianjiaPageProcessor extends AbstractPageProcessor{

    private static Logger logger = LoggerFactory.getLogger(LianjiaPageProcessor.class);

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();

        if(curUrl.contains("bj.lianjia.com/zufang/pg")){        //目录页
            List<String> urls = html.xpath("//ul[@id='house-lst']//h2").links().all();
            urls = this.removeDuplicate(urls);
            logger.info("共添加 " + urls.size() + "条明细");
            page.addTargetRequests(urls);

        }else{          //详情页
            if(this.isDetailHandled(curUrl)){
                logger.info("已经抓取过" + curUrl);
                return;
            }
            logger.info("开始处理 " + curUrl);
            String roomName = html.xpath("//div[@class='title']/h1/text()").get();
            String price = html.xpath("//span[@class='total']/text()").get();
            List<Selectable> detail = html.xpath("//div[@class='zf-room']/p").nodes();

            String space = detail.get(0).xpath("//p/text()").get();
            //space去掉()内容
            if(space.contains("(")){
                space = space.substring(0,space.indexOf("(")).trim();
            }
            //space去掉中文
            space = space.substring(0,space.length() - 2);

            String struct = detail.get(1).xpath("//p/text()").get();
            RoomType roomType = RoomType.parseRoomType(struct.split(" ")[0]);

            String floor = detail.get(2).xpath("//p/text()").get();
            String direction = detail.get(3).xpath("//p/text()").get().replace(" ","");

            List<String> subwayDescrption = new ArrayList<>(1);
            subwayDescrption.add(detail.get(4).xpath("//p/text()").get());

            String htmlStr = html.toString();
            int resBlockIndex = htmlStr.indexOf("resblockPosition");
            int begin = htmlStr.indexOf("'",resBlockIndex);
            int end  = htmlStr.indexOf("'",begin + 1);
            String position = htmlStr.substring(begin + 1,end);
            String[] positions = position.split(",");
            Double longitude = Double.parseDouble(positions[0]);
            Double latitude = Double.parseDouble(positions[1]);

            Region region = BaiduMapAPI.covertLocation(longitude,latitude);         //区域

            String contractName = html.xpath("//div[@class='brokerName']/a[@class='name LOGCLICK']/text()").get();
            String contractTel = html.xpath("//div[@class='phone']/text()").get();
            String description = html.xpath("//div[@class='featureContent']").get();

            List<String> imgList = html.xpath("//div[@class='thumbnail']//img/@src").all();


            Room room = new Room(null, RoomOrigin.LIAN_JIA.toString(),curUrl,contractName,contractTel,subwayDescrption,description,roomName,Double.parseDouble(price),longitude,latitude,region.toString(),null,null,Double.parseDouble(space),direction,struct,roomType != null? roomType.toString():struct,floor,imgList);
            Long id = IdGenerator.getId();
            room.setId(id);
            room.setStatus("true");
//            logger.info(JsonUtils2.obj2Json(room));
            ESUtils.curl("http://112.74.79.166:9200/room/room/" + String.valueOf(id) ,"PUT", JsonUtils2.obj2Json(room));
            this.handled(curUrl);

//            try {
//                hbase(room);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        }
    }

    public  void doCrawer(String pageNum) {
        Spider ziruSpider = Spider.create(new LianjiaPageProcessor());
        int num = Integer.parseInt(pageNum);
        while(num >= 1){
            ziruSpider.addUrl("https://bj.lianjia.com/zufang/pg" + num + "/");
            num --;
        }
        ziruSpider.thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(2000));
        ziruSpider.run();
    }

    public void doCrawerByNum(String pageNum){
        Spider ziruSpider = Spider.create(new LianjiaPageProcessor());
        ziruSpider.addUrl("https://bj.lianjia.com/zufang/pg" + pageNum + "/");
        ziruSpider.thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(2000));
        ziruSpider.run();
    }
}
