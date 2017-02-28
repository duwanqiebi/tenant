package com.dwqb.tenant.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.dwqb.tenant.api.service.ITenantMsgService;
import com.dwqb.tenant.core.es.ESUtils;
import com.dwqb.tenant.core.utils.JsonUtils2;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Service(protocol = "rest")
@Path("tenant")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class TenantMsgRestService implements ITenantMsgService{

    @GET
    @Path("search/{queryStr}/{num}")
    public String search(@PathParam("queryStr") String queryStr,@PathParam("num") String pageNum) {

//        String json = ESUtils.curl("http://localhost:9200/room/room/1","GET",null);
//    String json = "{\"page_num\":\"1\",\"houses\":[{\"url\":\"http://baidu.com\",\"title\":\"test\",\"text\":\"text\",\"pub_time\":\"20170222\",\"dizhi\":\"test\"}]}\n";
        String json = "{\"page_num\":\"1\",\"houses\":[{\"url\":\"http://baidu.com\",\"title\":\"test\",\"text\":\"text\",\"pub_time\":\"20170222\",\"dizhi\":[100,200],\"ditie\":[10],\"jushi\":\"jushi\",\"shouji\":\"shouji\",\"zujin\":\"租金\",\"links\":\"baidu.com\",\"images\":\"baidu.com\",\"confidence\":\"confidence\"}]}\n";
        return json;
    }
}
