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

    private static Logger logger = LoggerFactory.getLogger("WUBAPageProcessor");

    public void process(Page page) {
        String url = page.getUrl().toString();

        Html html;
        if(url.matches("[a-zA-z]+://bj.58.com[^s]*pn[\\d]+[^s]*")){         //目录页
            html = page.getHtml();

            //获取明细url
            Selectable selectable = html.xpath("body/div/div/div/table/tbody/tr/td/a").links();
            List<String> list = selectable.all();
            page.addTargetRequests(list);

            //获取下一页链接
            page.addTargetRequests(html.xpath("//a[@class=\"next\"]").all());
        }else{                                                              //详情页
            // TODO: 16/11/17 解析详情页
            logger.info(url);
        }



    }


    public static void main(String[] args) {

        Spider.create(new WUBAPageProcessor()).addUrl("http://bj.58.com/zufang/pn2/").thread(1).run();
    }
}
