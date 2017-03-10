package com.dwqb.tenant.core.model;

import java.io.Serializable;
import java.util.List;

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

    private List<String> imgList;

    public Room() {
    }

    public Room(String name, Double price, Double longitude, Double latitude, String priceType, String status, Double space, String dirction, String struct, String floor, List<String> imgList) {
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
        this.imgList = imgList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getSpace() {
        return space;
    }

    public void setSpace(Double space) {
        this.space = space;
    }

    public String getDirction() {
        return dirction;
    }

    public void setDirction(String dirction) {
        this.dirction = dirction;
    }

    public String getStruct() {
        return struct;
    }

    public void setStruct(String struct) {
        this.struct = struct;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }
}
