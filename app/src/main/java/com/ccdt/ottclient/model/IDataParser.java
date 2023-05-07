package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.util.Map;

public interface IDataParser {
    void parserJSON(JSONObject jsonObject) throws Exception;
    void parserMap(Map<String, String> map) throws Exception;

}
