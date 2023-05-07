package com.ccdt.ottclient.tasks.impl;


import android.text.TextUtils;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.model.DataMessage;
import com.ccdt.ottclient.model.InvokeResult;
import com.ccdt.ottclient.model.OrderObj;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductOrderTask extends BaseTask<Map<String, String>> {


    public ProductOrderTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {

        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_PRODUCTORDER;

        Map<String, String> map = null;
        if (params != null && params.length > 0) {
            map = params[0];
        }

        if (map == null) {
            map = new HashMap<>();
        }

        String productId = map.get("productId");
        String assetId = map.get("assetId");
        String assetName = map.get("assetName");

        String jsonStr = null;
        try {
            jsonStr = MyApp.getOTTApi().productOrder(productId, assetId, assetName);
            OrderObj order = new OrderObj();
            JSONObject jsonObject = new JSONObject(jsonStr);
            order.parserJSON(jsonObject.optJSONObject("data"));
            JSONObject msgJSON = jsonObject.optJSONObject("message");
            ret.data = order;
            ret.code = TaskResult.SUCCESS;
            ret.message = msgJSON.optString("msg");
        } catch (Exception e) {
            if(e instanceof SuperException){
                SuperException se = (SuperException) e;
//                jsonStr = se.getJsonData();
//                if(!TextUtils.isEmpty(jsonStr)){
//                    try {
//                        JSONObject json = new JSONObject(jsonStr);
//                        JSONObject jsonObject = json.optJSONObject("message");
//                        DataMessage dataMessage = new DataMessage();
//                        dataMessage.parserJSON(jsonObject);
//                        ret.data = dataMessage;
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
//                        ret.message = ex.getMessage();
//                    }
//                }
                ret.code = se.getErrorCode();
                ret.message = se.getMessage();
            } else {
                ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
                ret.message = e.getMessage();
                e.printStackTrace();
            }
        }


        return ret;
    }
}
