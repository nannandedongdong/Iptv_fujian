package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

/*
 * "message": {
        "version": "20140520",
        "status": "200",
        "msg": "OK",
        "method": "",
        "serverTime": 1426038808017
    }
 */

public class DataMessage extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private String version;
    private String status;
    private String msg;
    private String method;
    private String serverTime;

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            setVersion(jsonObject.optString("version"));
            setStatus(jsonObject.optString("status"));
            setMsg(jsonObject.optString("msg"));
            setMethod(jsonObject.optString("method"));
            setServerTime(jsonObject.optString("serverTime"));
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    @Override
    public String toString() {
        return "DataMessage{" +
                "version='" + version + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", method='" + method + '\'' +
                ", serverTime='" + serverTime + '\'' +
                '}';
    }
}
