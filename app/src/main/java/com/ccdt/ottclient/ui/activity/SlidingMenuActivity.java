package com.ccdt.ottclient.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ccdt.ottclient.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityHelper;


public abstract class SlidingMenuActivity extends FragmentActivity implements SlidingActivityBase {
    private static final String TAG = "SlidingMenuActivity";

    private static final int SLIDING_MENU_DEFAULT_MENU_LAYOUT = R.layout.default_menu_frame;
    private static final int SLIDING_MENU_DEFAULT_MENU_FRAME = R.id.default_menu_frame;

    private SlidingActivityHelper mHelper;
    protected SlidingMenu mSlidingMenu;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//
        getWindow().getDecorView().setBackgroundResource(R.drawable.bg);
        mHelper = new SlidingActivityHelper(this);
        mHelper.onCreate(savedInstanceState);

        // 设置主界面视图
        setContentView(getContentLayoutID());
//        getSupportFragmentManager().beginTransaction().replace(contentId, getContentFragment()).commit();

        // 设置滑动菜单视图
        setBehindContentView(SLIDING_MENU_DEFAULT_MENU_LAYOUT);
        getSupportFragmentManager().beginTransaction().replace(SLIDING_MENU_DEFAULT_MENU_FRAME, getMenuFragment()).commit();

        mSlidingMenu = getSlidingMenu();
        //         设置滑动菜单的属性值
//        SlidingMenu sm = getSlidingMenu();
//        sm.setShadowWidthRes(R.dimen.shadow_width);
//        sm.setShadowDrawable(R.drawable.shadow);
//        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);

        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setBehindWidth(200);
        mSlidingMenu.setFadeEnabled(false);
        mSlidingMenu.setBehindScrollScale(0.25f);
        mSlidingMenu.setFadeDegree(0.25f);
        setSlidingActionBarEnabled(true);


        initView();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate(savedInstanceState);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#findViewById(int)
     */
    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v != null)
            return v;
        return mHelper.findViewById(id);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHelper.onSaveInstanceState(outState);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(int)
     */
    @Override
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View)
     */
    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        super.setContentView(v, params);
        mHelper.registerAboveContentView(v, params);
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(int)
     */
    public void setBehindContentView(int id) {
        setBehindContentView(getLayoutInflater().inflate(id, null));
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View)
     */
    public void setBehindContentView(View v) {
        setBehindContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    public void setBehindContentView(View v, ViewGroup.LayoutParams params) {
        mHelper.setBehindContentView(v, params);
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#getSlidingMenu()
     */
    public SlidingMenu getSlidingMenu() {
        return mHelper.getSlidingMenu();
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#toggle()
     */
    public void toggle() {
        mHelper.toggle();
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showAbove()
     */
    public void showContent() {
        mHelper.showContent();
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showBehind()
     */
    public void showMenu() {
        mHelper.showMenu();
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showSecondaryMenu()
     */
    public void showSecondaryMenu() {
        mHelper.showSecondaryMenu();
    }

    /* (non-Javadoc)
     * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setSlidingActionBarEnabled(boolean)
     */
    public void setSlidingActionBarEnabled(boolean b) {
        mHelper.setSlidingActionBarEnabled(b);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean b = mHelper.onKeyUp(keyCode, event);
        if (b) return b;
        return super.onKeyUp(keyCode, event);
    }

    protected abstract int getContentLayoutID();

    protected abstract Fragment getMenuFragment();

    protected abstract void initView();


}
