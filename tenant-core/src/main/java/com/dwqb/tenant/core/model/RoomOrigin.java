package com.dwqb.tenant.core.model;

/**
 * Created by zhangqiang on 17/3/17.
 */
public enum RoomOrigin {

    ZI_RU("自如"),

    AN_JU_KE("安居客");

    private String roomOrigin;

    RoomOrigin(String roomOrigin) {
        this.roomOrigin = roomOrigin;
    }

    public String getRoomOrigin() {
        return roomOrigin;
    }

    public void setRoomOrigin(String roomOrigin) {
        this.roomOrigin = roomOrigin;
    }


    @Override
    public String toString() {
        return roomOrigin;
    }
}
