package com.dwqb.tenant.worker;


import com.dwqb.tenant.core.echart.BarOption;
import com.dwqb.tenant.core.echart.Node;
import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.*;
import com.dwqb.tenant.core.utils.JsonUtils2;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RegionPrice {

    public static BigDecimal getRegionPrice(Region region, RoomType roomType){
        String uri = "http://localhost:9200/room/room/_search";
        EsModel esModel = new EsModel(new EsQueryModel(new EsMatchModel(null,region.toString(),roomType.toString())));
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
                BigDecimal price = new BigDecimal((Double)source.get("price"));
                totalPrice.add(price);
            }
            return totalPrice.divide(new BigDecimal(total));
        }
    }

    public static void main(String[] args){

        Node<String> legend = new Node<>(null,new String[RoomType.values().length],null);
        int legendIndex = 0;

        Node<String>[] xAxis = new Node[1];
        xAxis[0] = new Node(null,new String[Region.values().length],"category");


        Node<Double>[] series = new Node[RoomType.values().length];

        for(RoomType roomType : RoomType.values()){
            legend.setData(roomType.toString(),legendIndex);

            Node<Double> seriesNode = new Node<>(roomType.toString(), new Double[Region.values().length], "bar");

            int xAxisIndex = 0;
            for(Region region : Region.values()){
                xAxis[0].setData(region.toString(), xAxisIndex );

                BigDecimal avgPrice = RegionPrice.getRegionPrice(region, roomType);
                seriesNode.setData(avgPrice.doubleValue(), xAxisIndex);
                xAxisIndex ++;
            }

            series[legendIndex] = seriesNode;
            legendIndex ++;
        }

        BarOption option = new BarOption(legend,xAxis,series);

        ESUtils.curl("http://localhost:9200/echart/regionPrice","POST",JsonUtils2.obj2Json(option));
    }
}
