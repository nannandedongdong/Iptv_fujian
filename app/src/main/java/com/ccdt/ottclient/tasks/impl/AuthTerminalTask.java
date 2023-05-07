package com.ccdt.ottclient.tasks.impl;

import android.os.Build;

import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.config.Config;
import com.ccdt.ottclient.model.AuthTerminalDataObj;
import com.ccdt.ottclient.exception.SuperException;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;


public class AuthTerminalTask extends BaseTask {

    public AuthTerminalTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Object... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_AUTHTERMINAL;
        try {
            AuthTerminalDataObj authTerminalDataObj = MyApp.getOTTApi().authTerminal(Build.SERIAL, null, null);

            if (authTerminalDataObj != null) {
                Account account = Account.getInstance();
                account.token = authTerminalDataObj.getToken();
                account.loginName = authTerminalDataObj.getLoginName();
                account.userId = authTerminalDataObj.getUserId();
                account.userName = authTerminalDataObj.getUserName();

                Config.OttCommActionService_IP = authTerminalDataObj.getCommonIp();
                Config.OttCommActionService_PORT = Integer.valueOf(authTerminalDataObj.getCommonPort());
                Config.OttSearchService_IP = authTerminalDataObj.getSearchIp();
                Config.OttSearchService_PORT = Integer.valueOf(authTerminalDataObj.getSearchPort());
            }
            ret.data = authTerminalDataObj;
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
