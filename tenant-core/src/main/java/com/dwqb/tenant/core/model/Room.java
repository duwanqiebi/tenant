package com.dwqb.tenant.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zhangqiang on 16/12/18.
 */
public class Room implements Serializable{

    private String name;

    private Double price;

    private Double longitude;

    private Double latitude;

    private String priceType;

    private String status;  //是否可租

    private Double space;

    private String dirction;

    private String struct;

    private String floor;


    public Room(String name, Double price, Double longitude, Double latitude, String priceType, String status, Double space, String dirction, String struct, String floor) {
        this.name = name;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.priceType = priceType;
        this.status = status;
        this.space = space;
        this.dirction = dirction;
        this.struct = struct;
        this.floor = floor;
    }
}
