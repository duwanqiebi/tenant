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
import com.dwqb.tenant.core.utils.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class AnjukePageProcessor extends AbstractPageProcessor{

    private static Logger logger = LoggerFactory.getLogger(AnjukePageProcessor.class);


    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();

        if(curUrl.contains("bj.zu.anjuke.com/fangyuan/p")){
            //目录页
            List<String> urls = html.xpath("//div[@class='zu-itemmod']/@link").all();
            urls = this.removeDuplicate(urls);
            page.addTargetRequests(urls);
        }else{          //详情页
            if(this.isDetailHandled(curUrl)){
                logger.info("已经抓取过" + curUrl);
                return;
            }
            String roomName = html.xpath("//div[@class='tit cf']/h3/text()").get();
            List<Selectable> detail = html.xpath("//dl[@class='p_phrase cf']").nodes();
            String price = detail.get(0).xpath("//dd[@class='og']/strong/span/text()").get();
            String priceType = detail.get(1).xpath("//dd/text()").get().trim();
            String struct = detail.get(2).xpath("//dd/text()").get();
            String zulinfangshi  = detail.get(3).xpath("//dd/text()").get();
            RoomType roomType = null;
            if(zulinfangshi != null && zulinfangshi.contains("整租")){
                roomType = RoomType.parseRoomType(struct);
            }else{
                roomType = RoomType.WO_SHI;
            }
            String space = detail.get(7).xpath("//dd/text()").get();
            String direction = detail.get(8).xpath("//dd/text()").get();
            String floor = detail.get(9).xpath("//dd/text()").get();
            String mapUrl = detail.get(13).xpath("//dd/a/@href").get();
            Map<String,String> paramMap = URLUtils.URLRequest(mapUrl);
            Double latitude = Double.parseDouble(paramMap.get("l1"));
            Double longitude= Double.parseDouble(paramMap.get("l2"));
            Region region = BaiduMapAPI.covertLocation(longitude,latitude);         //区域

            //img
            List<String> imgList = new ArrayList<>();
            List<Selectable> imgSelects = html.xpath("//div[@class='bigps photoslide cf']").nodes().get(0).xpath("//div[@class='picCon']//ul/li").nodes();
            for(Selectable selectable : imgSelects){
                String img = selectable.xpath("//img/@data-src").get();
                if(StringUtils.isBlank(img)){
                    img = selectable.xpath("//img/@src").get();
                }
                if(!StringUtils.isBlank(img)){
                    imgList.add(img);
                }
            }

            String contractName = html.xpath("//h2[@id='broker_true_name']/text()").get();
            String contractTel = html.xpath("//p[@class='broker-mobile']/text()").get().replace(" ","");

            String description = html.xpath("//div[@id='propContent']").toString();

            Long id = IdGenerator.getId();
            Room room = new Room(id, RoomOrigin.AN_JU_KE.toString(),curUrl,contractName,contractTel,null,description,roomName,Double.parseDouble(price),longitude,latitude,region.toString(),priceType,null,Double.parseDouble(space.replaceAll("\\D", "")),direction,struct,roomType != null? roomType.toString():struct,floor,imgList);


            //es
            String json = JsonUtils2.obj2Json(room);
            ESUtils.curl("http://112.74.79.166:9200/room/room/" + String.valueOf(id) ,"PUT", JsonUtils2.obj2Json(room));

//            try {
//                hbase(room);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            this.handled(curUrl);
        }
    }


    public static void doCrawer(String pageNum) {
        Spider ziruSpider = Spider.create(new AnjukePageProcessor());
        int num = Integer.parseInt(pageNum);
        while(num >= 1){
            ziruSpider.addUrl("http://bj.zu.anjuke.com/fangyuan/p" + num + "/");
            num --;
        }
        ziruSpider.thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(4000));
        ziruSpider.run();
    }

    public void doCrawerByNum(String pageNum) {
        Spider ziruSpider = Spider.create(new AnjukePageProcessor());
        ziruSpider.addUrl("http://bj.zu.anjuke.com/fangyuan/p" + pageNum + "/");
        ziruSpider.thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(2000));
        ziruSpider.run();
    }

    public static void main(String[] args){
        Spider ziruSpider = Spider.create(new AnjukePageProcessor());
        ziruSpider.addUrl("http://bj.zu.anjuke.com/fangyuan/p1/");
        ziruSpider.thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(2000));
        ziruSpider.run();
    }
}
