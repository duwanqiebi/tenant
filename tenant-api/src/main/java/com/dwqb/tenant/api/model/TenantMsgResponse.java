package com.dwqb.tenant.api.model;

import com.dwqb.tenant.core.model.Room;

import java.io.Serializable;
import java.util.List;


public class TenantMsgResponse implements Serializable{

    List<Room> houses;

    Integer total;

    Integer pageNum;

    public TenantMsgResponse(List<Room> houses, Integer total, Integer pageNum) {
        this.houses = houses;
        this.total = total;
        this.pageNum = pageNum;
    }

    public List<Room> getHouses() {
        return houses;
    }

    public void setHouses(List<Room> houses) {
        this.houses = houses;
    }
}
