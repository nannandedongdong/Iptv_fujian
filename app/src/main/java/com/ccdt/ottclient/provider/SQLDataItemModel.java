package com.ccdt.ottclient.provider;

import java.io.Serializable;

/**
 * 封装数据类
 * @author hezb
 */
public class SQLDataItemModel implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_MAKING = 0;//拍客
	public static final int TYPE_MOVE = 1;//视频

	private int type = TYPE_MAKING;
	private String mzId = "";
	private String title = "";
	private String posterUrl = "";
	private String playUrl = "";
	private long playPosition = 0;
	private int seriesPosition = 0;
	private long time = 0;//存储的时间 存的时候不用设置，自动设置

	public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getMzId() {
        return mzId;
    }
    public void setMzId(String mzId) {
        this.mzId = mzId;
    }
    public String getPlayUrl() {
        return playUrl;
    }
    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPosterUrl() {
        return posterUrl;
    }
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public long getPlayPosition() {
        return playPosition;
    }
    public void setPlayPosition(long playPosition) {
        this.playPosition = playPosition;
    }
    public int getSeriesPosition() {
        return seriesPosition;
    }
    public void setSeriesPosition(int seriesPosition) {
        this.seriesPosition = seriesPosition;
    }
    
}
