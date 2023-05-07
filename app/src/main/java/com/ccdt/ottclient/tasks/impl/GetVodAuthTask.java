package com.ccdt.ottclient.tasks.impl;

import android.util.Log;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.model.ProductInfo;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import org.json.JSONObject;

public class GetVodAuthTask extends BaseTask<String> {

    private static final String TAG = GetVodAuthTask.class.getSimpleName();

    public GetVodAuthTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(String... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETVODAUTH;

        String mzId = "";
        if (params != null && params.length > 0) {
            mzId = params[0];
        }

        try {
            String result = MyApp.getOTTApi().authorization("vod", mzId);
            Log.d("slf 鉴权结果", "result=" + result);
            ret.data = result;
            ret.code = TaskResult.SUCCESS;
            ret.message = "success";
        } catch (Exception e) {
            if (e instanceof SuperException) {
                ret.code = ((SuperException) e).getErrorCode();
                ret.message = ((SuperException) e).getErrorDesc();

                if (ret.code == 1 || ret.code == 5 || ret.code == 0 || ret.code == 10) {
                    String jsonData = ((SuperException) e).getJsonData();
                    Log.d("slf 鉴权结果", "result=" + jsonData);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        ProductInfo pro = new ProductInfo();
                        pro.parserJSON(jsonObject);
                        ret.data = pro;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            } else {
                ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
                ret.message = e.getMessage();
            }
            e.printStackTrace();
        }
        return ret;
    }
}
