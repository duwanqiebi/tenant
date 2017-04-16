package com.dwqb.tenant.core.model;

/**
 * Created by zhangqiang on 17/4/15.
 */
public class RoomBayes{

    private String roomOrigin;

    private String contractName;

    private String contractTel;

    private String region;

    private String dirction;

    private String roomType;

    private String floor;

    private String status;

    public RoomBayes(Room room){
        String per  = String.valueOf( (int)(room.getPrice() / room.getSpace())/10*10);
        this.roomOrigin = room.getRoomOrigin() + "/" +  per;
        this.contractName = room.getContractName();
        this.contractTel = room.getContractTel();
        this.region = room.getRegion() + "/" + per;
        this.dirction = room.getDirction() + "/" + per;
        this.roomType =room.getRoomType() + "/" + per;
        this.floor = room.getFloor() + "/" + per;
        this.status = room.getStatus();
    }

    public String getRoomOrigin() {
        return roomOrigin;
    }

    public void setRoomOrigin(String roomOrigin) {
        this.roomOrigin = roomOrigin;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDirction() {
        return dirction;
    }

    public void setDirction(String dirction) {
        this.dirction = dirction;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
