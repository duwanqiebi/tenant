package com.dwqb.tenant.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17/4/22.
 */
public class URLUtils {
    /**
     * 解析出url请求的路径，包括页面
     *
     * @param strURL url地址
     * @return url路径
     */
    public static String UrlPage(String strURL) {
        String strPage = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[#]");
        if (strURL.length() > 0) {
            if (arrSplit.length > 1) {
                if (arrSplit[0] != null) {
                    strPage = arrSplit[0];
                }
            }
        }

        return strPage;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();

        arrSplit = strURL.split("[#]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static void main(String[] args) {
        System.out.println(URLUtils.TruncateUrlPage("https://beijing.zu.anjuke.com/ditu/W0QQtZ2#l1=39.858013&l2=116.319119&l3=15&f1=0&f2=0&f3=0&f4=0&f5=0&f6=0&f7=0&f8=0&f9=852147&f10=0"));
        Map<String,String> map = URLUtils.URLRequest("https://beijing.zu.anjuke.com/ditu/W0QQtZ2#l1=39.858013&l2=116.319119&l3=15&f1=0&f2=0&f3=0&f4=0&f5=0&f6=0&f7=0&f8=0&f9=852147&f10=0");
        for(Map.Entry<String,String> entry: map.entrySet()){
            System.out.println(entry.getKey() + " = "  + entry.getValue());
        }

    }
}
