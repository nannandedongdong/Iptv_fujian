package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.api.OTTApi;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取设备订购的可用包月产品
 */
public class GetMyMonthOrderTask extends BaseTask<Map<String, String>> {

    public GetMyMonthOrderTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETMYMONTHORDER;

        int pageSize = 0;
        int pageNumber = 0;
        Map<String, String> map = null;
        if (params != null && params.length > 0) {
            map = params[0];

        }

        if (map == null) {
            map = new HashMap<>();
        }

        if (map.containsKey("pageSize")) {
            pageSize = Integer.valueOf(map.get("pageSize"));
        }
        if (map.containsKey("pageNumber")) {
            pageNumber = Integer.valueOf(map.get("pageNumber"));
        }

        try {
            ret.data = MyApp.getOTTApi().getMyMonthOrder(pageSize, pageNumber);
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
            return ret;
        }

        return ret;
    }
}
