package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class GetVodListTask extends BaseTask<Map<String, String>> {
    private static final String TAG = GetVodListTask.class.getName();

    public GetVodListTask(TaskCallback callback) {
        super(callback);
    }


    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETMOVIELIST;
        Map<String, String> map = null;
        if (params != null && params.length > 0) {
            map = params[0];
        }

        if (map == null) {
            map = new HashMap<>();
        }

        try {
            ret.code = 200;
            ret.message = "success";
            ret.data = OTTServiceApi.getInstance().getVodsSearchResult(map);
            String s = "";
        } catch (Exception e) {
            ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
            ret.message = e.getMessage();
            e.printStackTrace();
        }
        LogUtil.d(TAG, ret.toString());
        return ret;
    }
}
