package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.config.Config;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.ChannelObj;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.HashMap;
import java.util.Map;

public class GetChannelListTask extends BaseTask<Map<String, String>> {

    public GetChannelListTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETCHANNELLIST;

        Map<String, String> map = null;

        if (params != null && params.length > 0) {
            map = params[0];
        }

        if(map == null){
            map = new HashMap<>();
        }

        try {
            CommonSearchInvokeResult<ChannelObj> searchResult = OTTServiceApi.getInstance().getLiveChannelsSearchResult(map);
            ret.data = searchResult;
            ret.code = TaskResult.SUCCESS;
            ret.message = "success";
        } catch (Exception e) {
                e.printStackTrace();
        }
        return ret;
    }
}
