package com.ccdt.ottclient.api;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.config.Config;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.utils.AppUtil;
import com.ccdt.ottclient.utils.NetWorkHelper;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by hanyue on 2015/10/22.
 */
public class Api {
    private String host;
    private int mConnectTimeout;
    public SyncHttpClient asyncHttpClient = new SyncHttpClient();
    public static final String TAG = "request";
    private Context context;
    public static final int RESULT_OK = 200;


    public Api(Context context) {
        this.context = context;
        this.mConnectTimeout = 5000;
        // this.mReadTimeout = 30000;
        asyncHttpClient.setTimeout(this.mConnectTimeout);
        asyncHttpClient.setEnableRedirects(true);
        asyncHttpClient.setURLEncodingEnabled(true);
//        this.host = "192.167.1.104:8080";
    }

    public String get(String url) throws Exception {
        return get(url, null);
    }

    public String get(String url, RequestParams params) throws Exception {
        return get(url, params, Account.getInstance().token != null);
    }

    public String get(String url, RequestParams params, boolean auth) throws Exception {
        if (!NetWorkHelper.checkNetState(context)) {
            throw new SuperExceptionFactory().create(SuperExceptionFactory.ERROR_NETWORK_UNAVAILABLE,
                    SuperExceptionFactory.DESC_ERROR_NETWORK_UNAVAILABLE, null);
        }
        if (params == null) {
            params = new RequestParams();
        }
        setDefaultParams(params);

        final StringBuffer stringBuffer = new StringBuffer();
        int code;
        asyncHttpClient.removeAllHeaders();

        Log.d(TAG, "get -- params--" + params.toString());
        Log.d(TAG, "get -- url--" + url);
        asyncHttpClient.get(url, params, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                stringBuffer.append(arg2);
                Log.d(TAG, "get -- result--" + stringBuffer.toString());
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                arg3.printStackTrace();
            }
        });
        String resultJson = stringBuffer.toString();
        int resultCode = SuperExceptionFactory.ERROR_NO_RESPONSE;
        String resultMsg = SuperExceptionFactory.DESC_ERROR_NO_RESPONSE;
        try {
            JSONObject obj1 = new JSONObject(resultJson);
            JSONObject obj2 = obj1.optJSONObject("message");
            resultCode = Integer.valueOf(obj2.optString("status"));
            resultMsg = obj2.optString("msg");
            if (resultCode != RESULT_OK) {
                throw SuperExceptionFactory.create(resultCode, resultMsg, resultJson);
            }

        } catch (Exception e) {
            // TODO: handle exception
            throw SuperExceptionFactory.create(resultCode, resultMsg, resultJson);
        }

        return resultJson;

    }

    public int getConnectionTimeout() {
        return this.mConnectTimeout;
    }

    /**
     * 设置默认请求参数
     */
    public void setDefaultParams(RequestParams params) {
        // 客户端版本号
        params.put("appver", AppUtil.getVersionCode());
        // 客户端签名，通过多个设备参数计算得来
        params.put("sign", "fajk8gyayigdagyudag");
        // 时间戳
        params.put("call_id", System.currentTimeMillis());
        // 0-android，1-ios
        params.put("platform", 0);
    }

    public String post(String url, RequestParams params) throws Exception {
        return post(url, params, false);
    }

    public String post(String url, RequestParams params, boolean apikey) throws Exception {
        return post(url, params, apikey, Account.getInstance().token != null);
    }

    /******
     * @param auth 是否登录 true为已登录
     ********/
    public String post(String url, RequestParams params, boolean apikey, boolean auth) throws Exception {
        if (!NetWorkHelper.checkNetState(context)) {
            throw new SuperExceptionFactory().create(SuperExceptionFactory.ERROR_NETWORK_UNAVAILABLE,
                    SuperExceptionFactory.DESC_ERROR_NETWORK_UNAVAILABLE, null);
        }
        if (params == null) {
            params = new RequestParams();
        }
        setDefaultParams(params);
        final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(MyApp.getInstance());
        final StringBuffer stringBuffer = new StringBuffer();


        Log.d("request", "post -- params--" + params.toString());
        Log.d("request", "post -- url--" + url);
        asyncHttpClient.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }


            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                stringBuffer.append(arg2);
                Log.d("request", "post -- result--" + stringBuffer.toString());
            }


        });
        String resultJson = stringBuffer.toString();
        int resultCode = SuperExceptionFactory.ERROR_NO_RESPONSE;
        String resultMsg = SuperExceptionFactory.DESC_ERROR_NO_RESPONSE;
        try {
            JSONObject obj1 = new JSONObject(resultJson);
            resultCode = Integer.valueOf(obj1.optString("status"));
            resultMsg = obj1.optString("msg");
            if (resultCode != RESULT_OK) {
                throw SuperExceptionFactory.create(resultCode, resultMsg, resultJson);
            }

        } catch (Exception e) {
            // TODO: handle exception
            throw SuperExceptionFactory.create(resultCode, resultMsg, resultJson);
        }

        return resultJson;

    }

    public void cancelRequest() {
        // asyncHttpClient.abort();

        asyncHttpClient.cancelAllRequests(true);

    }

    public String url(String path) {
        return url(path, true);
    }

    public String url(String path, boolean https) {
        return url(Config.HOST, path, false);
    }

    public String url(String host, String path, boolean https) {
        String token = Account.getInstance().token;
        if (!TextUtils.isEmpty(token) && !"null".equals(token)) {
            token = "?ticket=" + token;
        } else {
            token = "?";
        }
        Object[] objArr = new Object[4];
        objArr[0] = https ? "https" : "http";
        objArr[1] = host;
        objArr[2] = path;
        objArr[3] = token;
        return String.format("%s://%s%s%s", objArr);
    }

}
