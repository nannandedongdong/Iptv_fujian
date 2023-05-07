package com.ccdt.ottclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Map;

public class LmIconObj implements IDataParser, Parcelable {
    /*
    "imageText": "电影-背景_stb",
    "imageUrl": "http://www.sitvs.com:15414//multimedia/image/EPOSIDE/2015/06/23/2015_06_23_15_03_03_599.jpg",
    "signCode": "background"
     */
    //这里定义了三个变量来说明读和写的顺序要一致
    private String imageText;
    private String imageUrl;
    private String signCode;

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            setImageText(jsonObject.optString("imageText"));
            setImageUrl(jsonObject.optString("imageUrl"));
            setSignCode(jsonObject.optString("signCode"));
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }


    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSignCode() {
        return signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    @Override
    public String toString() {
        return "LmIconObj{" +
                "imageText='" + imageText + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", signCode='" + signCode + '\'' +
                '}';
    }

    public LmIconObj(){}

    public LmIconObj(Parcel source) {
        //先写入 imageText 再imageUrl 再signCode
        imageText = source.readString();
        imageUrl = source.readString();
        signCode = source.readString();
    }

    //实例化静态内部对象CREATOR实现接口Parcelable.Creator
    public static final Parcelable.Creator<LmIconObj> CREATOR =
            new Creator<LmIconObj>() {

                //将Parcel对象反序列化为LmIconObj
                @Override
                public LmIconObj createFromParcel(Parcel source) {
                    return new LmIconObj(source);
                }

                @Override
                public LmIconObj[] newArray(int size) {
                    return new LmIconObj[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    //实现Parcelable的方法writeToParcel，将ParcelableDate序列化为一个Parcel对象
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //先写入 imageText 再imageUrl 再signCode
        dest.writeString(imageText);
        dest.writeString(imageUrl);
        dest.writeString(signCode);
    }
}
