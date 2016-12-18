package com.dwqb.tenant.crawler.pageprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import java.util.List;

/**
 * Created by zhangqiang on 16/11/16.
 */
public class WUBAPageProcessor extends AbstractPageProcessor{

    private static Logger logger = LoggerFactory.getLogger(WUBAPageProcessor.class);

    public void process(Page page) {
        //System.out.println(getSite().getHttpProxyPool().getProxy().getAddress().toString());
        String url = page.getUrl().toString();

        Html html;
        if(url.matches("[a-zA-z]+://bj.58.com[^s]*pn[\\d]+[^s]*")){         //目录页
            html = page.getHtml();

            //获取明细url
            Selectable selectable = html.xpath("body/div/div/div/table/tbody/tr/td/a").links();
            List<String> list = selectable.all();
            page.addTargetRequests(list);

            //获取下一页链接
            page.addTargetRequests(html.xpath("//*[@id=\"bottom_ad_li\"]/div[2]/a[@class=\"next\"]").all());
        }else if(url.matches("")){

        }

        else{                                                              //详情页
            // TODO: 16/11/17 解析详情页
            //logger.info(url);
            html = page.getHtml();
            Selectable main = html.xpath("body/div[@class=\"main-wrap\"]");
            /* 房源题目 */
            //Selectable houseTitle = main.select("div[@class=\"house-title\"]/h1/");
            //String title = houseTitle.xpath("h1/text()").toString();
            //System.out.println("title = " + title);
            //String info = houseTitle.xpath("p/text()").toString();
            //int index = info.indexOf("更新");
            //String updateTime = info.substring(index - 19,index);
            //System.out.println("info = " + info);
            //System.out.println("updateTime = " + updateTime);

            /* 房源基本信息 */
            Selectable basicInfo = main.xpath("div[@class=\"house-basic-info\"]");
            String price = basicInfo.xpath("div[@class=\"house-basic-right fr\"]/div[@class=\"house-basic-desc\"]/div[@class=\"house-desc-item fl c_333\"]/div[@class=\"house-pay-way f16\"]/sapn[@class=\"c_ff552e\"]/b/text()").toString();
            System.out.println("price = " + price);

        }



    }


    public static void main(String[] args) {

        Spider.create(new WUBAPageProcessor()).addUrl("http://bj.58.com/zufang/pn2/").thread(1).run();
    }
}
