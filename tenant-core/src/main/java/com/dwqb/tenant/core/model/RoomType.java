package com.dwqb.tenant.core.model;

/**
 * Created by zhangqiang on 17/3/12.
 */
public enum RoomType {

    WO_SHI("卧室"),

    OPEN("开间"),

    SINGLE("一居室"),

    TWO("两室一厅");

    private String roomType;

    RoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}
