package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Map;

/*
 {
                mzOrder=0,
                lmPid=0,
                lmId=qhyszb,
                siteId=qhys,
                lmName=直播,
                recFlag=1,
                showType=slide,
                lmType=zb
            }
 */
public class SiteLmObj extends BaseObject implements Parcelable {
    private String mzOrder;
    private String lmPid;
    private String lmId;
    private String siteId;
    private String lmName;
    private String recFlag;
    private String showType;
    private String lmType;

    public SiteLmObj(){}

    protected SiteLmObj(Parcel in) {
        mzOrder = in.readString();
        lmPid = in.readString();
        lmId = in.readString();
        siteId = in.readString();
        lmName = in.readString();
        recFlag = in.readString();
        showType = in.readString();
        lmType = in.readString();
    }

    public static final Creator<SiteLmObj> CREATOR = new Creator<SiteLmObj>() {
        @Override
        public SiteLmObj createFromParcel(Parcel in) {
            return new SiteLmObj(in);
        }

        @Override
        public SiteLmObj[] newArray(int size) {
            return new SiteLmObj[size];
        }
    };

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            this.mzOrder = jsonObject.optString("mzOrder");
            this.lmPid = jsonObject.optString("lmPid");
            this.lmId = jsonObject.optString("lmId");
            this.siteId = jsonObject.optString("siteId");
            this.lmName = jsonObject.optString("lmName");
            this.recFlag = jsonObject.optString("recFlag");
            this.showType = jsonObject.optString("showType");
            this.lmType = jsonObject.optString("lmType");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public String getMzOrder() {
        return mzOrder;
    }

    public void setMzOrder(String mzOrder) {
        this.mzOrder = mzOrder;
    }

    public String getLmPid() {
        return lmPid;
    }

    public void setLmPid(String lmPid) {
        this.lmPid = lmPid;
    }

    public String getLmId() {
        return lmId;
    }

    public void setLmId(String lmId) {
        this.lmId = lmId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLmName() {
        return lmName;
    }

    public void setLmName(String lmName) {
        this.lmName = lmName;
    }

    public String getRecFlag() {
        return recFlag;
    }

    public void setRecFlag(String recFlag) {
        this.recFlag = recFlag;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getLmType() {
        return lmType;
    }

    public void setLmType(String lmType) {
        this.lmType = lmType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mzOrder);
        dest.writeString(lmPid);
        dest.writeString(lmId);
        dest.writeString(siteId);
        dest.writeString(lmName);
        dest.writeString(recFlag);
        dest.writeString(showType);
        dest.writeString(lmType);
    }
}
