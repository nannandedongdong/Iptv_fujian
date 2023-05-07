package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.ccdt.ottclient.MyApp;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.ui.adapter.MainPageAdapter;
import com.ccdt.ottclient.ui.fragment.BaseIndicatorFragment;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.ToastUtil;


import java.util.List;

import upgrade.APPGetUpgradeInfo;
import viewpagerindicator.TabPageIndicator;

public class MainActivity extends BaseActivity implements TabPageIndicator.OnTabFocusChangeListener, View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ViewPager vp;
    private TabPageIndicator indicator;
    private MainPageAdapter adapter;
    private View btnSearch;
    private View view_hide;
    private int[] idArr = {
            R.id.main_tab_0,
            R.id.main_tab_1,
            R.id.main_tab_2,
            R.id.main_tab_3,
            R.id.main_tab_4,
            R.id.main_tab_5
    };
    public List<LmInfoObj> oneLevelColumnList;
    // ViewPager 当前位置
    private int mCurrentPagePosition;

    private int mKeyCode;
    private boolean isIndicatorHasFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oneLevelColumnList = MyApp.getInstance().getOneLevelLmList();

        adapter = new MainPageAdapter(getSupportFragmentManager(), oneLevelColumnList, idArr);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(adapter.getCount());
        indicator.setViewPager(vp, 0);
//        indicator.setViewPager(vp, 0);
        indicator.setOnTabFocusChangeListener(this);
        indicator.setOnPageChangeListener(this);

        vp.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((IIndicator) adapter.getItem(0)).onGetFocus();
            }
        }, 2000);
    }

    private Handler mHandler = new Handler();

    public void onEvent(IndicatorEvent event) {
        ToastUtil.toast("---onEvent---");

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        vp = (ViewPager) findViewById(R.id.vp_main);
        indicator = ((TabPageIndicator) findViewById(R.id.main_indicator));
        btnSearch = findViewById(R.id.activity_main_search);
//        view_hide = findViewById(R.id.view_hide);
        btnSearch.setOnClickListener(this);

        updateApp();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.activity_main_search: // 全局搜索按钮
                SearchActivity.actionStart(this);
                break;
            default:
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.e("slf", " 主界面  当前一级栏目：position=" + position);
        this.mCurrentPagePosition = position;

        if (!indicator.hasFocusIndicator()) {
            ((IIndicator) adapter.getItem(position)).onGetFocus();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mKeyCode = keyCode;
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && isIndicatorHasFocus) {
            onItemGetFocus();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onItemGetFocus() {
        BaseIndicatorFragment fragment = (BaseIndicatorFragment) adapter.getItem(mCurrentPagePosition);
        fragment.onGetFocus();
    }

    @Override
    public void onFocusChangeListener(TabPageIndicator.TabView tab, boolean hasFocus) {
        isIndicatorHasFocus = hasFocus;
        // 方向键向下 && 失去焦点
//        if(mKeyCode == KeyEvent.KEYCODE_DPAD_DOWN && !hasFocus){
//            onItemGetFocus();
//        }
    }


    private long exitTime = 0;

    public void ExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.ToastMessage(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void updateApp(){
        Handler handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 999:
                        APPGetUpgradeInfo appGetUpgradeInfo
                                = new APPGetUpgradeInfo(MainActivity.this, true);
                        appGetUpgradeInfo.run();
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        handler.sendEmptyMessageDelayed(999, 6000);
    }
}

