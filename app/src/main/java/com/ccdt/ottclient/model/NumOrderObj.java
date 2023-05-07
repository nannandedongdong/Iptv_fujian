package com.ccdt.ottclient.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 "assetId": "test003",
 "assetName": "",
 "orderTime": "2015-06-23 14:31:58",
 "orderPrice": 66,
 "stateCode": "1",
 "stateText": "使用中" ,
 "posterList": [
       {
         "posterUrl": "http://192.167.1.104:15414//multimedia/image/EPOSIDE/2015/07/08/2015_07_08_16_27_40_544.jpg",
         "equipType": "base",
         "posterType": "1"
       },{
         "posterUrl": "http://192.167.1.104:15414//multimedia/image/EPOSIDE/2015/07/08/2015_07_08_16_27_40_544.jpg",
         "equipType": "base",
         "posterType": "0"
       }
    ]
 */
public class NumOrderObj extends BaseObject {

    private String assetId;
    private String assetName;
    private String orderTime;
    private int orderPrice;
    private String beginTime;
    private String endTime;
    private String stateCode;
    private String stateText;
    private String modelNumStr;
    private String payType;
    private String deficitFlag;
    private String payFlag;
    private List<PosterObj> posterList;
    private int id;

    @Override
    public void parserJSON(JSONObject jsonObject){
        if (jsonObject != null) {
            this.assetId = jsonObject.optString("assetId");
            this.assetName = jsonObject.optString("assetName");
            this.orderTime = jsonObject.optString("orderTime");
            this.orderPrice = jsonObject.optInt("orderPrice");
            this.stateCode = jsonObject.optString("stateCode");
            this.stateText = jsonObject.optString("stateText");
            this.beginTime = jsonObject.optString("beginTime");
            this.endTime = jsonObject.optString("endTime");
            this.modelNumStr = jsonObject.optString("modelNumStr");
            this.payType = jsonObject.optString("payType");
            this.deficitFlag = jsonObject.optString("deficitFlag");
            this.payFlag = jsonObject.optString("payFlag");
            this.id = jsonObject.optInt("id");

            JSONArray jsonArray = jsonObject.optJSONArray("posterList");
            if (jsonArray != null) {
                posterList = new ArrayList<>();
                PosterObj posterObj;
                JSONObject jObj;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jObj = jsonArray.optJSONObject(i);
                    if (jObj != null) {
                        posterObj = new PosterObj();
                        posterObj.parserJSON(jObj);
                        posterList.add(posterObj);
                    }
                }
                posterObj = null;
                jObj = null;
            }

        }


    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
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

    public List<PosterObj> getPosterList() {
        return posterList;
    }

    public void setPosterList(List<PosterObj> posterList) {
        this.posterList = posterList;
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
        return "NumOrderObj{" +
                "assetId='" + assetId + '\'' +
                ", assetName='" + assetName + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderPrice=" + orderPrice +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", stateText='" + stateText + '\'' +
                ", modelNumStr='" + modelNumStr + '\'' +
                ", payType='" + payType + '\'' +
                ", deficitFlag='" + deficitFlag + '\'' +
                ", payFlag='" + payFlag + '\'' +
                ", posterList=" + posterList +
                ", id=" + id +
                '}';
    }
}
