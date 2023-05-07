package com.ccdt.ottclient.model;

/**
 * 购买记录对象
 * <p/>
 * 编号
 * 名称
 * 价格
 * 订购时间
 * 订购状态
 */
public class PurchaseHistoryObj implements Comparable<PurchaseHistoryObj> {
    private String productId;
    private int orderId;
    private String name;
    private int price;
    private String orderTime;
    private String status;
    private String statusText;
    private String flag; // 0 月  1 次

    private String modelNumStr;
    private String payType;
    private String deficitFlag;
    private String payFlag;
    private String beginTime;
    private String endTime;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    @Override
    public int compareTo(PurchaseHistoryObj another) {
        return this.orderTime.compareTo(another.orderTime);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "PurchaseHistoryObj{" +
                "productId='" + productId + '\'' +
                ", orderId=" + orderId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", orderTime='" + orderTime + '\'' +
                ", status='" + status + '\'' +
                ", statusText='" + statusText + '\'' +
                ", flag='" + flag + '\'' +
                ", modelNumStr='" + modelNumStr + '\'' +
                ", payType='" + payType + '\'' +
                ", deficitFlag='" + deficitFlag + '\'' +
                ", payFlag='" + payFlag + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
