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
import org.wltea.analyzer.sample.IKAnalzyerDemo;

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

//        List<String> queryList = IKAnalzyerDemo.analzyer(queryStr);

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

        String uri = "http://112.74.79.166:9200/room/room/_search";

//        EsModel esModel = new EsModel(new EsQueryModel(new EsMatchModel(queryStr)),Integer.parseInt(pageNum) * num,num);
        logger.info("es request json " + JsonUtils2.obj2Json(queryWrap));

        String result = ESUtils.curl(uri,"POST", JsonUtils2.obj2Json(queryWrap));
        logger.info(result);
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
                    Map source = (Map)curMap.get("_source");
//                    logger.info(source.get("subway").toString());
                    if(source.get("subway") == null || "null".equals(source.get("subway"))){
                        source.put("subway",new ArrayList<String>());
                    }
                    room = (Room) ObjUtils.mapToObject(source,Room.class);

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
                    logger.error("",e);
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
        String json = "{\n" +
                "          \"id\": 1668,\n" +
                "          \"roomOrigin\": \"安居客\",\n" +
                "          \"url\": \"http://bj.zu.anjuke.com/fangyuan/1064876755\",\n" +
                "          \"contractName\": \"张智博\",\n" +
                "          \"contractTel\": \"18710206748\",\n" +
                "          \"subway\": null,\n" +
                "          \"description\": \"<div class=\\\"pro_main cf\\\" id=\\\"propContent\\\" style=\\\"\\\"> \\n <div class=\\\"pro_con wb\\\">\\n  蛋壳2周年，新春送大礼！三重优惠，重大来袭！蛋壳签约两年保证不涨租！蛋壳签约两年再减免1200元！蛋壳签约两年中途可以随时退换！选择蛋壳，选择美好生活！（详情请电话*管家）[温馨提示]房间照片*实拍，拎包入住！看房无*，租房无*。看房请直接来电!蛋壳公寓回归家的温暖，让你在异乡不在流浪【房源详情】您好，这套房子是三室一厅，主卧朝南，带飘窗，采光极好16平米，飘窗面积很大，靠在飘窗上喝杯咖啡，看书听音乐，是种很舒适的享受，房间配置全新家具，家电，双周免费保洁，小区绿化30%以上，可随时可看房，此房仅此一间哦。【小区周边】\\n </div> \\n</div>\",\n" +
                "          \"name\": \"蛋壳公寓特价房源，仅此一间 免费网络可月付后一天\",\n" +
                "          \"price\": 2100,\n" +
                "          \"longitude\": 116.46293,\n" +
                "          \"latitude\": 39.940168,\n" +
                "          \"region\": \"朝阳区\",\n" +
                "          \"priceType\": \"付1押1\",\n" +
                "          \"status\": null,\n" +
                "          \"space\": 15,\n" +
                "          \"dirction\": \"南\",\n" +
                "          \"struct\": \"3室1厅1卫\",\n" +
                "          \"roomType\": \"卧室\",\n" +
                "          \"floor\": \"5/12\",\n" +
                "          \"imgList\": [\n" +
                "            \"http://b.pic1.ajkimg.com/display/hj/10f0b387b6fd9cf0cf790cf0ea09fb77/600x450.jpg?t=1\",\n" +
                "            \"http://d.pic1.ajkimg.com/display/hj/c4f3787df6bb425d09b17c8f7fab47b6/600x450.jpg?t=1\",\n" +
                "            \"http://c.pic1.ajkimg.com/display/hj/6c391f1d31455bc77da745cc5f193f22/600x450.jpg?t=1\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://b.pic1.ajkimg.com/display/hj/10f0b387b6fd9cf0cf790cf0ea09fb77/67x50c.jpg?t=1\",\n" +
                "            \"http://d.pic1.ajkimg.com/display/hj/c4f3787df6bb425d09b17c8f7fab47b6/67x50c.jpg?t=1\",\n" +
                "            \"http://c.pic1.ajkimg.com/display/hj/6c391f1d31455bc77da745cc5f193f22/67x50c.jpg?t=1\",\n" +
                "            \"http://b.pic1.ajkimg.com/display/hj/e5d5e0e1ad5f57db2af982acebb45a23/67x50c.jpg?t=1\",\n" +
                "            \"http://c.pic1.ajkimg.com/display/hj/bc78dd354309d66db5b3c4e3801e8ef8/67x50c.jpg?t=1\",\n" +
                "            \"http://b.pic1.ajkimg.com/display/hj/19ad20fc1fb418641deae088b3fb1cfe/67x50c.jpg?t=1\",\n" +
                "            \"http://c.pic1.ajkimg.com/display/hj/214dfd43c061733adcc6899be16c6391/67x50c.jpg?t=1\",\n" +
                "            \"http://d.pic1.ajkimg.com/display/hj/32d1262d426e7d077977996ffe198422/67x50c.jpg?t=1\",\n" +
                "            \"http://d.pic1.ajkimg.com/display/hj/705b50fab6e5ce467d78fa9788525911/67x50c.jpg?t=1\",\n" +
                "            \"http://d.pic1.ajkimg.com/display/hj/c8bda12b2910d00b79a431592c95b539/67x50c.jpg?t=1\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pages.anjukestatic.com/usersite/site/img/global/1/bg_default.jpg\",\n" +
                "            \"http://pic1.ajkimg.com/display/hj/10:46393800/67x50.jpg\",\n" +
                "            \"http://b.pic1.ajkimg.com/display/hj/920eb70fc31d5fd67f12720d8f6f2dde/67x50.jpg\",\n" +
                "            \"http://a.pic1.ajkimg.com/display/hj/0d1534e569c7df7c4a716e6f8ccd5dbc/67x50.jpg\",\n" +
                "            \"http://pic1.ajkimg.com/display/hj/849e5c592c3c9424773c902dd97ea815/67x50c.jpg?t=1\",\n" +
                "            \"http://pic1.ajkimg.com/display/hj/01aa02e6a16cee6ab41a913404e0de6d/67x50c.jpg?t=1\",\n" +
                "            \"http://pic1.ajkimg.com/display/hj/5422a8fe688162985a4c0b1f103ebcb0/67x50c.jpg?t=1\",\n" +
                "            \"http://pic1.ajkimg.com/display/hj/dc20e41874d345b23caae39d529e2e66/67x50c.jpg?t=1\",\n" +
                "            \"http://pic1.ajkimg.com/display/hj/ad21b7045fcd97538f6f861be13c9f06/67x50c.jpg?t=1\",\n" +
                "            \"http://images12.anjukestatic.com/community/20100719/1/11/1/83/85/111018385/67x50c.jpg\",\n" +
                "            \"http://images12.anjukestatic.com/community/20100719/1/11/1/83/74/111018374/67x50c.jpg\"\n" +
                "          ]\n" +
                "        }";
        Map map = JsonUtils2.json2Obj(json,Map.class);
        try {
            Room room = (Room) ObjUtils.mapToObject(map,Room.class);
            System.out.println(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(1);

    }
}
