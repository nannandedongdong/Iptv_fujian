package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 {
"id" -> "229"
"createTime" -> "2016-01-15 11:54:29"
"validFlag" -> "1"
"mzName" -> "BTV-卫视"
"logoUrl" -> "http://192.167.1.117:15414//multimedia/image/CHANNEL/2016/01/14/2016_01_14_16_55_30_442.png"
"mzType" -> "2"
    }
 */
public class CollectionObj extends BaseObject implements Parcelable {

    private String id;
    private String createTime;
    private String validFlag;
    private String mzName;
    private String memberId;
    private String mzType;
    private String mzId;
    private String haibao;
    private String ifMove;
    private List<ZbSourceObj> zbSource = new ArrayList<>();

    public CollectionObj(){}


    protected CollectionObj(Parcel in) {
        id = in.readString();
        createTime = in.readString();
        validFlag = in.readString();
        mzName = in.readString();
        memberId = in.readString();
        mzType = in.readString();
        mzId = in.readString();
        haibao = in.readString();
        ifMove = in.readString();
        zbSource = in.createTypedArrayList(ZbSourceObj.CREATOR);
    }

    public static final Creator<CollectionObj> CREATOR = new Creator<CollectionObj>() {
        @Override
        public CollectionObj createFromParcel(Parcel in) {
            return new CollectionObj(in);
        }

        @Override
        public CollectionObj[] newArray(int size) {
            return new CollectionObj[size];
        }
    };

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        if (map != null) {
            this.id = map.get("id");
            this.createTime = map.get("createTime");
            this.validFlag = map.get("validFlag");
            this.mzName = map.get("mzName");
            this.memberId = map.get("memberId");
            this.mzType = map.get("mzType");
            this.mzId = map.get("mzId");

            if("0".equals(this.mzType) ||"1".equals(this.mzType)){
                // vod
                this.haibao = map.get("haibao");
            } else if("2".equals(this.mzType)){
                // channel
                this.haibao = map.get("logoUrl");
                String zbSourceStr = map.get("zbSource");
                if(!TextUtils.isEmpty(zbSourceStr)){
                    JSONArray arr2 = new JSONArray(zbSourceStr);
                    for (int i = 0; i < arr2.length(); i++) {
                        JSONObject json2 = arr2.optJSONObject(i);
                        if (json2 != null) {
                            ZbSourceObj obj2 = new ZbSourceObj();
                            obj2.parserJSON(json2);
                            zbSource.add(obj2);
                        }
                    }
                }
            }
        }
    }

    public List<ZbSourceObj> getZbSource() {
        return zbSource;
    }

    public void setZbSource(List<ZbSourceObj> zbSource) {
        this.zbSource = zbSource;
    }

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {

    }

    public String getIfMove() {
        return ifMove;
    }

    public void setIfMove(String ifMove) {
        this.ifMove = ifMove;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getMzName() {
        return mzName;
    }

    public void setMzName(String mzName) {
        this.mzName = mzName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMzType() {
        return mzType;
    }

    public void setMzType(String mzType) {
        this.mzType = mzType;
    }

    public String getMzId() {
        return mzId;
    }

    public void setMzId(String mzId) {
        this.mzId = mzId;
    }

    public String getHaibao() {
        return haibao;
    }

    public void setHaibao(String haibao) {
        this.haibao = haibao;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createTime);
        dest.writeString(validFlag);
        dest.writeString(mzName);
        dest.writeString(memberId);
        dest.writeString(mzType);
        dest.writeString(mzId);
        dest.writeString(haibao);
        dest.writeString(ifMove);
        dest.writeTypedList(zbSource);
    }
}
