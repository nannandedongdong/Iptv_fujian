package com.ccdt.ottclient.tasks.impl;

import android.text.TextUtils;

import com.ccdt.ott.search.thrift.CommonSearchResult;
import com.ccdt.ott.util.ConstValue;
import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.NewsInfoObj;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.HashMap;
import java.util.Map;


public class GetNewsListTask extends BaseTask<Map<String, String>> {

    private static final String TAG = GetNewsListTask.class.getName();
    public GetNewsListTask(TaskCallback callback) {
        super(callback);
    }



    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETNEWSLIST;
        Map<String, String> map = null;

        if (params != null && params.length>0) {
            map = params[0];
        }

        if (map ==null) {
           map = new HashMap<>();
        }

        String userId = Account.getInstance().userId;
        if (!TextUtils.isEmpty(userId)) {
            map.put("memberId", userId);
        }
        map.put("searchType", ConstValue.SearchType.news.name());

        CommonSearchResult result = OTTServiceApi.getInstance().getSearchResult(map);

        try {
            ret.code = 200;
            ret.message = "success";
            ret.data = new CommonSearchInvokeResult<NewsInfoObj>(result, NewsInfoObj.class);
        } catch (Exception e) {
            ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
            ret.message = e.getMessage();
            e.printStackTrace();
        }

        return ret;

    }
}
