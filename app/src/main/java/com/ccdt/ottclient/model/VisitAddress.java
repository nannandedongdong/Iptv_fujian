package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Map;

/**
 * @Package com.ccdt.stb.vision.data.model
 * @ClassName: VisitAddress
 * @Description: 播放地址模板
 * @author hezb
 * @date 2015年6月10日 下午7:32:57
 */

public class VisitAddress extends BaseObject implements Parcelable {
    private String status;
    private String malv;
    private String bfUrl;
    private String mzDesc;
    private String format;

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            setStatus(jsonObject.optString("status"));
            setMalv(jsonObject.optString("malv"));
            setBfUrl(jsonObject.optString("bfUrl"));
            setFormat(jsonObject.optString("format"));
            setMzDesc(jsonObject.optString("mzDesc"));
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
//        if (map != null) {
//            setStatus(map.get("status"));
//            setMalv(map.get("malv"));
//            setBfUrl(map.get("bfUrl"));
//            setFormat(map.get("format"));
//        }
    }

    public VisitAddress(){}

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMalv() {
        return malv;
    }
    public void setMalv(String malv) {
        this.malv = malv;
    }
    public String getBfUrl() {
        return bfUrl;
    }
    public void setBfUrl(String bfUrl) {
        this.bfUrl = bfUrl;
    }
    public String getMzDesc() {
        return mzDesc;
    }
    public void setMzDesc(String mzDesc) {
        this.mzDesc = mzDesc;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    
    private VisitAddress(Parcel in) {
        status = in.readString();
        malv = in.readString();
        bfUrl = in.readString();
        mzDesc = in.readString();
        format = in.readString();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(malv);
        dest.writeString(bfUrl);
        dest.writeString(mzDesc);
        dest.writeString(format);
    }
    
    public static final Creator<VisitAddress> CREATOR = new Creator<VisitAddress>() {
        
        @Override
        public VisitAddress[] newArray(int size) {
            return new VisitAddress[size];
        }
        
        @Override
        public VisitAddress createFromParcel(Parcel source) {
            return new VisitAddress(source);
        }
    };
}
