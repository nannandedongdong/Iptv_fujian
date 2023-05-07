package com.ccdt.ottclient.model;

import com.ccdt.ott.search.thrift.CommonSearchResult;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonSearchInvokeResult<T> extends BaseObject {

    private int totalNum;
    private int rtListSize;
    private List<T> rtList;
    private Class<?> mClass;

    public CommonSearchInvokeResult(CommonSearchResult result,Class<?> cls) throws Exception {
        this.mClass = cls;
        if(result != null){
            this.totalNum = result.getTotalNum();
            this.rtListSize = result.getRtListSize();
            rtList = new ArrayList<>();
            List<Map<String, String>> list = result.getRtList();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    if (map != null) {
                        this.parserMap(map);
                    }
                }
            }
        }
    }

    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {


    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {
        T t = ((T) mClass.newInstance());
        Method method = mClass.getMethod("parserMap", Map.class);
        method.invoke(t, map);
        rtList.add(t);

    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<T> getRtList() {
        return rtList;
    }

    public void setRtList(List<T> rtList) {
        this.rtList = rtList;
    }

    public int getRtListSize() {
        return rtListSize;
    }

    public void setRtListSize(int rtListSize) {
        this.rtListSize = rtListSize;
    }

    @Override
    public String toString() {
        return "CommonSearchInvokeResult{" +
                "totalNum=" + totalNum +
                ", rtListSize=" + rtListSize +
                ", rtList=" + rtList +
                '}';
    }
}
