package com.dwqb.tenant.core.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
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
