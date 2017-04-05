package com.dwqb.tenant.provider.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.dwqb.tenant.api.model.TenantMsgResponse;
import com.dwqb.tenant.api.service.ITenantMsgService;
import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.model.*;
import com.dwqb.tenant.core.utils.JsonUtils2;
import com.dwqb.tenant.core.utils.ObjUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(protocol = "rest")
@Path("tenant")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TenantMsgRestService implements ITenantMsgService{

    Logger logger = LoggerFactory.getLogger(TenantMsgRestService.class);

    private static int size = 10;        //每页数量

    @POST
    @Path("search/{queryStr}/{num}")
    public String search(@PathParam("queryStr") String queryStr,@PathParam("num") String pageNum, String json) {

        SearchRequestModel requestJson = JsonUtils2.json2Obj(json,SearchRequestModel.class);

        Map queryWrap = new HashMap();
        Map query = new HashMap();
        Map bool =  new HashMap();
        List must =  new ArrayList();
        if(!"不限".equals(requestJson.getArea()) && !StringUtils.isBlank(requestJson.getArea())){
            Map term = new HashMap();
            term.put("region",requestJson.getArea());

            Map termWrap = new HashMap();
            termWrap.put("term",term);
            must.add(termWrap);
        }
        if(!"不限".equals(requestJson.getRoom()) && !StringUtils.isBlank(requestJson.getRoom())){
            Map match = new HashMap();
            match.put("roomType",requestJson.getRoom());

            Map matchWrap = new HashMap();
            matchWrap.put("match",match);
            must.add(matchWrap);
        }
        if(!"不限".equals(requestJson.getBrand()) && !StringUtils.isBlank(requestJson.getBrand())){
            Map term = new HashMap();
            term.put("roomOrigin",requestJson.getBrand());

            Map termWrap = new HashMap();
            termWrap.put("term",term);
            must.add(termWrap);
        }
        if(!"不限".equals(requestJson.getSubway()) && !StringUtils.isBlank(requestJson.getSubway())){
            Map match = new HashMap();
            match.put("subway",requestJson.getSubway());

            Map matchWrap = new HashMap();
            matchWrap.put("match",match);
            must.add(matchWrap);
        }
        if(!StringUtils.isBlank(queryStr) && !"null".equals(queryStr) && !queryStr.contains("号线")){
            Map match = new HashMap();
            match.put("name",queryStr);

            Map matchWrap = new HashMap();
            matchWrap.put("match",match);
            must.add(matchWrap);
        }
        if(!StringUtils.isBlank(queryStr) && !"null".equals(queryStr) && !queryStr.contains("号线")){
            Map match = new HashMap();
            match.put("description",queryStr);

            Map matchWrap = new HashMap();
            matchWrap.put("match",match);
            must.add(matchWrap);
        }
        if(!StringUtils.isBlank(queryStr) && !"null".equals(queryStr) && queryStr.contains("号线")){
            Map match = new HashMap();
            match.put("subway",queryStr);

            Map matchWrap = new HashMap();
            matchWrap.put("match",match);
            must.add(matchWrap);
        }
        bool.put("must",must);
        query.put("bool",bool);

        //高亮
        Map highlightWrap = new HashMap();
        Map highlight = new HashMap();
        List pre_tags = new ArrayList();
        pre_tags.add("<em class=\"c_color\">");
        Map pre_tagsWrap = new HashMap();
        pre_tagsWrap.put("pre_tags",pre_tags);
        List post_tags = new ArrayList();
        post_tags.add("</em>");
        Map post_tagsWrap = new HashMap();
        post_tagsWrap.put("post_tags",post_tags);
        Map fields = new HashMap();
        fields.put("name",new HashMap<>());
        fields.put("subway",new HashMap<>());
        fields.put("region",new HashMap<>());
        fields.put("description",new HashMap<>());

        highlight.put("pre_tags",pre_tags);
        highlight.put("post_tags",post_tags);
        highlight.put("fields",fields);

        queryWrap.put("query",query);
        queryWrap.put("highlight",highlight);
        queryWrap.put("size",size);
        queryWrap.put("from",size * (Integer.parseInt(pageNum) - 1));


        TenantMsgResponse response = null;

        String uri = "http://localhost:9200/room/room/_search";

//        EsModel esModel = new EsModel(new EsQueryModel(new EsMatchModel(queryStr)),Integer.parseInt(pageNum) * num,num);
        logger.info("es request json " + JsonUtils2.obj2Json(queryWrap));

        String result = ESUtils.curl(uri,"POST", JsonUtils2.obj2Json(queryWrap));

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

                    //高亮字段解析
                    Map highlightMap = (Map) curMap.get("highlight");

                    List nameList = (List) highlightMap.get("name");
                    if(!CollectionUtils.isEmpty(nameList)){
                        room.setName(nameList.get(0).toString());
                    }

                    List subwayList = (List) highlightMap.get("subway");
                    if(!CollectionUtils.isEmpty(subwayList)){
                        room.setSubway(subwayList);
                    }



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


    public static void main(String[] args){
        String json = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"region\":\"大兴区\"}},{\"match\":{\"roomType\":\"2室1厅\"}},{\"term\":{\"roomOrigin\":\"自如\"}},{\"match\":{\"subway\":\"4号线\"}}]}}}";
        Map map = JsonUtils2.json2Obj(json,Map.class);
        System.out.println(1);
    }
}
