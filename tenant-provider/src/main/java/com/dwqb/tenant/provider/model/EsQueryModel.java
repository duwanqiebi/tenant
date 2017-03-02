package com.dwqb.tenant.provider.model;

/**
 * Created by zhangqiang on 17/3/1.
 */
public class EsQueryModel{

    private EsMatchModel match;

    public EsQueryModel(EsMatchModel match) {
        this.match = match;
    }

    public EsMatchModel getMatch() {
        return match;
    }

    public void setMatch(EsMatchModel match) {
        this.match = match;
    }
}
