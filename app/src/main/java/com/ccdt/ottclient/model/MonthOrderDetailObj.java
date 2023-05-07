package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.util.Map;

/*
{
                "orderTime": "2015-06",
                "orderPrice": 20,
                "stateCode": "1",
                "stateText": "使用中"
            }
 */
public class MonthOrderDetailObj extends BaseObject {

    private String orderTime;
    private String orderPrice;
    private String stateCode;
    private String stateText;

    @Override
    public void parserJSON(JSONObject jsonObject) {
        if (jsonObject != null) {
            orderTime = jsonObject.optString("orderTime");
            orderPrice = jsonObject.optString("orderPrice");
            stateCode = jsonObject.optString("stateCode");
            stateText = jsonObject.optString("stateText");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    @Override
    public String toString() {
        return "MonthOrderDetailObj{" +
                "orderTime='" + orderTime + '\'' +
                ", orderPrice='" + orderPrice + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", stateText='" + stateText + '\'' +
                '}';
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }
}
