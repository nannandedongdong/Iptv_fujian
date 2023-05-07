package com.ccdt.ottclient.model;

/*
            {
                "productId": "xgdd",
                "productName": "星光大道",
                "businessModelName": "包月",
                "orderTime": "2015-06-23 14:33:07",
                "orderPrice": 20,
                "subscribeFlag": "1",
                "subscribeText": "下月自动续订"
            }
 */
/*
                "productId": "by",
                "productName": "包月",
                "businessModelName": "包月",
                "orderTime": "2015-12-17 15:07:14",
                "orderPrice": 10,
                "beginTime": "2015-12-17 15:07:14",
                "endTime": "2016-01-17 15:07:14",
                "subscribeFlag": "1",
                "subscribeText": "下月自动续订",
                "modelNumStr": "1月",
                "payType": "1",
                "deficitFlag": "0",
                "payFlag": "0"
 */
import org.json.JSONObject;

import java.util.Map;

/**
 * 包月产品
 */
public class MonthOrderObj extends BaseObject {

    private String productId;
    private String productName;
    private String businessModelName;
    private String orderTime;
    private int orderPrice;
    private String beginTime;
    private String endTime;
    private String subscribeFlag;
    private String subscribeText;
    private String modelNumStr;
    private String payType;
    private String deficitFlag;
    private String payFlag;
    private int id;

    @Override
    public void parserJSON(JSONObject jsonObject) {
        if (jsonObject != null) {
            productId = jsonObject.optString("productId");
            productName = jsonObject.optString("productName");
            businessModelName = jsonObject.optString("businessModelName");
            orderTime = jsonObject.optString("orderTime");
            orderPrice = jsonObject.optInt("orderPrice");
            subscribeFlag = jsonObject.optString("subscribeFlag");
            subscribeText = jsonObject.optString("subscribeText");
            beginTime = jsonObject.optString("beginTime");
            endTime = jsonObject.optString("endTime");
            modelNumStr = jsonObject.optString("modelNumStr");
            payType = jsonObject.optString("payType");
            deficitFlag = jsonObject.optString("deficitFlag");
            payFlag = jsonObject.optString("payFlag");
            id = jsonObject.optInt("id");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBusinessModelName() {
        return businessModelName;
    }

    public void setBusinessModelName(String businessModelName) {
        this.businessModelName = businessModelName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getSubscribeFlag() {
        return subscribeFlag;
    }

    public void setSubscribeFlag(String subscribeFlag) {
        this.subscribeFlag = subscribeFlag;
    }

    public String getSubscribeText() {
        return subscribeText;
    }

    public void setSubscribeText(String subscribeText) {
        this.subscribeText = subscribeText;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getModelNumStr() {
        return modelNumStr;
    }

    public void setModelNumStr(String modelNumStr) {
        this.modelNumStr = modelNumStr;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getDeficitFlag() {
        return deficitFlag;
    }

    public void setDeficitFlag(String deficitFlag) {
        this.deficitFlag = deficitFlag;
    }

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MonthOrderObj{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", businessModelName='" + businessModelName + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderPrice=" + orderPrice +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", subscribeFlag='" + subscribeFlag + '\'' +
                ", subscribeText='" + subscribeText + '\'' +
                ", modelNumStr='" + modelNumStr + '\'' +
                ", payType='" + payType + '\'' +
                ", deficitFlag='" + deficitFlag + '\'' +
                ", payFlag='" + payFlag + '\'' +
                ", id=" + id +
                '}';
    }
}
