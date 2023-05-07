package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.util.Map;

/*
 "status": "407",
 "msg": "参数缺失",
 "serverTime": "20150525102156"
 */
public class MessageObj extends BaseObject {
    private String status;
    private String msg;
    private String serverTime;

    @Override
    public void parserJSON(JSONObject jsonObject){
        if (jsonObject != null) {
            status = jsonObject.optString("status");
            msg = jsonObject.optString("msg");
            serverTime = jsonObject.optString("serverTime");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    @Override
    public String toString() {
        return "MessageObj{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", serverTime='" + serverTime + '\'' +
                '}';
    }
}
