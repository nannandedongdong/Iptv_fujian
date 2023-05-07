package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
[
    {
        "mzDesc": "一步之遥",
        "visitAddress": [
            {
                "status": "finished",
                "malv": "1500k",
                "bfUrl": "multimedia/vodsource/video/babaquna.mp4",
                "format": "HD"
            }
        ],
        "actors": "",
        "jujiId": "EPISODE7478d3d9effa49b38a75922d44e91975",
        "director": "",
        "jujiName": "一步之遥"
    }
]
*/

/**
 * @author hezb
 * @Package com.ccdt.stb.vision.data.model
 * @ClassName: JuJiInfo
 * @Description: 剧集
 * @date 2015年4月27日 下午1:41:31
 */

public class JuJiInfo extends BaseObject implements Serializable, Parcelable {

    private static final long serialVersionUID = 1L;

    private String mzDesc;
    private List<VisitAddress> visitAddress = new ArrayList<>();
    private String actors;
    private String jujiId;
    private String director;
    private String jujiName;
    private String chapter;//剧集

    //本地的格式化的 剧集
    public int juJiNum;

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {

            setMzDesc(jsonObject.optString("mzDesc"));
            setDirector(jsonObject.optString("director"));
            setActors(jsonObject.optString("actors"));
            setJujiId(jsonObject.optString("jujiId"));
            setJujiName(jsonObject.optString("jujiName"));
            setChapter(jsonObject.optString("chapter"));


            String visitAddressStrs = jsonObject.optString("visitAddress");
            if (!TextUtils.isEmpty(visitAddressStrs)) {
                JSONArray jsonArray = new JSONArray(visitAddressStrs);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);
                    if (json != null) {
                        VisitAddress obj = new VisitAddress();
                        obj.parserJSON(json);
                        getVisitAddress().add(obj);
                    }
                }
            }
        }
    }

    public String getMzDesc() {
        return mzDesc;
    }

    public void setMzDesc(String mzDesc) {
        this.mzDesc = mzDesc;
    }

    public List<VisitAddress> getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(List<VisitAddress> visitAddress) {
        this.visitAddress = visitAddress;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getJujiId() {
        return jujiId;
    }

    public void setJujiId(String jujiId) {
        this.jujiId = jujiId;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getJujiName() {
        return jujiName;
    }

    public void setJujiName(String jujiName) {
        this.jujiName = jujiName;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    private JuJiInfo(Parcel in) {
        mzDesc = in.readString();
        visitAddress = in.createTypedArrayList(VisitAddress.CREATOR);
        actors = in.readString();
        jujiId = in.readString();
        director = in.readString();
        jujiName = in.readString();
        chapter = in.readString();

        juJiNum = in.readInt();
    }

    public JuJiInfo(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mzDesc);
        dest.writeTypedList(visitAddress);
        dest.writeString(actors);
        dest.writeString(jujiId);
        dest.writeString(director);
        dest.writeString(jujiName);
        dest.writeString(chapter);

        dest.writeInt(juJiNum);
    }

    public static final Creator<JuJiInfo> CREATOR = new Creator<JuJiInfo>() {

        @Override
        public JuJiInfo[] newArray(int size) {
            return new JuJiInfo[size];
        }

        @Override
        public JuJiInfo createFromParcel(Parcel source) {
            return new JuJiInfo(source);
        }
    };


    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }
}
