package com.dwqb.tenant.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17/3/12.
 */
public enum RoomType {

    WO_SHI("卧室"),

    OPEN("开间"),

    SINGLE("一居室"),

    TWO("2室1厅");

    private String roomType;

    private static Map<String, RoomType> map = new HashMap<String, RoomType>();
    static {
        for (RoomType type : RoomType.values()) {
            map.put(type.getRoomType(), type);
        }
    }

    RoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public static RoomType parseRoomType(String type){
        return map.get(type);
    }

    @Override
    public String toString() {
        return roomType;
    }
}
