package com.ccdt.ottclient.tasks.impl;

import android.text.TextUtils;

import com.ccdt.ott.search.thrift.CommonSearchResult;
import com.ccdt.ott.util.ConstValue;
import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.model.CollectionObj;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.HashMap;
import java.util.Map;


public class GetCollectedListTask extends BaseTask<Map<String, String>> {
    public GetCollectedListTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {

        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETCOLLECTEDLIST;

        Map<String, String> map = null;
        if (params != null && params.length > 0) {
            map = params[0];
        }

        if (map == null) {
            map = new HashMap<>();
        }

        map.put(ConstantKey.ROAD_TYPE_SEARCHTYPE, ConstValue.SearchType.collects.name());
        map.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);//设备类型
        map.put(ConstantKey.ROAD_TYPE_PAGESIZE, "1000");
        String userId = Account.getInstance().userId;
        if (!TextUtils.isEmpty(userId)) {
            map.put(ConstantKey.ROAD_TYPE_MEMBERID, userId);
        }
        try {
            CommonSearchResult searchResult = OTTServiceApi.getInstance().getSearchResult(map);
            ret.data = new CommonSearchInvokeResult<CollectionObj>(searchResult, CollectionObj.class);
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
