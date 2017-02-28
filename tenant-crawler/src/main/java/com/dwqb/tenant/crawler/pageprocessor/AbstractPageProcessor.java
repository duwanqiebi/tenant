package com.dwqb.tenant.crawler.pageprocessor;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by zhangqiang on 16/11/16.
 */
public abstract class AbstractPageProcessor implements PageProcessor{

    protected Site site;

    public AbstractPageProcessor(){
        site = Site.me()
                .setUserAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)")
                .setSleepTime(1000);
    }

    public Site getSite() {
        return this.site;
    }
}
