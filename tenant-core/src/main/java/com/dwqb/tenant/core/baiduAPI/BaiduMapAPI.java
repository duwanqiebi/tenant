package com.dwqb.tenant.core.baiduAPI;

import com.dwqb.tenant.core.model.Region;
import com.dwqb.tenant.core.utils.HttpClientUtils;
import com.dwqb.tenant.core.utils.JsonUtils2;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhangqiang on 17/3/11.
 */
public class BaiduMapAPI {

    private static final String ak = "fPHf9fN80YS715i7Tqmgp2d7LSnDEbGc";

    public static Region covertLocation(Double longitude, Double latitude){
        Region region = null;
        StringBuffer url = new StringBuffer("http://api.map.baidu.com/geocoder/v2/?location=");
        url.append(latitude).append(",").append(longitude).append("&output=json&ak=")
                .append(ak);
        try {
            String json = HttpClientUtils.post(url.toString(),null,null);
            Map map = JsonUtils2.json2Obj(json, Map.class);
            Map addressComponentMap = (Map) ((Map)map.get("result")).get("addressComponent");
            region = Region.getRegion( (String) addressComponentMap.get("district"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return region;
    }

    public static Double[] getLocation(String location){
        StringBuffer url = new StringBuffer("http://api.map.baidu.com/geocoder/v2/?address=");
        url.append(location).append("&output=json&ak=").append(ak);
        Double[] result = new Double[2];
        try {
            String json = HttpClientUtils.post(url.toString(),null,null);
            Map map = JsonUtils2.json2Obj(json, Map.class);
            Map locationmap = (Map) ((Map)map.get("result")).get("location");
            Double lng = Double.parseDouble(locationmap.get("lng").toString());
            Double lat = Double.parseDouble(locationmap.get("lat").toString());
            result[0] = lng;
            result[1] = lat;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
