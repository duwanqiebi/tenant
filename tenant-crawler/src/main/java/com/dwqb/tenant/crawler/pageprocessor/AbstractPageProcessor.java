package com.dwqb.tenant.crawler.pageprocessor;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

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


    protected List<String> removeDuplicate(List<String> list){
        List<String> result = new ArrayList<>();
        for(String str : list){
            if(!result.contains(str)){
                result.add(str);
            }
        }
        return result;
    }
}
