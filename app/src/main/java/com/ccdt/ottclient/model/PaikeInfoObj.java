package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Map;

/*
"id" -> "MAKING003"
"commentNum" -> "0"
"praiseNum" -> "0"
"showUrl" -> "http://192.167.1.117:15414//multimedia/image/EPOSIDE/2015/11/02/2015_11_02_13_57_15_842.jpg"
"jobId" -> "28"
"mzName" -> "test pk3"
"collectNum" -> "0"
 */
public class PaikeInfoObj extends BaseObject implements Parcelable {

    private String id;
    private String commentNum;
    private String praiseNum;
    private String showUrl;
    private String jobId;
    private String mzName;
    private String collectNum;
    private String bfUrl;


    protected PaikeInfoObj(Parcel in) {
        id = in.readString();
        commentNum = in.readString();
        praiseNum = in.readString();
        showUrl = in.readString();
        jobId = in.readString();
        mzName = in.readString();
        collectNum = in.readString();
        bfUrl = in.readString();
    }

    public static final Creator<PaikeInfoObj> CREATOR = new Creator<PaikeInfoObj>() {
        @Override
        public PaikeInfoObj createFromParcel(Parcel in) {
            return new PaikeInfoObj(in);
        }

        @Override
        public PaikeInfoObj[] newArray(int size) {
            return new PaikeInfoObj[size];
        }
    };

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        if (map != null) {
            setId(map.get("id"));
            setCommentNum(map.get("commentNum"));
            setPraiseNum(map.get("praiseNum"));
            setShowUrl(map.get("showUrl"));
            setJobId(map.get("jobId"));
            setMzName(map.get("mzName"));
            setCollectNum(map.get("collectNum"));
            this.bfUrl = map.get("bfUrl");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getMzName() {
        return mzName;
    }

    public void setMzName(String mzName) {
        this.mzName = mzName;
    }

    public String getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(String collectNum) {
        this.collectNum = collectNum;
    }

    public String getBfUrl() {
        return bfUrl;
    }

    public void setBfUrl(String bfUrl) {
        this.bfUrl = bfUrl;
    }

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {

    }

    public PaikeInfoObj() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(commentNum);
        dest.writeString(praiseNum);
        dest.writeString(showUrl);
        dest.writeString(jobId);
        dest.writeString(mzName);
        dest.writeString(collectNum);
        dest.writeString(bfUrl);
    }

    ////////////////////////////////////////////////////
    // 实现 Parcelable 接口



}
