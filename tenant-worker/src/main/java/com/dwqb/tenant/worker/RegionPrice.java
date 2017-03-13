package com.dwqb.tenant.worker;


import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.*;
import com.dwqb.tenant.core.utils.JsonUtils2;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RegionPrice {

    public static BigDecimal getRegionPrice(Region region){
        String uri = "http://localhost:9200/room/room/_search";
        EsModel esModel = new EsModel(new EsQueryModel(new EsMatchModel(null,region.toString())));
        String result = ESUtils.curl(uri,"GET", JsonUtils2.obj2Json(esModel));
        Map map = JsonUtils2.json2Obj(result, Map.class);
        Map hits = (Map) map.get("hits");
        int total = (int) hits.get("total");
        if(total == 0){
            return BigDecimal.ZERO;
        }else{
            List hitList = (List)hits.get("hits");
            BigDecimal totalPrice = BigDecimal.ZERO;
            for(Object obj : hitList){
                Map curMap = (Map)obj;
                Map source = (Map)curMap.get("_source");
                BigDecimal price = new BigDecimal((String) source.get("price"));
                totalPrice.add(price);
            }
            return totalPrice.divide(new BigDecimal(total));
        }
    }

    public static void main(String[] args){
        for(Region region : Region.values()){
            BigDecimal avgPrice = RegionPrice.getRegionPrice(region);

        }


    }
}
