package com.ccdt.ottclient.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InvokeResult<T> extends BaseObject {

    private PageObj pages;
    private List<T> dList;
    private String mClass;

    public InvokeResult(Class<?> cls) {
        if (cls != null) {
            mClass = cls.getName();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            Iterator<String> keys = jsonObject.keys();
            String key;
            while (keys.hasNext()) {
                key = keys.next();
                if ("data".equals(key)) {
                    JSONObject dataJson = jsonObject.optJSONObject("data");
                    if (dataJson != null) {
                        keys = dataJson.keys();
                        while (keys.hasNext()) {
                            key = keys.next();
                            if ("pages".equals(key)) {
                                JSONObject jsonPage = dataJson.optJSONObject("pages");
                                if (jsonPage != null) {
                                    pages = new PageObj();
                                    pages.parserJSON(jsonPage);
                                }
                            } else if ("dList".equals(key)) {
                                JSONArray jsonArrList = dataJson.optJSONArray("dList");
                                if (jsonArrList != null && jsonArrList.length() > 0) {
                                    JSONObject jsonPro;
                                    dList = new ArrayList<>();
                                    T t;
                                    Class<T> clazz;
                                    if (!TextUtils.isEmpty(mClass)) {
                                        clazz = (Class<T>) Class.forName(mClass);
                                        for (int i = 0; i < jsonArrList.length(); i++) {
                                            jsonPro = jsonArrList.optJSONObject(i);
                                            if (jsonPro != null) {
                                                t = clazz.newInstance();
                                                Method method = clazz.getMethod("parserJSON", JSONObject.class);
                                                method.invoke(t, jsonPro);
                                                dList.add(t);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    public PageObj getPages() {
        return pages;
    }

    public List<T> getdList() {
        return dList;
    }

    @Override
    public String toString() {
        return "InvokeResult{" +
                "pages=" + pages +
                ", dList=" + dList +
                '}';
    }
}
