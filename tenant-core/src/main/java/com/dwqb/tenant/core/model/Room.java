package com.dwqb.tenant.core.model;

import java.io.Serializable;
import java.util.List;


public class Room implements Serializable{

    private Long id;

    private String roomOrigin;

    private String url;

    private String contractName;

    private String contractTel;

    private List<String> subway;

    private String description;

    private String name;

    private Double price;

    private Double longitude;

    private Double latitude;

    private String region;

    private String priceType;

    private String status;  //是否可租

    private Double space;

    private String dirction;

    private String struct;

    private String roomType;

    private String floor;

    private List<String> imgList;

    public Room() {
    }



    public Room(String roomOrigin, String url, String name, Double price, Double longitude, Double latitude, String region, String priceType, String status, Double space, String dirction, String struct, String roomType, String floor, List<String> imgList) {
        this.roomOrigin = roomOrigin;
        this.url = url;
        this.name = name;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.region = region;
        this.priceType = priceType;
        this.status = status;
        this.space = space;
        this.dirction = dirction;
        this.struct = struct;
        this.roomType = roomType;
        this.floor = floor;
        this.imgList = imgList;
    }

    public Room(Long id, String roomOrigin, String url, String contractName, String contractTel, List<String> subway, String name, Double price, Double longitude, Double latitude, String region, String priceType, String status, Double space, String dirction, String struct, String roomType, String floor, List<String> imgList) {
        this.id = id;
        this.roomOrigin = roomOrigin;
        this.url = url;
        this.contractName = contractName;
        this.contractTel = contractTel;
        this.subway = subway;
        this.name = name;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.region = region;
        this.priceType = priceType;
        this.status = status;
        this.space = space;
        this.dirction = dirction;
        this.struct = struct;
        this.roomType = roomType;
        this.floor = floor;
        this.imgList = imgList;
    }

    public Room(Long id, String roomOrigin, String url, String name, Double price, Double longitude, Double latitude, String region, String priceType, String status, Double space, String dirction, String struct, String roomType, String floor, List<String> imgList) {
        this.id = id;
        this.roomOrigin = roomOrigin;
        this.url = url;
        this.name = name;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.region = region;
        this.priceType = priceType;
        this.status = status;
        this.space = space;
        this.dirction = dirction;
        this.struct = struct;
        this.roomType = roomType;
        this.floor = floor;
        this.imgList = imgList;
    }

    public Room(Long id, String roomOrigin, String url, String contractName, String contractTel, List<String> subway, String description, String name, Double price, Double longitude, Double latitude, String region, String priceType, String status, Double space, String dirction, String struct, String roomType, String floor, List<String> imgList) {
        this.id = id;
        this.roomOrigin = roomOrigin;
        this.url = url;
        this.contractName = contractName;
        this.contractTel = contractTel;
        this.subway = subway;
        this.description = description;
        this.name = name;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.region = region;
        this.priceType = priceType;
        this.status = status;
        this.space = space;
        this.dirction = dirction;
        this.struct = struct;
        this.roomType = roomType;
        this.floor = floor;
        this.imgList = imgList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomOrigin() {
        return roomOrigin;
    }

    public void setRoomOrigin(String roomOrigin) {
        this.roomOrigin = roomOrigin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractTel() {
        return contractTel;
    }

    public void setContractTel(String contractTel) {
        this.contractTel = contractTel;
    }

    public List<String> getSubway() {
        return subway;
    }

    public void setSubway(List<String> subway) {
        this.subway = subway;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
