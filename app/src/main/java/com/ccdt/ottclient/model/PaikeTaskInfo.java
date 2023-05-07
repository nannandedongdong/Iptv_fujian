package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.util.Map;

/*
 {
        id=30,
        jobDesc=myjob2,
        updateTime=2015-11-2618: 08: 51,
        bannerImg=http: //192.167.1.117: 15414//multimedia/image/MAKING/2015/11/26/2015_11_26_17_05_22_2331.png,
        jobName=myjob2,
        detailHref=http: //192.167.1.117: 15414//html/qhys/30.html
    },
 */
public class PaikeTaskInfo extends BaseObject {

    private String id;
    private String jobDesc;
    private String updateTime;
    private String bannerImg;
    private String jobName;
    private String detailHref;

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        if (map != null) {
            setId(map.get("id"));
            setJobDesc(map.get("jobDesc"));
            setUpdateTime(map.get("updateTime"));
            setBannerImg(map.get("bannerImg"));
            setJobName(map.get("jobName"));
            setDetailHref(map.get("detailHref"));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDetailHref() {
        return detailHref;
    }

    public void setDetailHref(String detailHref) {
        this.detailHref = detailHref;
    }
}
