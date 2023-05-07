package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.util.Map;

/*
{
                "productName": "土皇帝",
                "productId": "200",
                "productPrice": 560,
                "startTime": "2015-05-01",
                "endTime": "2015-12-22",
                "remark": "土皇帝土皇帝土皇帝土皇帝",
                "businessModelName": "包月"
            }
 */
public class OtherProductObj extends BaseObject {
    private String productName;
    private String productId;
    private int productPrice;
    private String startTime;
    private String endTime;
    private String remark;
    private String businessModelName;

    @Override
    public void parserJSON(JSONObject jsonObject){
        if (jsonObject != null) {
            productName = jsonObject.optString("productName");
            productId = jsonObject.optString("productId");
            productPrice = jsonObject.optInt("productPrice");
            startTime = jsonObject.optString("startTime");
            endTime = jsonObject.optString("endTime");
            remark = jsonObject.optString("remark");
            businessModelName = jsonObject.optString("businessModelName");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBusinessModelName() {
        return businessModelName;
    }

    public void setBusinessModelName(String businessModelName) {
        this.businessModelName = businessModelName;
    }

    @Override
    public String toString() {
        return "OtherProductObj{" +
                "productName='" + productName + '\'' +
                ", productId='" + productId + '\'' +
                ", productPrice=" + productPrice +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", remark='" + remark + '\'' +
                ", businessModelName='" + businessModelName + '\'' +
                '}';
    }
}
