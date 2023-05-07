package com.ccdt.ottclient.api;

import android.text.TextUtils;

import com.ccdt.ott.common.service.OttCommActionService;
import com.ccdt.ott.search.service.OttSearchService;
import com.ccdt.ott.search.thrift.CommonSearchResult;
import com.ccdt.ott.util.ConstValue;
import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.config.Config;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.ChannelObj;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.model.NewsInfoObj;
import com.ccdt.ottclient.model.ProgramObj;
import com.ccdt.ottclient.model.VodDetailObj;
import com.ccdt.ottclient.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangdx
 * @Package com.ccdt.news.utils
 * @ClassName: ApiNewUtils
 * @Description: 数据請求的工且类
 * @date 2015-4-21 上午11:43:53
 */

public class OTTServiceApi {

    private static final String TAG = OTTServiceApi.class.getSimpleName();

    private static OTTServiceApi instance;
    private OttCommActionService commS;
    private OttSearchService searchS;

    private OTTServiceApi() {
        commS = new OttCommActionService(
                Config.OttCommActionService_IP, Config.OttCommActionService_PORT);

        searchS = new OttSearchService(
                Config.OttSearchService_IP, Config.OttSearchService_PORT);
    }

    public static OTTServiceApi getInstance() {
        if (instance == null) {
            instance = new OTTServiceApi();
        }
        return instance;
    }

    /**
     * 获取子栏目信息
     *
     * @param lmId   栏目ID
     * @return
     */
    public List<LmInfoObj> getSubLm(String lmId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("siteId", Config.SITE_ID);
        if (!TextUtils.isEmpty(lmId)) {
            params.put("lmId", lmId);
        }
        List<Map<String, String>> result = commS.getSubLm(params);
        List<LmInfoObj> columnList = new ArrayList<>();
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                Map<String, String> map = result.get(i);
                if (map != null) {
                    LmInfoObj lmInfoObj = new LmInfoObj();
                    lmInfoObj.parserMap(map);
                    columnList.add(lmInfoObj);
                }
            }
        }

        LogUtil.d(TAG, columnList.toString());

        return columnList;
    }


    /**
     * 获取某个栏目下的媒資列表信息
     */
    public CommonSearchResult getSearchResult(Map<String, String> params) {

        if (params == null) {
            params = new HashMap<>();
        }

        params.put(ConstantKey.ROAD_TYPE_SITEID, Config.SITE_ID);

        return searchS.getSearchResult(params);
    }


    public CommonSearchInvokeResult<VodDetailObj> getVodsSearchResult(Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
//        String userId = Account.getInstance().userId;
//        if (!TextUtils.isEmpty(userId)) {
//            params.put("memberId", userId);
//        }
        params.put("searchType", ConstValue.SearchType.vods.name());
        CommonSearchResult result = getSearchResult(params);

        return new CommonSearchInvokeResult<VodDetailObj>(result, VodDetailObj.class);

    }
    public CommonSearchInvokeResult<ChannelObj> getLiveChannelsSearchResult(Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("searchType", ConstValue.SearchType.zhibo.name());
        params.put(ConstantKey.ROAD_TYPE_SITEID, Config.SITE_ID);

        CommonSearchResult result = getSearchResult(params);

        return new CommonSearchInvokeResult<ChannelObj>(result, ChannelObj.class);

    }
    public CommonSearchInvokeResult<ProgramObj> getLiveEpgsSearchResult(Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("searchType", ConstValue.SearchType.epgs.name());
        CommonSearchResult result = getSearchResult(params);

        return new CommonSearchInvokeResult<ProgramObj>(result, ProgramObj.class);

    }

    /**
     * @return List<Map<String,String>>
     * @Description: 获取某个栏目下的列表信息
     * @author wangdx
     * @date 2015-4-22下午1:32:48
     */
    public CommonSearchResult getSearchListAndToalNum(Map<String, String> params) {
        return searchS.getSearchResult(params);
    }

    /**
     * 获取资产的详情
     *
     * @param params
     * @return
     */
    public Map<String, String> getMzDetail(Map<String, String> params) {

        // 获取影片详情数据
//        request = new Request(ITvRequestFactory.REQUEST_TYPE_GET_DETAIL);
//        request.put(ConstantKey.ROAD_TYPE_SITEID, ConstantValue.SITE_ID);
//        request.put(ConstantKey.ROAD_TYPE_ID, mzId);//资产id
//        request.put(ConstantKey.ROAD_TYPE_SEARCHTYPE, ConstValue.SearchType.vods.name());//搜索类型
//        if (isLogin) {
//            request.put(ConstantKey.ROAD_TYPE_MEMBERID, userId);
//        }
//        request.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, ConstantKey.ROAD_TYPE_COMMENTNUM);//需要那些字段
//        request.put(ConstantKey.ROAD_TYPE_ISRECOMMEND, "true");
//        request.put(ConstantKey.ROAD_TYPE_RECOMMENDNUM, "8");
//        request.put(ConstantKey.ROAD_TYPE_RECOMMEND_SHOWFIELDS, ConstantValue.SHOWFIELDS_RECOMMEND);
//        request.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);//海报类型
//        request.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);//设备类型
//        requests.add(request);

        if (params == null) {
            params = new HashMap<>();
        }
        String userId = Account.getInstance().userId;
        if (!TextUtils.isEmpty(userId)) {
            params.put("memberId", userId);
        }
        params.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);//设备类型
        params.put(ConstantKey.ROAD_TYPE_SITEID, Config.SITE_ID);
        Map<String, String> mzDetail = searchS.getMzDetail(params);
        LogUtil.d(TAG, mzDetail.toString());
        return mzDetail;
    }

    public VodDetailObj getVodDetail(Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }

        params.put(ConstantKey.ROAD_TYPE_SEARCHTYPE, ConstValue.SearchType.vods.name());//搜索类型
        params.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, ConstantValue.SHOWFIELDS_VOD_DETAIL);//搜索类型
        Map<String, String> mzDetail = this.getMzDetail(params);
        VodDetailObj vodDetailObj = null;

        if (mzDetail != null) {
            vodDetailObj = new VodDetailObj();
            vodDetailObj.parserMap(mzDetail);
        }

        return vodDetailObj;
    }

    /**
     * 取新闻详细信息
     *
     * @param newsId
     * @return
     * @throws Exception
     */
    public NewsInfoObj getNewsDetail(String newsId) throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("id", newsId);
        String userId = Account.getInstance().userId;
        if (!TextUtils.isEmpty(userId)) {
            params.put("memberId", userId);
        }
        params.put("searchType", ConstValue.SearchType.news.name());
        params.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, ConstantValue.SHOWFIELDS_NEWS);
        Map<String, String> map = this.getMzDetail(params);
        LogUtil.d(TAG, map.toString());

        NewsInfoObj ret = new NewsInfoObj();
        ret.parserMap(map);

        LogUtil.d(TAG, "getNewsDetail - "+ ret.toString());

        return ret;
    }

    /**
     * @param params
     * @return List<Map<String,String>>
     * @Description: 获取推荐节目
     * @author wangdx
     * @date 2015-4-26上午11:25:54
     */
    public List<Map<String, String>> getRecomMz(Map<String, String> params) {
        return commS.getRecomMz(params);
    }

    /**
     * 获取相关新闻
     *
     * @param params
     * @return
     */
    public List<Map<String, String>> getSimilarInfo(Map<String, String> params) {
        return searchS.getSimilarInfo(params);
    }

    /**
     * 媒资收藏
     *
     * @param mzId   媒资ID
     * @param userId 用户ID
     * @return 1 成功
     */
    public int actionOperateCollectionAdd(String mzId, String userId) {
        Map<String, String> params = new HashMap<>();
        params.put(ConstValue.CommonParams.commActionType.name(), ConstValue.CommonActionType.addCollect.name());


        params.put(ConstantKey.ROAD_TYPE_MZID, mzId);
        params.put(ConstantKey.ROAD_TYPE_USERID, userId);

        return actionOperate(params);
    }

    /**
     * 媒资收藏删除
     *
     * @param mzId   媒资ID
     * @param userId 用户ID
     * @return 1 成功
     */
    public int actionOperateCollectionDel(String mzId, String userId) {
        Map<String, String> params = new HashMap<>();
        params.put(ConstValue.CommonParams.commActionType.name(), ConstValue.CommonActionType.delCollect.name());
        params.put(ConstantKey.ROAD_TYPE_USERID, userId);
        params.put(ConstantKey.ROAD_TYPE_MZID, mzId);
        return actionOperate(params);
    }


    /**
     * 用户的注册、修改、登录、评论（增删）、点赞（增删）、收藏（增删）
     *
     * @param params params
     * @return
     */
    public int actionOperate(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }

        // 站点id
        if (!params.containsKey(ConstantKey.ROAD_TYPE_SITEID)) {
            params.put(ConstantKey.ROAD_TYPE_SITEID, ConstantValue.SITE_ID);
        }

        String userId = Account.getInstance().userId;
        if (!TextUtils.isEmpty(userId)) {
            params.put(ConstantKey.ROAD_TYPE_USERID, userId);
        }

        return commS.actionOperate(params);
    }

    /**
     * @param params
     * @return void
     * @Description: 登录使用
     * @author hezb
     * @date 2015年4月28日上午11:41:41
     */
    public String findUname(Map<String, String> params) {
        return commS.findUserOrLogin(params);
    }

    /**
     * 升级
     */
    public Map<String, String> getLastVersion() {
        Map<String, String> params = new HashMap<>();
        // 站点编码 key-value
        // 站点id
        params.put(ConstantKey.ROAD_TYPE_SITEID, ConstantValue.SITE_ID);
        // 接口编码
        params.put(ConstValue.CommonParams.commActionType.name(), "getSoftwareUpgrade");
        // 包名
        params.put(ConstantKey.UPGRADE_APPPACKAGE, MyApp.getAppContext().getPackageName());

        return commS.getLastVersion(params);
    }
}
