package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LmInfoObj implements IDataParser, Parcelable {
    private String lmId;
    private String lmName;
    private String type;
    private List<LmIconObj> lmIcon = new ArrayList<>();
    public LmInfoObj() {
    }

    protected LmInfoObj(Parcel in) {
        lmId = in.readString();
        lmName = in.readString();
        type = in.readString();
        lmIcon = in.createTypedArrayList(LmIconObj.CREATOR);
    }

    public static final Creator<LmInfoObj> CREATOR = new Creator<LmInfoObj>() {
        @Override
        public LmInfoObj createFromParcel(Parcel in) {
            return new LmInfoObj(in);
        }

        @Override
        public LmInfoObj[] newArray(int size) {
            return new LmInfoObj[size];
        }
    };

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        lmIcon.clear();
        if (map != null) {
            this.lmName = map.get("lmName");
            this.lmId = map.get("lmId");
            this.type = map.get("type");
            String icStrs = map.get("lmIcon");
            if (!TextUtils.isEmpty(icStrs)) {
                JSONArray jsonArray = new JSONArray(icStrs);
                for (int j = 0; j < jsonArray.length(); j++) {
                    LmIconObj icObj = new LmIconObj();
                    icObj.parserJSON(jsonArray.getJSONObject(j));
                    lmIcon.add(icObj);
                }
            }
        }
    }

    public String getLmId() {
        return lmId;
    }

    public void setLmId(String lmId) {
        this.lmId = lmId;
    }

    public String getLmName() {
        return lmName;
    }

    public void setLmName(String lmName) {
        this.lmName = lmName;
    }

    public List<LmIconObj> getLmIcon() {
        return lmIcon;
    }

    public void setLmIcon(List<LmIconObj> lmIcon) {
        this.lmIcon = lmIcon;
    }

    public String getType() {
        return type;
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
        dest.writeString(lmId);
        dest.writeString(lmName);
        dest.writeString(type);
        dest.writeTypedList(lmIcon);
    }
}
