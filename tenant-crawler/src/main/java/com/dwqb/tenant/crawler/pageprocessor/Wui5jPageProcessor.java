package com.dwqb.tenant.crawler.pageprocessor;


import com.dwqb.tenant.core.model.RoomType;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Random;

public class Wui5jPageProcessor extends AbstractPageProcessor{

    @Override
    public void process(Page page) {
        String curUrl = page.getUrl().toString();
        Html html = page.getHtml();

        if(curUrl.contains("bj.5i5j.com/rent/n")){
            List<String> urls = html.xpath("//ul[@class='list-body']//h2").links().all();
            urls = this.removeDuplicate(urls);
            page.addTargetRequests(urls);
        }else{
            String rootName = html.xpath("//h1[@class='house-tit']/text()").get();
            String price = html.xpath("//span[@class='font-price']/text()").get();

            List<Selectable> detail = html.xpath("//ul[@class='house-info-2']/li").nodes();

            RoomType roomType = null;
            if(html.xpath("//span[@class='mr-r']/text()").get().contains("整租")){
                roomType = RoomType.parseRoomType(detail.get(0).xpath("//li/text()").get());
            }else{
                roomType = RoomType.WO_SHI;
            }

            String space = detail.get(2).xpath("//li/text()").get();
            space = space.replace("平米","");

            String derection = detail.get(4).xpath("//li/text()").get();
            String floor = detail.get(5).xpath("//li/text()").get();

//            List<String> imgList = html.
        }
    }


    public static void main(String[] args) {
        Spider ziruSpider = Spider.create(new Wui5jPageProcessor()).addUrl("http://bj.5i5j.com/rent/n1").thread(1);
        ziruSpider.setEmptySleepTime(new Random().nextInt(1000));
        ziruSpider.run();
    }
}
