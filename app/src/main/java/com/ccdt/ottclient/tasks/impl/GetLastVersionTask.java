package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.Map;

public class GetLastVersionTask extends BaseTask<Map<String, String>>{
    public GetLastVersionTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETLASTVERSION;
        try {

            Map<String, String> lastVersion = OTTServiceApi.getInstance().getLastVersion();
            ret.data = lastVersion;
            ret.code = TaskResult.SUCCESS;
            ret.message = "success";
        } catch (Exception e) {
            e.printStackTrace();
            ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
            ret.message = e.getMessage();
        }
        return ret;
    }
}
