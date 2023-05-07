package com.ccdt.ottclient.model;


import org.json.JSONObject;

import java.util.Map;

public class PlayHistoryObj extends BaseObject {
    public int type;//类型 拍客，视频
    public String mzId;
    public String title;
    public String posterUrl;
    public String playUrl;
    public int playPosition;//播放位置
    public int seriesPosition;//剧集位置
    public String time;//存储时间


    public String getMzId() {
        return mzId;
    }

    public void setMzId(String mzId) {
        this.mzId = mzId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {

    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }

    public int getSeriesPosition() {
        return seriesPosition;
    }

    public void setSeriesPosition(int seriesPosition) {
        this.seriesPosition = seriesPosition;
    }
}
