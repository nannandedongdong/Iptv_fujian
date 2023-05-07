package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by hanyue on 2015/12/17.
 */


/**
 * 返回结果：
 {
 "message": {
 "status": "200",
 "msg": "ok",
 "serverTime": "20151211160851"
 },
 "data": {
 "memberIcon ": "http://192.167.1.235/multimedia/images/1.png",//设备头像
 "terminalSn": "test1", //设备唯一标识
 "terminalAccount": "151210369028", //账户编码
 "nickName": "测试设备1", //设备昵称/名称
 "terminalAccountName": "测试用户1",//账户名称
 "balance": 970, //账户余额
 "id": 159//设备id主键
 }
 }
 */
public class AccountInfo extends BaseObject {

    public String memberIcon; //头像
    public String terminalSn;
    public String terminalAccount;
    public String nickName;
    public String terminalAccountName;
    public double balance;
    public int id;



    @Override
    public void parserJSON(JSONObject jsonObject) throws Exception {
        if (jsonObject != null) {
            memberIcon = jsonObject.optString("memberIcon");
            terminalSn =jsonObject.optString("terminalSn");
            terminalAccount = jsonObject.optString("terminalAccount");
            nickName = jsonObject.optString("nickName");
            terminalAccountName = jsonObject.optString("terminalAccountName");
            balance = jsonObject.optDouble("balance", 0);
            id = jsonObject.optInt("id");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }
}
