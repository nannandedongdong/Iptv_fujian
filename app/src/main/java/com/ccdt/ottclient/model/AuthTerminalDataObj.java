package com.ccdt.ottclient.model;

import org.json.JSONObject;

import java.util.Map;

/**
 * 机顶盒认证接口 Object
 */
public class AuthTerminalDataObj extends BaseObject{
    private String token;
    private String authDetail;
    private String loginName;
    private String userId;
    private String userName;
    private String portalUrl;
    private String commonIp;
    private String commonPort;
    private String searchIp;
    private String searchPort;

    public void parserJSON(JSONObject jsonObject) {
        if (jsonObject != null) {
            token = jsonObject.optString("token");
            authDetail = jsonObject.optString("authDetail");
            loginName = jsonObject.optString("loginName");
            userId = jsonObject.optString("userId");
            userName = jsonObject.optString("userName");
            portalUrl = jsonObject.optString("portalUrl");
            commonIp = jsonObject.optString("commonIp");
            commonPort = jsonObject.optString("commonPort");
            searchIp = jsonObject.optString("searchIp");
            searchPort = jsonObject.optString("searchPort");
        }
    }

    @Override
    public void parserMap(Map<String, String> map) throws Exception {

    }

    @Override
    public String toString() {
        return "AuthTerminalDataObj{" +
                "token='" + token + '\'' +
                ", authDetail='" + authDetail + '\'' +
                ", loginName='" + loginName + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", portalUrl='" + portalUrl + '\'' +
                ", commonIp='" + commonIp + '\'' +
                ", commonPort='" + commonPort + '\'' +
                ", searchIp='" + searchIp + '\'' +
                ", searchPort='" + searchPort + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuthDetail() {
        return authDetail;
    }

    public void setAuthDetail(String authDetail) {
        this.authDetail = authDetail;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    public String getCommonIp() {
        return commonIp;
    }

    public void setCommonIp(String commonIp) {
        this.commonIp = commonIp;
    }

    public String getCommonPort() {
        return commonPort;
    }

    public void setCommonPort(String commonPort) {
        this.commonPort = commonPort;
    }

    public String getSearchIp() {
        return searchIp;
    }

    public void setSearchIp(String searchIp) {
        this.searchIp = searchIp;
    }

    public String getSearchPort() {
        return searchPort;
    }

    public void setSearchPort(String searchPort) {
        this.searchPort = searchPort;
    }
}
