package com.dwqb.tenant.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.dwqb.tenant.api.service.IEchartsRestService;
import com.dwqb.tenant.core.echart.BarOption;
import com.dwqb.tenant.core.echart.Node;
import com.dwqb.tenant.core.utils.JsonUtils2;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Service(protocol = "rest")
@Path("tenant")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class EchartsRestService implements IEchartsRestService{

    @GET
    @Path("echart/getRegionPrice")
    public String getRegionPrice(){
        int num = 3;

        Node<String> legend = new Node<>(null,new String[]{"一居室","二居室","三居室"},null);
        Node<String>[] xAxis = new Node[1];
        xAxis[0] = new Node(null,new String[]{"海淀区","西城区","东城区"},"category");
        Node<Integer>[] series = new Node[3];
        series[0] = new Node<>("一居室",new Integer[]{100,200,300},"bar");
        series[1] = new Node<>("二居室",new Integer[]{100,200,300},"bar");
        series[2] = new Node<>("三居室",new Integer[]{100,200,300},"bar");

        BarOption option = new BarOption(legend,xAxis,series);
        return JsonUtils2.obj2Json(option);
    }
}
