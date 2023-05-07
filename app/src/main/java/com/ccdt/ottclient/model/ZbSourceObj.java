package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Map;

/*
 {
rate=720x480,
name=BTV-卫视,
path=udp://225.0.0.100:1001,
moveUrl=live/CHANNEL50b9433b72fb445b846396c887c66d23/SD_401007970499558052/timeshift.m3u8,
liveUrl=live/CHANNEL50b9433b72fb445b846396c887c66d23/SD_401007970499558052/live.m3u8,
code=401007970499558052,
type=SD
            }
 */

public class ZbSourceObj extends BaseObject implements Parcelable {
    private String rate;
    private String path;
    private String name;
    private String code;
    private String type;
    private String liveUrl;
    private String moveUrl;

    public ZbSourceObj() {
    }


    protected ZbSourceObj(Parcel in) {
        rate = in.readString();
        path = in.readString();
        name = in.readString();
        code = in.readString();
        type = in.readString();
        liveUrl = in.readString();
        moveUrl = in.readString();
    }

    public static final Creator<ZbSourceObj> CREATOR = new Creator<ZbSourceObj>() {
        @Override
        public ZbSourceObj createFromParcel(Parcel in) {
            return new ZbSourceObj(in);
        }

        @Override
        public ZbSourceObj[] newArray(int size) {
            return new ZbSourceObj[size];
        }
    };

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            this.rate = jsonObject.optString("rate");
            this.path = jsonObject.optString("path");
            this.name = jsonObject.optString("name");
            this.code = jsonObject.optString("code");
            this.type = jsonObject.optString("type");
            this.liveUrl = jsonObject.optString("liveUrl");
            this.moveUrl = jsonObject.optString("moveUrl");

        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public String getMoveUrl() {
        return moveUrl;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rate);
        dest.writeString(path);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(type);
        dest.writeString(liveUrl);
        dest.writeString(moveUrl);
    }
}
