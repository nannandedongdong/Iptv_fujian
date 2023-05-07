package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.model.MessageObj;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;


public class PayTask extends BaseTask<String> {


    public PayTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(String... params) {

        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_PAY;

        String id = "";

        if (params != null && params.length > 0) {
            id = params[0];
        }


        try {
            ret.data = MyApp.getOTTApi().payProduct(id);
            ret.code = 200;
            ret.message = "支付成功";
        } catch (Exception e) {
            if (e instanceof SuperException) {
                ret.code = ((SuperException) e).getErrorCode();
                ret.message = ((SuperException) e).getErrorDesc();
            } else {
                ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
                ret.message = "系统异常";
            }
            e.printStackTrace();
        }

        return ret;
    }
}
