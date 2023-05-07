package com.ccdt.ottclient.model;

/*
{
        pubTime=6.0,
        updateTime=2015-11-1814: 05: 03,
        mzDesc=影片根据同名游戏改编，“代号47”（鲁伯特·弗兰德RupertFriend饰）是神秘杀手组织的头号杀手，他性格冷静，智慧过人，行动神秘低调，从来没人知道他的底细，只靠颈后位置的一个纹身条码辨认身份。　　“代号47”既是罪恶的化身，又是善意的使徒。他从不过问犯罪的真理及意义，直到碰上一名惊为天人神祕女子卡蒂娅（汉娜·韦尔HannahWare饰），改写了他的命运，更使他遭到杀手组织的追杀，生命危在旦夕……,
        star=4,
        mzName=代号47,
        runTime=5772,
        director=亚历山大·巴赫,
        id=MOVIEc5ca93a3f0a24b52b6d7bce76b052216,
        category=悬疑,
        vodType=1,
        showUrl="http://192.167.1.117:15414//multimedia/image/EPOSIDE/2015/11/18/2015_11_18_14_05_03_632.jpg",
        actors=鲁伯特·弗兰德/扎克瑞·昆图/塞伦·希德/托马斯·克莱舒曼/汉娜·韦尔,
        juji=[
            {
                "mzDesc": "影片根据同名游戏改编，“代号47”（鲁伯特·弗兰德 Rupert Friend 饰）是神秘杀手组织的头号杀手，他性格冷静，智慧过人，行动神秘低调，从来没人知道他的底细，只靠颈后位置的一个纹身条码辨认身份。 \r\n　　“代号47”既是罪恶的化身，又是善意的使徒。他从不过问犯罪的真理 及意义，直到碰上一名惊为天人神祕女子卡蒂娅（汉娜·韦尔 Hannah Ware 饰），改写了他的命运，更使他遭到杀手组织的追杀，生命危在旦夕……",
                "visitAddress": [
                    {
                        "status": "finished",
                        "malv": "Original",
                        "bfUrl": "http://192.167.1.117:15414/vod/8738/origin/transcode.m3u8",
                        "format": "原始"
                    }
                ],
                "actors": "鲁伯特·弗兰德 / 扎克瑞·昆图 / 塞伦·希德 / 托马斯·克莱舒曼 / 汉娜·韦尔",
                "jujiId": "EPISODE28fa9dc53c544df1b314d8bcb53ffaf7",
                "director": "亚历山大·巴赫",
                "jujiName": "代号47"
            }
        ]
    }
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VodDetailObj extends BaseObject implements Parcelable {
    private String pubTime;
    private String updateTime;
    private String mzDesc;
    private String star;
    private String mzName;
    private String runTime;
    private String director;
    private String id;
    private String category;
    private String vodType;
    private String actors;
    private String showUrl;
    private String ifCollect;
    private String jishu;
    private String realJishu;
    private String language;
    private String origin;
    private String year;

    private ArrayList<JuJiInfo> juji = new ArrayList<>();
    private List<VodRecommendObj> recommends = new ArrayList<>();

    public VodDetailObj() {
    }


    protected VodDetailObj(Parcel in) {
        pubTime = in.readString();
        updateTime = in.readString();
        mzDesc = in.readString();
        star = in.readString();
        mzName = in.readString();
        runTime = in.readString();
        director = in.readString();
        id = in.readString();
        category = in.readString();
        vodType = in.readString();
        actors = in.readString();
        showUrl = in.readString();
        ifCollect = in.readString();
        jishu = in.readString();
        realJishu = in.readString();
        language = in.readString();
        origin = in.readString();
        year = in.readString();
        juji = in.createTypedArrayList(JuJiInfo.CREATOR);
        recommends = in.createTypedArrayList(VodRecommendObj.CREATOR);
    }

    public static final Creator<VodDetailObj> CREATOR = new Creator<VodDetailObj>() {
        @Override
        public VodDetailObj createFromParcel(Parcel in) {
            return new VodDetailObj(in);
        }

        @Override
        public VodDetailObj[] newArray(int size) {
            return new VodDetailObj[size];
        }
    };

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        if (map != null) {
            setPubTime(map.get("pubTime"));
            setUpdateTime(map.get("updateTime"));
            setMzDesc(map.get("mzDesc"));
            setStar(map.get("star"));
            setMzName(map.get("mzName"));
            setRunTime(map.get("runTime"));
            setDirector(map.get("director"));
            setId(map.get("id"));
            setCategory(map.get("category"));
            setVodType(map.get("vodType"));
            setActors(map.get("actors"));
            setShowUrl(map.get("showUrl"));
            setIfCollect(map.get("ifCollect"));
            this.jishu = map.get("jishu");
            this.realJishu = map.get("realJishu");
            this.origin = map.get("origin");
            this.language = map.get("language");
            this.year = map.get("year");
            String jujiStrs = map.get("juji");
            String recommendsStr = map.get("recommends");

            if (!TextUtils.isEmpty(jujiStrs)) {
                JSONArray jsonArray = new JSONArray(jujiStrs);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
                    if (json != null) {
                        JuJiInfo vodJujiObj = new JuJiInfo();
                        vodJujiObj.parserJSON(json);
                        getJuji().add(vodJujiObj);
                    }
                }
            }

            if (!TextUtils.isEmpty(recommendsStr)) {
                JSONArray jsonArray = new JSONArray(recommendsStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
                    if (json != null) {
                        VodRecommendObj recommendObj = new VodRecommendObj();
                        recommendObj.parserJSON(json);
                        getRecommends().add(recommendObj);
                    }
                }
            }


        }
    }

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {

    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getMzDesc() {
        return mzDesc;
    }

    public void setMzDesc(String mzDesc) {
        this.mzDesc = mzDesc;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getMzName() {
        return mzName;
    }

    public void setMzName(String mzName) {
        this.mzName = mzName;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVodType() {
        return vodType;
    }

    public void setVodType(String vodType) {
        this.vodType = vodType;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getIfCollect() {
        return ifCollect;
    }

    public void setIfCollect(String ifCollect) {
        this.ifCollect = ifCollect;
    }

    public String getJishu() {
        return jishu;
    }

    public void setJishu(String jishu) {
        this.jishu = jishu;
    }

    public String getRealJishu() {
        return realJishu;
    }

    public void setRealJishu(String realJishu) {
        this.realJishu = realJishu;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ArrayList<JuJiInfo> getJuji() {
        return juji;
    }

    public List<VodRecommendObj> getRecommends() {
        return recommends;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pubTime);
        dest.writeString(updateTime);
        dest.writeString(mzDesc);
        dest.writeString(star);
        dest.writeString(mzName);
        dest.writeString(runTime);
        dest.writeString(director);
        dest.writeString(id);
        dest.writeString(category);
        dest.writeString(vodType);
        dest.writeString(actors);
        dest.writeString(showUrl);
        dest.writeString(ifCollect);
        dest.writeString(jishu);
        dest.writeString(realJishu);
        dest.writeString(language);
        dest.writeString(origin);
        dest.writeString(year);
        dest.writeTypedList(juji);
        dest.writeTypedList(recommends);
    }
}
