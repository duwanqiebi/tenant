package com.dwqb.tenant.provider.model;

/**
 * Created by zhangqiang on 17/3/1.
 */
public class EsModel {

    private EsQueryModel query;

    private Integer from;

    private Integer size;

    public EsModel(EsQueryModel query, Integer from, Integer size) {
        this.query = query;
        this.from = from;
        this.size = size;
    }

    public EsQueryModel getQuery() {
        return query;
    }

    public void setQuery(EsQueryModel query) {
        this.query = query;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
