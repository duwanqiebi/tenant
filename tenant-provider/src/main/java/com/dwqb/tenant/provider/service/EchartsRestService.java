package com.dwqb.tenant.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.dwqb.tenant.api.model.TenantMsgResponse;
import com.dwqb.tenant.api.service.IEchartsRestService;
//import com.dwqb.tenant.core.echart.BarOption;
//import com.dwqb.tenant.core.echart.Node;
//import com.dwqb.tenant.core.es.ESUtils;
//import com.dwqb.tenant.core.model.Room;
//import com.dwqb.tenant.core.utils.JsonUtils2;
//import com.dwqb.tenant.core.utils.ObjUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(protocol = "rest")
@Path("tenant")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class EchartsRestService implements IEchartsRestService{

    @GET
    @Path("echart/getRegionPrice")
    public String getRegionPrice(){
//        String response = null;
//        String uri = "http://localhost:9200/echart/regionPrice/_search";
//
//        String result = ESUtils.curl(uri,"GET", null);
//
//        Map map = JsonUtils2.json2Obj(result, Map.class);
//        Map hits = (Map) map.get("hits");
//        int total = (int) hits.get("total");
//        if(total == 0){
//            return null;
//        }else{
//            List hitList = (List)hits.get("hits");
//            for(Object obj : hitList){
//                Map curMap = (Map)obj;
//                Room room = null;
//                try {
//                    Map source = (Map)curMap.get("_source");
//                    response = JsonUtils2.obj2Json(source);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return response;
        return "test";
    }
}
