package com.dwqb.tenant.core.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by zhangqiang on 17/3/1.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class EsMatchModel {

    private String title ;

    private String region;

    public EsMatchModel(String title) {
        this.title = title;
    }

    public EsMatchModel(String title, String region) {
        this.title = title;
        this.region = region;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
