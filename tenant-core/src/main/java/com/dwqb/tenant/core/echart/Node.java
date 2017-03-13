package com.dwqb.tenant.core.echart;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by zhangqiang on 17/3/12.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Node<T> {

    private String name;

    private String type;

    private T[] data;

    public Node() {
    }

    public Node(String name, T[] data, String type) {
        this.name = name;
        this.data = data;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }
}
