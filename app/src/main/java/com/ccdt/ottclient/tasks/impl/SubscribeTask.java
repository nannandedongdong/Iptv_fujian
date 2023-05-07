package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.model.MessageObj;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import org.json.JSONObject;

/**
 * Created by iSun on 2016/1/7.
 */
public class SubscribeTask extends BaseTask<String> {
    public SubscribeTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(String... params) {

        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_SUBSCRIBE;

        String productId = "";
        if (params != null && params.length>0) {
            productId = params[0];
        }

        try {
            String unsubscribe = MyApp.getOTTApi().subscribe(productId);
            MessageObj messageObj = new MessageObj();
            messageObj.parserJSON(new JSONObject(unsubscribe));
            ret.data = messageObj;
            ret.code = 200;
            ret.message = messageObj.getMsg();
        } catch (Exception e) {
            if (e instanceof SuperException) {
                ret.code = ((SuperException) e).getErrorCode();
                ret.message = ((SuperException) e).getErrorDesc();
            } else {
                ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
                ret.message = e.getMessage();
            }
            e.printStackTrace();
            return ret;
        }


        return ret;
    }
}
