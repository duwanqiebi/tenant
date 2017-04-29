package com.dwqb.tenant.crawler.pageprocessor;

import com.dwqb.tenant.core.model.Room;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
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

    public void hbase(Room room) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HTable hTable = new HTable(conf, "room");
        Put p = new Put(Bytes.toBytes(room.getId()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("contractName"),Bytes.toBytes(room.getContractName()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("contractTel"),Bytes.toBytes(room.getContractTel()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("description"),Bytes.toBytes(room.getDescription()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("direction"),Bytes.toBytes(room.getDirction()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("floor"),Bytes.toBytes(room.getFloor()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("latitude"),Bytes.toBytes(room.getLatitude()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("longtitude"),Bytes.toBytes(room.getLongitude()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes(room.getName()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("price"),Bytes.toBytes(room.getPrice()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("priceType"),Bytes.toBytes(room.getPriceType()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("region"),Bytes.toBytes(room.getRegion()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("roomOrigin"),Bytes.toBytes(room.getRoomOrigin()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("roomType"),Bytes.toBytes(room.getRoomType()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("space"),Bytes.toBytes(room.getSpace()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("status"),Bytes.toBytes(room.getStatus()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("struct"),Bytes.toBytes(room.getStruct()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("url"),Bytes.toBytes(room.getUrl()));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("imgList"),Bytes.toBytes(StringUtils.join(room.getImgList(),",")));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("subway"),Bytes.toBytes( StringUtils.join(room.getSubway(),",") ));
        hTable.put(p);
        hTable.close();
    }

    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        HTable hTable = new HTable(conf, "room");
        Put p = new Put(Bytes.toBytes("1"));
        p.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes("1111"));
        hTable.put(p);
    }
}
