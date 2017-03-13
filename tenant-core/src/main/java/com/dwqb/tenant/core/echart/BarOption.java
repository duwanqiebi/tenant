package com.dwqb.tenant.core.echart;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by zhangqiang on 17/3/12.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BarOption extends BaseOption{

    private Node<String> legend;

    private Node<String>[] xAxis;

    private Node<Integer>[] series;

    public BarOption() {
    }

    public BarOption(Node<String> legend, Node<String>[] xAxis, Node<Integer>[] series) {
        this.legend = legend;
        this.xAxis = xAxis;
        this.series = series;
    }

    public Node<String> getLegend() {
        return legend;
    }

    public void setLegend(Node<String> legend) {
        this.legend = legend;
    }

    public Node<String>[] getxAxis() {
        return xAxis;
    }

    public void setxAxis(Node<String>[] xAxis) {
        this.xAxis = xAxis;
    }

    public Node<Integer>[] getSeries() {
        return series;
    }

    public void setSeries(Node<Integer>[] series) {
        this.series = series;
    }
}
