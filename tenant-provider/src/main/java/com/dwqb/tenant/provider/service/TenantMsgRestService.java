package com.dwqb.tenant.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.dwqb.tenant.api.service.ITenantMsgService;
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
        System.out.println(queryStr);
        System.out.println(pageNum);
        return null;
    }
}
