package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Map;

public class VodRecommendObj extends BaseObject implements Parcelable {

    private String showUrl;
    private String id;
    private String mzName;

    public VodRecommendObj(){}

    protected VodRecommendObj(Parcel in) {
        showUrl = in.readString();
        id = in.readString();
        mzName = in.readString();
    }

    public static final Creator<VodRecommendObj> CREATOR = new Creator<VodRecommendObj>() {
        @Override
        public VodRecommendObj createFromParcel(Parcel in) {
            return new VodRecommendObj(in);
        }

        @Override
        public VodRecommendObj[] newArray(int size) {
            return new VodRecommendObj[size];
        }
    };

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            setShowUrl(jsonObject.optString("showUrl"));
            setId(jsonObject.optString("id"));
            setMzName(jsonObject.optString("mzName"));
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMzName() {
        return mzName;
    }

    public void setMzName(String mzName) {
        this.mzName = mzName;
    }

    @Override
    public String toString() {
        return "VodRecommendObj{" +
                "showUrl='" + showUrl + '\'' +
                ", id='" + id + '\'' +
                ", mzName='" + mzName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(showUrl);
        dest.writeString(id);
        dest.writeString(mzName);
    }
}
