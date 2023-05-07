package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * "programTime" -> "2016/01/18"
 "status" -> "6"
 "zbMzId" -> "CHANNEL0888a9b3b06e4f7d872013d559fca51e"
 "endTime" -> "2016/01/18 01:02"
 "epgTime" -> "2"
 "id" -> "EPG7b72f18c5d9b44a88fde53075f94c642"
 "startTime" -> "2016/01/18 01:00"
 "epgName" -> "晚间气象服务"
 "lzState" -> "1"
 */
public class ProgramObj extends BaseObject implements Serializable{

    public String startTime;
    public String epgName;
    public String backAddress;
    public String programTime;
    public String status;
    public String zbMzId;
    public String endTime;
    public String epgTime;
    public String id;
    public String lzState;

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {

    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        if (map == null){
            return;
        }
        startTime = map.get("startTime");
        epgName = map.get("epgName");
        backAddress = map.get("backAddress");
        programTime = map.get("programTime");
        status = map.get("status");
        zbMzId = map.get("zbMzId");
        endTime = map.get("endTime");
        id = map.get("id");
        epgTime = map.get("epgTime");
        lzState = map.get("lzState");
    }

    @Override
    public String toString() {
        return "ProgramObj{" +
                "startTime='" + startTime + '\'' +
                ", epgName='" + epgName + '\'' +
                ", backAddress='" + backAddress + '\'' +
                ", programTime='" + programTime + '\'' +
                ", status='" + status + '\'' +
                ", zbMzId='" + zbMzId + '\'' +
                ", endTime='" + endTime + '\'' +
                ", epgTime='" + epgTime + '\'' +
                ", id='" + id + '\'' +
                ", lzState='" + lzState + '\'' +
                '}';
    }
}
