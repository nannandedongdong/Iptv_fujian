package com.ccdt.ottclient.model;

/*
"posterUrl": "http://192.167.1.104:15414//multimedia/image/EPOSIDE/2015/07/08/2015_07_08_16_27_40_544.jpg",
"equipType": "base",
"posterType": "1"
 */

import org.json.JSONObject;

import java.util.Map;

/**
 * 海报实体类
 */
public class PosterObj extends BaseObject  {
    private String posterUrl;
    private String equipType;
    private String posterType;

    @Override
    public void parserJSON(JSONObject jsonObject){
        this.posterUrl = jsonObject.optString("posterUrl");
        this.equipType = jsonObject.optString("equipType");
        this.posterType = jsonObject.optString("posterType");
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getPosterType() {
        return posterType;
    }

    public void setPosterType(String posterType) {
        this.posterType = posterType;
    }

    @Override
    public String toString() {
        return "PosterObj{" +
                "posterUrl='" + posterUrl + '\'' +
                ", equipType='" + equipType + '\'' +
                ", posterType='" + posterType + '\'' +
                '}';
    }
}
