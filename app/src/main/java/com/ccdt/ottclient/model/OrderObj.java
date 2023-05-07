package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Map;

/*
{
    "message": {
        "status": "200",
        "msg": "订购成功",
        "serverTime": "20160107110233"
    },
    "data": {
        "orderId": "201601071102322517633",
        "terminalSn": "unknown",
        "terminalName": "",
        "productId": "acdb",
        "productName": "按次",
        "businessModel": "num",
        "modelNum": 24,
        "productPrice": 2,
        "payType": "1",
        "deficitFlag": "0",
        "orderAssetId": "MOVIE5e7e2e90ca1c485f9c9347d3da99cb8f",
        "orderAssetName": "蚁人",
        "beginTime": "2016-01-07 11:02:32",
        "endTime": "2016-01-08 11:02:32",
        "payFlag": "0",
        "businessModelName": "按次",
        "modelNumStr": "24小时",
        "id": 14,
        "validFlag": "1",
        "createTime": "2016-01-07 11:02:32",
        "updateTime": "2016-01-07 11:02:32"
    }
}
 */
public class OrderObj extends BaseObject implements Parcelable {

    private String orderId;
    private String terminalSn;
    private String terminalName;
    private String productId;
    private String productName;
    private String businessModel;
    private int modelNum;
    private int productPrice;
    private String payType;
    private String deficitFlag;
    private String orderAssetId;
    private String orderAssetName;
    private String beginTime;
    private String endTime;
    private String payFlag;
    private String businessModelName;
    private String modelNumStr;
    private int id;
    private String validFlag;
    private String createTime;
    private String updateTime;

    public OrderObj(){}

    protected OrderObj(Parcel in) {
        orderId = in.readString();
        terminalSn = in.readString();
        terminalName = in.readString();
        productId = in.readString();
        productName = in.readString();
        businessModel = in.readString();
        modelNum = in.readInt();
        productPrice = in.readInt();
        payType = in.readString();
        deficitFlag = in.readString();
        orderAssetId = in.readString();
        orderAssetName = in.readString();
        beginTime = in.readString();
        endTime = in.readString();
        payFlag = in.readString();
        businessModelName = in.readString();
        modelNumStr = in.readString();
        id = in.readInt();
        validFlag = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<OrderObj> CREATOR = new Creator<OrderObj>() {
        @Override
        public OrderObj createFromParcel(Parcel in) {
            return new OrderObj(in);
        }

        @Override
        public OrderObj[] newArray(int size) {
            return new OrderObj[size];
        }
    };

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }
    public String getOrderId(){
        return this.orderId;
    }
    public void setTerminalSn(String terminalSn){
        this.terminalSn = terminalSn;
    }
    public String getTerminalSn(){
        return this.terminalSn;
    }
    public void setTerminalName(String terminalName){
        this.terminalName = terminalName;
    }
    public String getTerminalName(){
        return this.terminalName;
    }
    public void setProductId(String productId){
        this.productId = productId;
    }
    public String getProductId(){
        return this.productId;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public String getProductName(){
        return this.productName;
    }
    public void setBusinessModel(String businessModel){
        this.businessModel = businessModel;
    }
    public String getBusinessModel(){
        return this.businessModel;
    }
    public void setModelNum(int modelNum){
        this.modelNum = modelNum;
    }
    public int getModelNum(){
        return this.modelNum;
    }
    public void setProductPrice(int productPrice){
        this.productPrice = productPrice;
    }
    public int getProductPrice(){
        return this.productPrice;
    }
    public void setPayType(String payType){
        this.payType = payType;
    }
    public String getPayType(){
        return this.payType;
    }
    public void setDeficitFlag(String deficitFlag){
        this.deficitFlag = deficitFlag;
    }
    public String getDeficitFlag(){
        return this.deficitFlag;
    }
    public void setOrderAssetId(String orderAssetId){
        this.orderAssetId = orderAssetId;
    }
    public String getOrderAssetId(){
        return this.orderAssetId;
    }
    public void setOrderAssetName(String orderAssetName){
        this.orderAssetName = orderAssetName;
    }
    public String getOrderAssetName(){
        return this.orderAssetName;
    }
    public void setBeginTime(String beginTime){
        this.beginTime = beginTime;
    }
    public String getBeginTime(){
        return this.beginTime;
    }
    public void setEndTime(String endTime){
        this.endTime = endTime;
    }
    public String getEndTime(){
        return this.endTime;
    }
    public void setPayFlag(String payFlag){
        this.payFlag = payFlag;
    }
    public String getPayFlag(){
        return this.payFlag;
    }
    public void setBusinessModelName(String businessModelName){
        this.businessModelName = businessModelName;
    }
    public String getBusinessModelName(){
        return this.businessModelName;
    }
    public void setModelNumStr(String modelNumStr){
        this.modelNumStr = modelNumStr;
    }
    public String getModelNumStr(){
        return this.modelNumStr;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setValidFlag(String validFlag){
        this.validFlag = validFlag;
    }
    public String getValidFlag(){
        return this.validFlag;
    }
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }
    public String getUpdateTime(){
        return this.updateTime;
    }

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            this.orderId = jsonObject.optString("orderId");
            this.terminalSn = jsonObject.optString("terminalSn");
            this.terminalName = jsonObject.optString("terminalName");
            this.productId = jsonObject.optString("productId");
            this.productName = jsonObject.optString("productName");
            this.businessModel = jsonObject.optString("businessModel");
            this.modelNum = jsonObject.optInt("modelNum");
            this.productPrice = jsonObject.optInt("productPrice");
            this.payType = jsonObject.optString("payType");
            this.deficitFlag = jsonObject.optString("deficitFlag");
            this.orderAssetId = jsonObject.optString("orderAssetId");
            this.orderAssetName = jsonObject.optString("orderAssetName");
            this.beginTime = jsonObject.optString("beginTime");
            this.endTime = jsonObject.optString("endTime");
            this.payFlag = jsonObject.optString("payFlag");
            this.businessModelName = jsonObject.optString("businessModelName");
            this.modelNumStr = jsonObject.optString("modelNumStr");
            this.id = jsonObject.optInt("id");
            this.validFlag = jsonObject.optString("validFlag");
            this.createTime = jsonObject.optString("createTime");
            this.updateTime = jsonObject.optString("updateTime");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(terminalSn);
        dest.writeString(terminalName);
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(businessModel);
        dest.writeInt(modelNum);
        dest.writeInt(productPrice);
        dest.writeString(payType);
        dest.writeString(deficitFlag);
        dest.writeString(orderAssetId);
        dest.writeString(orderAssetName);
        dest.writeString(beginTime);
        dest.writeString(endTime);
        dest.writeString(payFlag);
        dest.writeString(businessModelName);
        dest.writeString(modelNumStr);
        dest.writeInt(id);
        dest.writeString(validFlag);
        dest.writeString(createTime);
        dest.writeString(updateTime);
    }
}
