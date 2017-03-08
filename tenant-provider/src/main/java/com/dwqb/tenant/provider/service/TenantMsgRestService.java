package com.dwqb.tenant.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.dwqb.tenant.api.model.TenantMsgResponse;
import com.dwqb.tenant.api.service.ITenantMsgService;
import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.Room;
import com.dwqb.tenant.core.utils.JsonUtils2;
import com.dwqb.tenant.core.utils.ObjUtils;
import com.dwqb.tenant.provider.model.EsMatchModel;
import com.dwqb.tenant.provider.model.EsModel;
import com.dwqb.tenant.provider.model.EsQueryModel;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service(protocol = "rest")
@Path("tenant")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TenantMsgRestService implements ITenantMsgService{

    private static int num = 10;        //每页数量

    @GET
    @Path("search/{queryStr}/{num}")
    public String search(@PathParam("queryStr") String queryStr,@PathParam("num") String pageNum) {

        TenantMsgResponse response = null;

        String uri = "http://localhost:9200/room/room/_search";

        EsModel esModel = new EsModel(new EsQueryModel(new EsMatchModel("queryStr")),Integer.parseInt(pageNum) * num,num);

        String result = ESUtils.curl(uri,"GET", JsonUtils2.obj2Json(esModel));

        Map map = JsonUtils2.json2Obj(result, Map.class);
        Map hits = (Map) map.get("hits");
        int total = (int) hits.get("total");
        if(total == 0){
            return null;
        }else{
            List<Room> roomList = new ArrayList(total);
            List hitList = (List)hits.get("hits");
            for(Object obj : hitList){
                Map curMap = (Map)obj;
                Room room = null;
                try {
                    room = (Room) ObjUtils.mapToObject((Map)curMap.get("_source"),Room.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Room room = (Room)curMap.get("_source");
                roomList.add(room);
                response = new TenantMsgResponse(roomList,total,Integer.parseInt(pageNum));
            }
        }

        return JsonUtils2.obj2Json(response);
    }
}
