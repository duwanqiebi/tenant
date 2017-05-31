package com.dwqb.tenant.core.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by zhangqiang on 17/3/30.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SearchRequestModel implements Serializable {

    private String area;

    private String room;

    private String type;

    private String brand;

    private String subway;

    private String price;

    private String space;

    public SearchRequestModel() {
    }

    public SearchRequestModel(String area, String room, String type, String brand, String subway, String price, String space) {
        this.area = area;
        this.room = room;
        this.type = type;
        this.brand = brand;
        this.subway = subway;
        this.price = price;
        this.space = space;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSubway() {
        return subway;
    }

    public void setSubway(String subway) {
        this.subway = subway;
    }
}
