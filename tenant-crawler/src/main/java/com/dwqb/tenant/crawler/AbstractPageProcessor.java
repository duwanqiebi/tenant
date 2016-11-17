package com.dwqb.tenant.crawler;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by zhangqiang on 16/11/16.
 */
public abstract class AbstractPageProcessor implements PageProcessor{

    protected Site site;

    public AbstractPageProcessor(){
        site = Site.me();
    }

    public Site getSite() {
        return this.site;
    }
}
