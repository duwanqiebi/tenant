package com.dwqb.tenant.crawler.pageprocessor;

import org.apache.http.HttpHost;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhangqiang on 16/11/16.
 */
public abstract class AbstractPageProcessor implements PageProcessor{

    protected Site site;

    public AbstractPageProcessor(){
        site = Site.me().
                setUserAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)")
                .setSleepTime(1000);
//        List<String[]> list = new ArrayList<String[]>();
//        list.add(new String[]{"60.206.148.135","3128"});
//        list.add(new String[]{"60.207.239.247","3128"});
//        list.add(new String[]{"124.206.167.250","3128"});
//        list.add(new String[]{"124.206.167.250","3128"});
//        list.add(new String[]{"60.206.229.152","3128"});
//        site.setHttpProxyPool(list);
//        site.enableHttpProxyPool();
    }

    public Site getSite() {
        return this.site;
    }
}
