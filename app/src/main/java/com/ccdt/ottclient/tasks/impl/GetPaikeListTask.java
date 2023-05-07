package com.ccdt.ottclient.tasks.impl;

import com.ccdt.ott.search.thrift.CommonSearchResult;
import com.ccdt.ott.util.ConstValue;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.exception.SuperExceptionFactory;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.PaikeInfoObj;
import com.ccdt.ottclient.tasks.BaseTask;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;

import java.util.HashMap;
import java.util.Map;

public class GetPaikeListTask extends BaseTask<Map<String, String>> {
    public GetPaikeListTask(TaskCallback callback) {
        super(callback);
    }

    @Override
    protected TaskResult doInBackground(Map<String, String>... params) {
        TaskResult ret = new TaskResult();
        ret.taskId = Constants.TASK_GETPAIKELIST;
        Map<String, String> map = null;

        if(params!=null && params.length > 0){
            map = params[0];
        }

        if(map == null){
            map = new HashMap<>();
        }

        map.put(ConstantKey.ROAD_TYPE_SEARCHTYPE, ConstValue.SearchType.paike.name());
        map.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, ConstantKey.ROAD_TYPE_PACK_SHOWFILED);
        map.put(ConstantKey.ROAD_TYPE_STATUS, "6");//已发布
//        String userId = Account.getInstance().userId;
//        if (!TextUtils.isEmpty(userId)) {
//            map.put("memberId", userId);
//        }
        try {
            ret.code = 200;
            ret.message = "success";
            CommonSearchResult searchResult = OTTServiceApi.getInstance().getSearchResult(map);
            ret.data = new CommonSearchInvokeResult<PaikeInfoObj>(searchResult, PaikeInfoObj.class);
        } catch (Exception e) {
            ret.code = SuperExceptionFactory.ERROR_EXCEPTION_NOT_CUSTOM;
            ret.message = e.getMessage();
            e.printStackTrace();
        }

        return ret;
    }
}
