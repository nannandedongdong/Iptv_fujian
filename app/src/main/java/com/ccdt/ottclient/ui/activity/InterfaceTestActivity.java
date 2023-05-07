package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ccdt.ott.search.thrift.CommonSearchResult;
import com.ccdt.ott.util.ConstValue;
import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.api.OTTServiceApi;
import com.ccdt.ottclient.config.Config;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.AuthTerminalDataObj;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.model.InvokeResult;
import com.ccdt.ottclient.model.MonthOrderDetailObj;
import com.ccdt.ottclient.model.MonthOrderObj;
import com.ccdt.ottclient.model.NumOrderObj;
import com.ccdt.ottclient.model.OtherProductObj;
import com.ccdt.ottclient.exception.DefaultException;
import com.ccdt.ottclient.exception.NetWorkUnavailableException;
import com.ccdt.ottclient.exception.NoResponseException;
import com.ccdt.ottclient.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceTestActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = InterfaceTestActivity.class.getSimpleName();

    public View btn_getSearchResult;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, InterfaceTestActivity.class);
        context.startActivity(intent);

    }


    private View btnGetMyNumOrder;
    private View btnGetOtherProducts;
    private View btnGetMyMonthOrder;
    private View btnGetMyMonthOrderDetail;
    private View btnGouMaiJiLu;
    private TextView txtWidth;
    private TextView txtHeight;
    private TextView txtScale;
    private TextView txtDip;
    private TextView txtVersionSdk;
    public View btn_getSubLm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnGetMyNumOrder.setOnClickListener(this);
        btnGetOtherProducts.setOnClickListener(this);
        btnGetMyMonthOrder.setOnClickListener(this);
        btnGetMyMonthOrderDetail.setOnClickListener(this);
        btnGouMaiJiLu.setOnClickListener(this);
        btn_getSubLm.setOnClickListener(this);
        btn_getSearchResult.setOnClickListener(this);
        initToken();



        WindowManager manager = this.getWindowManager();
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        float scale = getResources().getDisplayMetrics().density;

        txtWidth.setText("width: " + width);
        txtHeight.setText("height: " + height);
        txtScale.setText("scale: " + scale);

        // px = dp * (dpi / 160)
        // 获取屏幕密度（方法3）
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidthDip = dm.widthPixels;
        int screenHeightDip = dm.heightPixels;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        txtDip.setText("xdpi: " + xdpi + "  ;ydpi :" + ydpi);

        showVersionSdk();

    }

    private void showVersionSdk() {
        txtVersionSdk.setText("SDK_INT:" + Build.VERSION.SDK_INT +
                        "\nCODENAME:" + Build.VERSION.CODENAME +
                        "\nSDK:" + Build.VERSION.SDK
        );
    }

    private void initToken() {
        new MyAsyncTask().execute(-999);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnGouMaiJiLu:
                Intent intent = new Intent(this, PurchaseHistoryActivity.class);
                startActivity(intent);
                break;
            default:
                new MyAsyncTask().execute(id);
                break;
        }


    }

    /**
     * 获取设备订购的按次消费资产信息
     */
    private void getMyMonthOrder() {
        try {
            InvokeResult<MonthOrderObj> myMonthOrder = MyApp.getOTTApi().getMyMonthOrder(10, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_interface_test;
    }

    @Override
    public void initWidget() {
        btnGetMyNumOrder = findViewById(R.id.btnGetMyNumOrder);
        btnGetOtherProducts = findViewById(R.id.btnGetOtherProducts);
        btnGetMyMonthOrder = findViewById(R.id.btnGetMyMonthOrder);
        btnGetMyMonthOrderDetail = findViewById(R.id.btnGetMyMonthOrderDetail);
        btnGouMaiJiLu = findViewById(R.id.btnGouMaiJiLu);
        txtWidth = (TextView) findViewById(R.id.txtWidth);
        txtHeight = ((TextView) findViewById(R.id.txtHeight));
        txtScale = ((TextView) findViewById(R.id.txtScale));
        txtDip = ((TextView) findViewById(R.id.txtDip));
        txtVersionSdk = ((TextView) findViewById(R.id.txtVersionSdk));
        btn_getSubLm = findViewById(R.id.btn_getSubLm);
        btn_getSearchResult = findViewById(R.id.btn_getSearchResult);
    }

    private class MyAsyncTask extends AsyncTask<Integer, Void, Object> {

        @Override
        protected Object doInBackground(Integer... params) {
            try {
                switch (params[0]) {
                    case R.id.btnGetOtherProducts:
                        // 获取我尚未订购的包月产品
                        InvokeResult<OtherProductObj> res = MyApp.getOTTApi().getOtherProducts(0, 0);
                        LogUtil.d("---btnGetOtherProducts--", res.toString());
                        break;
                    case R.id.btnGetMyMonthOrder:
                        // 获取我订购的包月产品
                        InvokeResult<MonthOrderObj> invokeResult = MyApp.getOTTApi().getMyMonthOrder(0, 0);
                        LogUtil.d("---btnGetMyMonthOrder--", invokeResult.toString());
                        break;
                    case R.id.btnGetMyNumOrder:
                        // 获取设备订购的按次消费资产信息
                        InvokeResult<NumOrderObj> invokeResult2 = MyApp.getOTTApi().getMyNumOrder(null, 0, 0, null, null);
                        List<NumOrderObj> numOrderObjList = invokeResult2.getdList();
                        break;
                    case R.id.btnGetMyMonthOrderDetail:
                        // 获取某一包月产品的消费记录详情
                        InvokeResult<MonthOrderDetailObj> myMonthOrderDetail = MyApp.getOTTApi().getMyMonthOrderDetail("MOVIEd3272b5ddf834b1b83efa687cc89b140");
                    case R.id.btn_getSubLm:
                        List<LmInfoObj> subLm = OTTServiceApi.getInstance().getSubLm("0");
                        LogUtil.d(TAG, subLm.toString());

                        break;
                    case R.id.btn_getSearchResult:
//                        OTTServiceApi.getInstance().get
                        /*
                        // 最新上線
        Request newMoviesRequest = new Request(ITvRequestFactory.REQUEST_TYPE_GET_SEARCH);
        newMoviesRequest.put("chineselineflag", ZXSX);
        newMoviesRequest.put(ConstantKey.ROAD_TYPE_LMID, ConstantValue.VOD_ZXSX);
        newMoviesRequest.put(ConstantKey.ROAD_TYPE_SITEID, ConstantValue.SITE_ID);
        newMoviesRequest.put(ConstantKey.ROAD_TYPE_SEARCHTYPE, ConstValue.SearchType.vods.name());
        newMoviesRequest.put(ConstantKey.ROAD_TYPE_PAGESIZE, 7 + "");
        newMoviesRequest.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);//海报类型
        newMoviesRequest.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);//设备类型
        requests.add(newMoviesRequest);
                         */
                        Map<String, String> params_getSearchResult = new HashMap<>();
                        params_getSearchResult.put(ConstantKey.ROAD_TYPE_LMID, "qhysdy");
                        params_getSearchResult.put(ConstantKey.ROAD_TYPE_SITEID, Config.SITE_ID);
                        params_getSearchResult.put(ConstantKey.ROAD_TYPE_SEARCHTYPE, ConstValue.SearchType.vods.name());
                        params_getSearchResult.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);//海报类型
                        params_getSearchResult.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);//设备类型
                        CommonSearchResult searchResult = OTTServiceApi.getInstance().getSearchResult(params_getSearchResult);
                        List<Map<String, String>> rtList = searchResult.getRtList();
                        break;
                    default:
                        AuthTerminalDataObj authTerminalDataObj = MyApp.getOTTApi().authTerminal(Build.SERIAL, null, null);

                        if (authTerminalDataObj != null) {
                            Account.getInstance().token = authTerminalDataObj.getToken();
                        }

                        LogUtil.i("token--------", "token=" + Account.getInstance().token);
                        break;
                }
            } catch (Exception e) {
                if (e instanceof DefaultException) {
                    LogUtil.d("DefaultException-----", "DefaultException");
                } else if (e instanceof NoResponseException) {
                    LogUtil.d("NoResponseException-----", "NoResponseException");
                } else if (e instanceof NetWorkUnavailableException) {
                    LogUtil.d("NetWorkUnavailableException-----", "NetWorkUnavailableException");
                } else {
                    LogUtil.d("Exception-----", "Exception");
                }
                e.printStackTrace();
            }


            return null;
        }


    }


}
