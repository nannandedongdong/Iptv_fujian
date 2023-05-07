package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.util.Map;

/*
 "pages": {
 "currentNum": 1,
 "pageSize": 5,
 "totalPage": 1,
 "totalNumber": 2
 }
 */
public class PageObj extends BaseObject {

    private int currentNum;
    private int pageSize;
    private int totalPage;
    private int totalNumber;

    @Override
    public void parserJSON(JSONObject jsonObject){
        if (jsonObject != null) {
            this.currentNum = jsonObject.optInt("currentNum");
            this.pageSize = jsonObject.optInt("pageSize");
            this.totalPage = jsonObject.optInt("totalPage");
            this.totalNumber = jsonObject.optInt("totalNumber");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    @Override
    public String toString() {
        return "PageObj{" +
                "currentNum=" + currentNum +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", totalNumber=" + totalNumber +
                '}';
    }
}
