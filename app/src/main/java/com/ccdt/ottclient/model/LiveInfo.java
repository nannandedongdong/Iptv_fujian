package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by hanyue on 2015/11/27.
 */
public class LiveInfo extends BaseObject implements Serializable {
    public String sitLm;
    public String channelId;
    public String mzName;
    public String logoUrl;
    public String zbSource;
    public String endTime;
    public String recordTime;
    public String ifBack;
    public String praiseNum;
    public String commentNum;
    public String startTime;
    public String id;
    public String moveTime;
    public String ifMove;
    public String epgName;
    public String collectNum;


    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {

    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        if (map != null){
            sitLm = map.get("sitLm");
            channelId = map.get("channelId");
            mzName = map.get("mzName");
            logoUrl = map.get("logoUrl");
            zbSource = map.get("zbSource");
            endTime = map.get("endTime");
            recordTime = map.get("recordTime");
            ifBack = map.get("ifBack");
            praiseNum = map.get("praiseNum");
            commentNum = map.get("commentNum");
            startTime = map.get("startTime");
            moveTime = map.get("moveTime");
            ifMove = map.get("ifMove");
            epgName = map.get("epgName");
            ifMove = map.get("ifMove");
            collectNum = map.get("collectNum");
            id = map.get("id");
        }
    }
}
