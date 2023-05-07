package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ccdt.ottclient.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/*
{
    id=NEWSf8de239d53704ece9274e5fde335,
    content=********************************,
    editPerson=系统管理员,
    source=中新网,
    updateTime=2015-11-1814: 41: 41,
    shortTitle=巴黎郊区发生枪击1人受伤或与反恐行动有关,
    mzName=巴黎郊区发生枪击1人受伤或与反恐行动有关,
    titleImg=http: //192.167.1.117: 15414//multimedia/image/NEWS/2015/11/18/NEWSf8de239d53704ece9274e5fde335.jpg,
    detailHref=http: //192.167.1.117: 15414//html/qhys/zixun/NEWSf8de239d53704ece9274e5fde335.html
}
 */
public class NewsInfoObj extends BaseObject implements Parcelable {

    private String id;
    private String content;
    private String editPerson;
    private String source;
    private String updateTime;
    private String shortTitle;
    private String mzName;
    private String titleImg;
    private String detailHref;
    private ArrayList<String> newsImgs = new ArrayList<>();

    public NewsInfoObj() {
    }


    protected NewsInfoObj(Parcel in) {
        id = in.readString();
        content = in.readString();
        editPerson = in.readString();
        source = in.readString();
        updateTime = in.readString();
        shortTitle = in.readString();
        mzName = in.readString();
        titleImg = in.readString();
        detailHref = in.readString();
        newsImgs = in.createStringArrayList();
    }

    public static final Creator<NewsInfoObj> CREATOR = new Creator<NewsInfoObj>() {
        @Override
        public NewsInfoObj createFromParcel(Parcel in) {
            return new NewsInfoObj(in);
        }

        @Override
        public NewsInfoObj[] newArray(int size) {
            return new NewsInfoObj[size];
        }
    };

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
//        if (jsonObject != null) {
//            this.id = jsonObject.optString("id");
//            this.editPerson = jsonObject.optString("editPerson");
//            this.source = jsonObject.optString("source");
//            this.updateTime = jsonObject.optString("updateTime");
//            this.shortTitle = jsonObject.optString("shortTitle");
//            this.mzName = jsonObject.optString("mzName");
//            this.titleImg = jsonObject.optString("titleImg");
//            this.detailHref = jsonObject.optString("detailHref");
//            this.content = jsonObject.optString("content");
//        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        if (map != null) {
            this.id = map.get("id");
            this.editPerson = map.get("editPerson");
            this.source = map.get("source");
            this.updateTime = map.get("updateTime");
            this.shortTitle = map.get("shortTitle");
            this.mzName = map.get("mzName");
            this.titleImg = map.get("titleImg");
            this.detailHref = map.get("detailHref");
            this.content = map.get("content");
            String imgs = map.get("newsImgs");
            if (!TextUtils.isEmpty(imgs)) {
                JSONArray array = new JSONArray(imgs);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    newsImgs.add(object.optString("imgUrl"));
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEditPerson() {
        return editPerson;
    }

    public void setEditPerson(String editPerson) {
        this.editPerson = editPerson;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getMzName() {
        return mzName;
    }

    public void setMzName(String mzName) {
        this.mzName = mzName;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getDetailHref() {
        return detailHref;
    }

    public void setDetailHref(String detailHref) {
        this.detailHref = detailHref;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getNewsImgs() {
        return newsImgs;
    }

    public void setNewsImgs(ArrayList<String> newsImgs) {
        this.newsImgs = newsImgs;
    }

    @Override
    public String toString() {
        return "NewsInfoObj{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", editPerson='" + editPerson + '\'' +
                ", source='" + source + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", shortTitle='" + shortTitle + '\'' +
                ", mzName='" + mzName + '\'' +
                ", titleImg='" + titleImg + '\'' +
                ", detailHref='" + detailHref + '\'' +
                ", newsImgs=" + newsImgs +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(editPerson);
        dest.writeString(source);
        dest.writeString(updateTime);
        dest.writeString(shortTitle);
        dest.writeString(mzName);
        dest.writeString(titleImg);
        dest.writeString(detailHref);
        dest.writeStringList(newsImgs);
    }
}


