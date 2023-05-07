package com.ccdt.ottclient;

/*
{
    "message": {
        "status": "200",
        "msg": "success",
        "serverTime": "20151027165829"
    },
    "data": {
        "token": "ST-722-E4EUMNXJK49Zve0BEIJa-cas",
        "authDetail": "[unknown]认证成功，认证票据为:ST-722-E4EUMNXJK49Zve0BEIJa-cas",
        "loginName": "测试通用设备",
        "userId": "108",
        "userName": "unknown",
        "portalUrl": "www.sitvs.com:15414",
        "commonIp": "www.sitvs.com",
        "commonPort": "15415",
        "searchIp": "www.sitvs.com",
        "searchPort": "15413"
    }
}
 */

import com.ccdt.ottclient.model.AccountInfo;

/**
 * 账户信息存储
 * Created by hanyue on 2015/10/22.
 */
public class Account {
    private volatile static Account account;
    public String token;  //TOKEN
    public String userId;
    public String userName;
    public String loginName;
    public AccountInfo info;


    private Account() {

    }

    public static Account getInstance() {
        if (account == null) {
            synchronized (Account.class) {
                account = new Account();
            }
        }
        return account;
    }
}
