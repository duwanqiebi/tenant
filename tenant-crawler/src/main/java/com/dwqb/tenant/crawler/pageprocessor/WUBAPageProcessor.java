package com.dwqb.tenant.crawler.pageprocessor;

import com.alibaba.dubbo.common.utils.StringUtils;
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
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangqiang on 16/11/16.
 */
@Component
public class WUBAPageProcessor extends AbstractPageProcessor{

    private static Logger logger = LoggerFactory.getLogger(WUBAPageProcessor.class);

    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();

        if(curUrl.matches("[a-zA-z]+://bj.58.com[^s]*pn[\\d]+[^s]*")){         //目录页
            //获取明细url
            Selectable selectable = html.xpath("//div[@class='des']/h2/a").links();
            List<String> urls = selectable.all();

            urls = this.removeDuplicate(urls);
            page.addTargetRequests(urls);
            logger.info("添加明细共" + urls.size());
        }
        else{                                                              //详情页
            if(this.isDetailHandled(curUrl)){
                logger.info("已经抓取过" + curUrl);
                return;
            }

            char[] c = new char[1];
            c[0] = 160;
            String s=  new String(c);

            String roomName = html.xpath("//div[@class='house-title']/h1/text()").get();
            String price = html.xpath("//div[@class='house-pay-way f16']/span/b/text()").get();

            List<Selectable> detailList = html.xpath("//ul[@class='f14']/li").nodes();

            String struct = detailList.get(1).xpath("//li/span[2]/text()").get();
            String[] structs = struct.split(" ");
            struct = structs[0];
            RoomType roomType = RoomType.parseRoomType(struct);
            String space = structs[1].replaceAll(s,"");

            String derectionAndfloor = detailList.get(2).xpath("//li/span[2]/text()").get();
            derectionAndfloor = derectionAndfloor.replaceAll(s," ");
            String direction = null;
            String floor = null;
            if(derectionAndfloor.split(" ").length >= 3){
                direction = derectionAndfloor.split(" ")[0];
                floor = derectionAndfloor.split(" ")[2];
            }

            Double longitude =null;
            Double latitude=null;
            Region region=null;
            String xiaoqu = detailList.get(3).xpath("//li/span[2]/text()").get();
            if(StringUtils.isBlank(xiaoqu)){
                xiaoqu = detailList.get(3).xpath("//li/span[2]/a/text()").get();
            }
            if(StringUtils.isNotEmpty(xiaoqu)){
                Double[] zuobiao  = BaiduMapAPI.getLocation(xiaoqu);
                longitude = zuobiao[0];
                latitude = zuobiao[1];
                region = BaiduMapAPI.covertLocation(longitude,latitude);         //区域
            }



            String contractName = html.xpath("//p[@class='agent-name f16 pr']/a/text()").get();
            String contractTel = html.xpath("//em[@class='phone-num']/text()").get();
            String description = html.xpath("//div[@class='house-word-introduce f16 c_555']").get();
            List<String> imgList = html.xpath("//div[@class='basic-pic-list pr']//li/@data-src").all();


            Room room = new Room(null, RoomOrigin.WUBA.toString(),curUrl,contractName,contractTel,null,description,roomName,Double.parseDouble(price),longitude,latitude,region.toString(),null,null,Double.parseDouble(space),direction,struct,roomType != null? roomType.toString():struct,floor,imgList);
            Long id = IdGenerator.getId();
            room.setId(id);
            //es
            String json = JsonUtils2.obj2Json(room);
            ESUtils.curl("http://112.74.79.166:9200/room/room/" + String.valueOf(id) ,"PUT", JsonUtils2.obj2Json(room));

//            try {
//                hbase(room);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
            this.handled(curUrl);
        }



    }

    public  void doCrawer(String pageNum) {
        Spider ziruSpider = Spider.create(new WUBAPageProcessor());
        int num = Integer.parseInt(pageNum);
        while(num >= 1){
            ziruSpider.addUrl("http://bj.58.com/zufang/pn" + num);
            num --;
        }
        ziruSpider.thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(2000));
        ziruSpider.run();
    }

    public void doCrawerByNum(String s) {
        Spider ziruSpider = Spider.create(new WUBAPageProcessor());
        ziruSpider.addUrl("http://bj.58.com/zufang/pn" + s);
        ziruSpider.thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(4000));
        ziruSpider.run();
    }

    public static void main(String[] args){
        Spider ziruSpider = Spider.create(new WUBAPageProcessor());
        ziruSpider.addUrl("http://bj.58.com/zufang/pn" + 1);
        ziruSpider.thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(4000));
        ziruSpider.run();
    }
}
