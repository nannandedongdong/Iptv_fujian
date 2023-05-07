package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.HashMap;
import java.util.Map;


public class GetVodDetailTask extends BaseTask<Map<String, String>> {

    public GetVodDetailTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETVODDETAIL;

        Map<String, String> map = null;

        if (params != null && params.length > 0) {
            map = params[0];
        } else {
            map = new HashMap<>();
        }

        try {
            ret.data = OTTServiceApi.getInstance().getVodDetail(map);
            ret.code = 200;
            ret.message = "success";
        } catch (Exception e) {
            if (e instanceof SuperException) {
                ret.code = ((SuperException) e).getErrorCode();
                ret.message = ((SuperException) e).getErrorDesc();
            } else {
                ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
                ret.message = e.getMessage();
            }
            e.printStackTrace();
        }

        return ret;
    }
}
