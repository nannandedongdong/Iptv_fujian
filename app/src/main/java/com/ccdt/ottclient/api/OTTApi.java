package com.ccdt.ottclient.api;

import android.text.TextUtils;

import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.config.Config;
import com.ccdt.ottclient.model.AccountInfo;
import com.ccdt.ottclient.model.AuthTerminalDataObj;
import com.ccdt.ottclient.model.InvokeResult;
import com.ccdt.ottclient.model.MessageObj;
import com.ccdt.ottclient.model.MonthOrderDetailObj;
import com.ccdt.ottclient.model.MonthOrderObj;
import com.ccdt.ottclient.model.OtherProductObj;
import com.ccdt.ottclient.model.NumOrderObj;

import org.json.JSONObject;


public final class OTTApi {
    private final String TAG = getClass().getSimpleName();

    /**
     * 机顶盒认证接口
     */
    public AuthTerminalDataObj authTerminal(String sn, String mac, String terminalType) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_AUTHTERMINAL));
        url.append("&sn=" + sn);
        if (mac != null && !mac.isEmpty()) {
            url.append("&mac=" + mac);
        }
        if (terminalType != null && !terminalType.isEmpty()) {
            url.append("&terminalType=" + terminalType);
        }

        String response = MyApp.getApi().get(url.toString());
        AuthTerminalDataObj authTerminalDataObj = new AuthTerminalDataObj();

        JSONObject jsonObject = new JSONObject(response);
        JSONObject jsonData = jsonObject.optJSONObject("data");
        authTerminalDataObj.parserJSON(jsonData);

        return authTerminalDataObj;
    }


    /**
     * VOD鉴权接口
     * 供终终端设备调用，用于验证终端设备是否有权限点播/直播视频源或访问第三方业务。
     * http://ip:port/auth/product/authorization?ticket =xxxx&type=vod&assetId=xxxx
     *
     * @param type    鉴权的业务类型，必选参数。Vod点播/直播鉴权，type=vod
     * @param assetId assetId：终端访问的业务编码或vod编码，必须参数。。Type=vod时，assetId为视频编码；
     * @return String
     */
    public String authorization(String type, String assetId) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_AUTHORIZATION));
        url.append("&assetId=" + assetId);
        url.append("&type=" + type);

        return MyApp.getApi().get(url.toString());
    }


    /**
     * 订购接口
     * 提供产品订购功能
     * http://ip:port/auth/product/productOrder?ticket=xxxx&productId=xxxx&assetId=xxxx&assetName=xxxx
     *
     * @param productId 鉴权的vod/业务所属的产品编码，必选参数
     * @param assetId   终端访问的业务编码或vod编码，必须参数。
     * @param assetName 终端访问的业务名称或vod名称，可选参数
     * @return
     * @throws Exception
     */
    public String productOrder(String productId, String assetId, String assetName) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_PRODUCTORDER));
        url.append("&productId=" + productId);
        url.append("&assetId=" + assetId);
        if (assetName != null && !assetName.isEmpty()) {
            url.append("&assetName=" + assetName);
        }
        return MyApp.getApi().get(url.toString());
    }

    /**
     * 包月产品退订
     * 退订包月产品，以便下个月不再自动续订
     * http://ip:port/auth/product/unsubscribe?ticket=xxxx&productId=xxxx
     *
     * @param productId 鉴权的vod/业务所属的产品编码，必选参数
     * @return
     * @throws Exception
     */
    public String unsubscribe(String productId) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_UNSUBSCRIBE));
        url.append("&productId=" + productId);
        return MyApp.getApi().get(url.toString());
    }

    /**
     * 包月产品续订，以便下个月自动续订
     * http://ip:port/auth/product/subscribe?ticket=xxxx&productId=xxxx
     *
     * @param productId 鉴权的vod/业务所属的产品编码，必选参数
     * @return
     * @throws Exception
     */
    public String subscribe(String productId) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_SUBSCRIBE));
        url.append("&productId=" + productId);
        return MyApp.getApi().get(url.toString());
    }

    /**
     * 获取设备订购的可用包月产品
     * http://ip:port/auth/userinfo/getMyMonthOrder?ticket=xxxx&pageSize=xxxx&pageNumber=xxxx
     *
     * @param pageSize   每页加载的最大数量，可选参数。默认为5
     * @param pageNumber 加载的页码，可选参数。默认为1.取值范围为1,2,3,4,5,6...
     * @return
     * @throws Exception
     */
    public InvokeResult<MonthOrderObj> getMyMonthOrder(int pageSize, int pageNumber) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_GETMYMONTHORDER));

        if (pageSize > 0) {
            url.append("&pageSize=" + pageSize);
        }

        if (pageNumber > 0) {
            url.append("&pageNumber=" + pageNumber);
        }

        String response = MyApp.getApi().get(url.toString());
        JSONObject jsonObject = new JSONObject(response);
        InvokeResult<MonthOrderObj> invokeResult = new InvokeResult<>(MonthOrderObj.class);
        invokeResult.parserJSON(jsonObject);

        return invokeResult;
    }

    /**
     * 获取某一包月产品的消费记录详情
     * http://ip:port/userinfo/getMyMonthOrderDetail?ticket=xxxx&productId=xxxx
     *
     * @param productId 包月产品的产品编码，必须属性
     * @return InvokeResult
     * @throws Exception
     */
    public InvokeResult<MonthOrderDetailObj> getMyMonthOrderDetail(String productId) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_GETMYMONTHORDERDETAIL));
        url.append("&productId=" + productId);
        String response = MyApp.getApi().get(url.toString());
        JSONObject jsonObject = new JSONObject(response);
        InvokeResult<MonthOrderDetailObj> invokeResult = new InvokeResult<>(MonthOrderDetailObj.class);
        invokeResult.parserJSON(jsonObject);
        return invokeResult;
    }


    /**
     * 获取设备订购的按次消费资产信息
     * http://ip:port/auth/userinfo/getMyNumOrder?ticket=xxxx&pageSize=xxxx&pageNumber=xxxx&state=XXXX&terminalType=XXXX&posterType=XXXX
     *
     * @param state        订单状态。1-为使用中；0为已使用。也可不设置该参数，查询所有状态。
     * @param pageSize     每页加载的最大数量，可选参数。默认为5
     * @param pageNumber   加载的页码，可选参数。默认为1.取值范围为1,2,3,4,5,6….
     * @param terminalType 设备类型。base-机顶盒；terminal-手机。也可不设置，获取所有海报
     * @param posterType   海报类型。0-横版；1-竖版。也可不设置，获取所有海报
     * @return GetMyNumOrderRes
     * @throws Exception
     */
    public InvokeResult<NumOrderObj> getMyNumOrder(
            String state,
            int pageSize,
            int pageNumber,
            String terminalType,
            String posterType) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_GETMYNUMORDER));
        if (!TextUtils.isEmpty(state)) {
            url.append("&state=" + state);
        }

        if (!TextUtils.isEmpty(terminalType)) {
            url.append("&terminalType=" + terminalType);
        }

        if (!TextUtils.isEmpty(posterType)) {
            url.append("&posterType=" + posterType);
        }

        if (pageSize > 0) {
            url.append("&pageSize=" + pageSize);
        }

        if (pageNumber > 0) {
            url.append("&pageNumber=" + pageNumber);
        }

        String response = MyApp.getApi().get(url.toString());
        JSONObject jsonObject = new JSONObject(response);
        InvokeResult<NumOrderObj> invokeResult = new InvokeResult<>(NumOrderObj.class);
        invokeResult.parserJSON(jsonObject);
        return invokeResult;
    }

    /**
     * 获取我尚未订购的包月产品
     *
     * @param pageSize   每页加载的最大数量，可选参数。默认为5
     * @param pageNumber 加载的页码，可选参数。默认为1.取值范围为1,2,3,4,5,6...
     * @return InvokeResult
     * @throws Exception
     */
    public InvokeResult<OtherProductObj> getOtherProducts(
            int pageSize,
            int pageNumber) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_GETOTHERPRODUCTS));

        if (pageSize > 0) {
            url.append("&pageSize=" + pageSize);
        }

        if (pageNumber > 0) {
            url.append("&pageNumber=" + pageNumber);
        }

        String response = MyApp.getApi().get(url.toString());
        JSONObject jsonObject = new JSONObject(response);

        InvokeResult<OtherProductObj> invokeResult = new InvokeResult<>(OtherProductObj.class);
        invokeResult.parserJSON(jsonObject);
        return invokeResult;
    }

    /**
     * 获取用户信息
     *
     * @throws Exception
     */
    public AccountInfo getAccountInfo() throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_GETACCOUNT));
//        url.append( Account.getInstance().token);
        String response = MyApp.getApi().get(url.toString());
        JSONObject jsonObject = new JSONObject(response);

        JSONObject json = jsonObject.optJSONObject("data");

        AccountInfo info = new AccountInfo();
        info.parserJSON(json);
        Account.getInstance().info = info;
        return info;
    }


    /**
     * 支付接口
     */
    public MessageObj payProduct(String orderId) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(MyApp.getApi().url(Config.PATH_PAYPRODUCT));
        url.append("&id=" + orderId);
        String response = MyApp.getApi().get(url.toString());
        JSONObject jsonObject = new JSONObject(response);
        MessageObj msg = new MessageObj();
        msg.parserJSON(jsonObject);
        return msg;
    }


}
