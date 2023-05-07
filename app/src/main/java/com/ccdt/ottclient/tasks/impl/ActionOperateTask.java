package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.HashMap;
import java.util.Map;

public class ActionOperateTask extends BaseTask<Map<String, String>> {

    public ActionOperateTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {

        TaskResult ret = new TaskResult();

        ret.taskId = Constants.TASK_ACTIONOPERATE;

        Map<String, String> map = null;
        if (params != null && params.length > 0) {
            map = params[0];
        }

        if (map == null) {
            map = new HashMap<>();
        }

        try {
            ret.data = OTTServiceApi.getInstance().actionOperate(map);
            ret.code = TaskResult.SUCCESS;
            ret.message = "success";
        } catch (Exception e) {
            e.printStackTrace();
            ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
            ret.message = e.getLocalizedMessage();
            ret.data = -1;
        }

        return ret;
    }
}
