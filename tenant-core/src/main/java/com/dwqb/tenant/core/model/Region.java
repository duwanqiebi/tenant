package com.dwqb.tenant.core.model;


import java.util.HashMap;
import java.util.Map;

public enum Region {

    DONG_CHENG("东城区"),

    XI_CHENG("西城区"),

    HAI_DIAN("海淀区"),

    CHAO_YANG("朝阳区"),

    FENG_TAI("丰台区"),

    MEN_TOU_GOU("门头沟区"),

    SHI_JING_SHAN("石景山区"),

    FANG_SHAN("房山区"),

    TONG_ZHOU("通州区"),

    SHUN_YI("顺义区"),

    CHANG_PING("昌平区"),

    DA_XING("大兴区"),

    HUAI_ROU("怀柔区"),

    PING_GU("平谷区"),

    MI_YUN("密云区"),

    YAN_QING("延庆区");


    private final String name;

    private static Map<String, Region> map = new HashMap<String, Region>();
    static {
        for (Region region : Region.values()) {
            map.put(region.name, region);
        }
    }

    Region(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static Region getRegion(String region){
        return map.get(region);
    }

    @Override
    public String toString() {
        return name;
    }
}
