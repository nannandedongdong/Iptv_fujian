package com.ccdt.ottclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.ccdt.ottclient.model.ExitMsg;
import com.ccdt.ottclient.utils.LogUtil;

import java.util.List;
import java.util.Stack;

import de.greenrobot.event.EventBus;


public abstract class BaseActivity extends FragmentActivity {
    protected Activity mActivity;
    public static final String TAG = BaseActivity.class.getName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//

        LogUtil.i(TAG, "onCreate  0");
        int contentViewId = getContentViewId();
        if (contentViewId > 0) {
            setContentView(contentViewId);
        }
        LogUtil.i(TAG, "onCreate  1");
        mActivity = this;
        if(contentViewId > 0){
            initWidget();
        }
        EventBus.getDefault().register(this);
        ScreenManager.getScreenManager().pushActivity(this);
    }

    public void onEventMainThread(ExitMsg event) {
        this.finish();
    }

    private void startActivity(Class c) {
        Intent intent = new Intent(mActivity, c);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ScreenManager.getScreenManager().popActivity(this);
    }

    /**
     * 返回layout的ID，用于setContentView
     * @return
     */
    protected abstract int getContentViewId();

    /**
     * 加载控件
     */
    protected abstract void initWidget();






}
