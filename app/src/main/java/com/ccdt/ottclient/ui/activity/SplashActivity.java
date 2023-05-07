package com.ccdt.ottclient.ui.activity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.Config;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.AccountInfo;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.AuthTerminalTask;
import com.ccdt.ottclient.tasks.impl.GetAccountInfoTask;
import com.ccdt.ottclient.tasks.impl.GetSubLmTask;
import com.ccdt.ottclient.utils.AppUtil;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.SPUtils;
import com.ccdt.ottclient.utils.ToastUtil;

import java.util.List;

public class SplashActivity extends BaseActivity implements TaskCallback {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private ProgressBar progressBar;

//    private static final int TASK_COUNT = 3;
    private int taskCount;
    private TextView txtVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        progressBar = ((ProgressBar) this.findViewById(R.id.progressBar));

        txtVersionName = ((TextView) findViewById(R.id.id_txt_versionName));
        txtVersionName.setText(AppUtil.getVersionName());
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 加载配置
        initConfig();
        //鉴权
        authTerminal();

    }

    private void getAccountInfo() {
        new GetAccountInfoTask(this).execute();
    }

    /////////////////////////////////

    /**
     * 取根一级栏目列表
     */
    private void initOneLevelColumnList() {
        GetSubLmTask.executeTask(this, "0");
    }

    /////////////////////////////////////////////

    /**
     * 加载配置
     */
    private void initConfig() {
//        Config.HOST = "www.sitvs.com:15414";
//        Config.SITE_ID = ConstantValue.SITE_ID;
//        Config.APP_SHOP_URL = "http://www.sitvs.com/ChaMGD/yyzx.html";

        Config.HOST = (String) SPUtils.get(this, "AUTH_IP", "www.sitvs.com:15414");
        Config.SITE_ID = ((String) SPUtils.get(this, "SITE_ID", "ott"));
        Config.APP_SHOP_URL = ((String) SPUtils.get(this, "APP_SHOP_URL", "http://www.sitvs.com/ChaMGD/yyzx.html"));
        Config.OttCommActionService_IP = ((String) SPUtils.get(this, "COMM_SERVER_URL", "www.sitvs.com"));
        Config.OttCommActionService_PORT = ((int) SPUtils.get(this, "COMM_SERVER_PORTAL", 15415));
        Config.OttSearchService_IP = ((String) SPUtils.get(this, "COMM_SERVER_SEARCH_URL", "www.sitvs.com"));
        Config.OttSearchService_PORT = ((int) SPUtils.get(this, "COMM_SERVER_SEARCH_PORTAL", 15413));
    }

    /////////////////////////////////////////////////

    /**
     * 鉴权
     */
    private void authTerminal() {
        new AuthTerminalTask(this).execute();
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_AUTHTERMINAL:
                    if (result.code == 200) {
                        ToastUtil.ToastMessage(this,"认证成功");
                        // 获取用户信息
                        getAccountInfo();
                        // 取根一级栏目列表
                        initOneLevelColumnList();
                    } else {
                        ToastUtil.ToastMessage(this, "认证失败，" + result.message);
                        taskCount = 2;
                        // 鉴权失败 跳转到主页
                        turnToMainActivity();
                    }
//                    turnToMainActivity();
                    break;
                case Constants.TASK_GETSUBLM:
                    taskCount++;
                    if (result.code == 200) {
                        LogUtil.d(TAG, "加载一级栏目列表成功");
                        LogUtil.d(TAG, result.data.toString());
                        if (result.data != null) {
                            MyApp.getInstance().setOneLevelLmList((List<LmInfoObj>) result.data);
                        }
                    } else {
                        LogUtil.d(TAG, "加载一级栏目列表失败 error:" + result.message);
                    }
                    turnToMainActivity();
                    break;
                case Constants.TASK_GETACCOUNTINFO:
                    taskCount++;
                    if (result.code == 200) {
                        LogUtil.d(TAG, "加载用户信息成功");
                        LogUtil.d(TAG, result.data.toString());
                        if (result.data != null) {
                            Account.getInstance().info = (AccountInfo) result.data;
                        }

                    } else {
                        LogUtil.d(TAG, "加载用户信息失败 error:" + result.message);
                    }
                    turnToMainActivity();
                    break;
                default:
                    break;
            }
        }


    }

    ////////////////////////////////////////////////
    @Override
    public void initWidget() {

    }

    private void turnToMainActivity() {

        if(taskCount == 2){
            MainActivity.actionStart(this);
            finish();
        }

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }
}
