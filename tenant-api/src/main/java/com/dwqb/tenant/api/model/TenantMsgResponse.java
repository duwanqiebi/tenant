package com.dwqb.tenant.api.model;

import com.dwqb.tenant.core.model.Room;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 17/3/2.
 */
public class TenantMsgResponse implements Serializable{

    List<Room> houses;

    Integer total;

    public TenantMsgResponse(List<Room> houses, Integer total) {
        this.houses = houses;
        this.total = total;
    }

    public List<Room> getHouses() {
        return houses;
    }

    public void setHouses(List<Room> houses) {
        this.houses = houses;
    }
}
