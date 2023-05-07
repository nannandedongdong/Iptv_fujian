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
        sourceType=N,
        siteLm=[
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
        ],
        channelId=cctv-1,
        mzName=CCTV-1,
        logoUrl=http: //192.167.1.117: 15414//multimedia/image/CHANNEL/2015/11/18/2015_11_18_13_58_30_122.png,
        zbSource=[
            {
                "name": "CCTV-1",
                "code": "5025538139692157935",
                "type": "SD"
            }
        ],
        recordTime=0,
        ifBack=0,
        id=CHANNEL2756ab0bf1b8485f8495cef81f7f3c6d,
        commentNum=0,
        praiseNum=0,
        moveTime=0,
        ifMove=0,
        collectNum=0,
        "collectUids" -> "[158]"
    }
 */
public class ChannelObj extends BaseObject implements Parcelable {

    private String sourceType;
    private String channelId;
    private String mzName;
    private String logoUrl;
    private String recordTime;
    private String ifBack;
    private String id;
    private String commentNum;
    private String praiseNum;
    private String moveTime;
    private String ifMove;
    private String collectNum;
    private String startTime;
    private String endTime;
    private String epgName;
    private String collectUids;
    private List<SiteLmObj> siteLm = new ArrayList<>();
    private List<ZbSourceObj> zbSource = new ArrayList<>();

    public ChannelObj(){}

    protected ChannelObj(Parcel in) {
        sourceType = in.readString();
        channelId = in.readString();
        mzName = in.readString();
        logoUrl = in.readString();
        recordTime = in.readString();
        ifBack = in.readString();
        id = in.readString();
        commentNum = in.readString();
        praiseNum = in.readString();
        moveTime = in.readString();
        ifMove = in.readString();
        collectNum = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        epgName = in.readString();
        collectUids = in.readString();
        siteLm = in.createTypedArrayList(SiteLmObj.CREATOR);
        zbSource = in.createTypedArrayList(ZbSourceObj.CREATOR);
    }

    public static final Creator<ChannelObj> CREATOR = new Creator<ChannelObj>() {
        @Override
        public ChannelObj createFromParcel(Parcel in) {
            return new ChannelObj(in);
        }

        @Override
        public ChannelObj[] newArray(int size) {
            return new ChannelObj[size];
        }
    };

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        if (map != null) {
            this.sourceType = map.get("sourceType");
            this.channelId = map.get("channelId");
            this.mzName = map.get("mzName");
            this.logoUrl = map.get("logoUrl");
            this.recordTime = map.get("recordTime");
            this.ifBack = map.get("ifBack");
            this.id = map.get("id");
            this.commentNum = map.get("commentNum");
            this.praiseNum = map.get("praiseNum");
            this.moveTime = map.get("moveTime");
            this.ifMove = map.get("ifMove");
            this.collectNum = map.get("collectNum");
            this.startTime = map.get("startTime");
            this.endTime  = map.get("endTime");
            this.epgName = map.get("epgName");
            this.collectUids = map.get("collectUids");
            String siteLmStr = map.get("siteLm");
            if(!TextUtils.isEmpty(siteLmStr)){
                JSONArray arr1 = new JSONArray(siteLmStr);
                for (int i = 0; i < arr1.length(); i++) {
                    JSONObject json1 = arr1.optJSONObject(i);
                    if (json1 != null) {
                        SiteLmObj obj1 = new SiteLmObj();
                        obj1.parserJSON(json1);
                        siteLm.add(obj1);
                    }
                }
            }
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

    public String getSourceType() {
        return sourceType;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getMzName() {
        return mzName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public String getIfBack() {
        return ifBack;
    }

    public String getId() {
        return id;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public String getMoveTime() {
        return moveTime;
    }

    public String getIfMove() {
        return ifMove;
    }

    public String getCollectNum() {
        return collectNum;
    }

    public String getEpgName() {return epgName;}

    public List<SiteLmObj> getSiteLm() {
        return siteLm;
    }

    public List<ZbSourceObj> getZbSource() {
        return zbSource;
    }
    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {

    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCollectUids() {
        return collectUids;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sourceType);
        dest.writeString(channelId);
        dest.writeString(mzName);
        dest.writeString(logoUrl);
        dest.writeString(recordTime);
        dest.writeString(ifBack);
        dest.writeString(id);
        dest.writeString(commentNum);
        dest.writeString(praiseNum);
        dest.writeString(moveTime);
        dest.writeString(ifMove);
        dest.writeString(collectNum);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(epgName);
        dest.writeString(collectUids);
        dest.writeTypedList(siteLm);
        dest.writeTypedList(zbSource);
    }
}
