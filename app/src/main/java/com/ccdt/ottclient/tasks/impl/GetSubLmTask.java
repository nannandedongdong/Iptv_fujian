package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.config.Config;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

public class GetSubLmTask extends BaseTask<String> {

    /**
     * 获取子栏目列表
     * eg. GetSubLmTask.executeTask(this, "lmid");
     *
     * @param callback 回调接口
     * @param lmId 栏目ID
     */
    public static void executeTask(TaskCallback callback, String lmId) {
        new GetSubLmTask(callback).execute(lmId);
    }

    public GetSubLmTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(String... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETSUBLM;
        try {
            ret.data = OTTServiceApi.getInstance().getSubLm(params[0]);
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
