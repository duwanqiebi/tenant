package com.dwqb.tenant.core.model;

/**
 * Created by zhangqiang on 17/3/17.
 */
public enum RoomOrigin {

    WUBA("58同城"),

    WO_AI_WO_JIA("我爱我家"),

    LIAN_JIA("链家"),

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
