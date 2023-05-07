package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.model.VodDetailObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetVodListTask;
import com.ccdt.ottclient.ui.adapter.MovieAdapter;
import com.ccdt.ottclient.ui.fragment.MenuFragment;
import com.ccdt.ottclient.ui.fragment.MovieFilterFragment;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.Utility;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.viva.tv.app.widget.FocusedBasePositionManager;
import io.viva.tv.app.widget.FocusedGridView;


public class MovieListActivity extends SlidingMenuActivity implements MenuFragment.OnMenuItemClickListener, TaskCallback, SlidingMenu.OnCloseListener, SlidingMenu.OnOpenListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private MenuFragment fragment;
    private List<VodDetailObj> mDataSet = new ArrayList<>();
    private MovieAdapter adapter;
    private TextView txtTvTags, tvFilter;
    private LmInfoObj lminfo;
    private FocusedGridView mGrid;
    private TextView tvTitle;
    private View ivSwitch;
    private ArrayList<Map<String, String>> list;
    private Handler myHandler = new Handler();
    private MovieFilterFragment movieFilterFragment;
    private String lmid;
    private View mSelectedMenuView;
    private boolean isFocusOnMenu;
    private int pagenum = 1;
    private Handler mHander = new Handler();
    private Runnable foused = new Runnable() {
        @Override
        public void run() {
            fouseList();
        }
    };
    private int colorTextUnselected;
    private MovieAdapter.ViewHolder focusedHolder;
    private boolean isNeedClear;
    private StringBuffer mTag;
    private LinearLayout ll_tags_space;
    private boolean firstLoad = false;


    public static void actionStart(Context context, LmInfoObj lmInfo) {
        Intent intent = new Intent(context, MovieListActivity.class);
        intent.putExtra("lmInfo", lmInfo);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstLoad = true;
        Intent intent = getIntent();
        colorTextUnselected = this.getResources().getColor(R.color.text_unselected_vod);
        if (intent != null) {
            LmInfoObj lmInfo = intent.getParcelableExtra("lmInfo");
            lmid = lmInfo.getLmId();
            fragment.setLmInfo(lmInfo);
            fragment.setOnMenuItemClickListener(this);
//            Map<String, String> params = new HashMap<>();
//            params.put(ConstantKey.ROAD_TYPE_LMID, lmid);
//            params.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);
//            params.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);
//            params.put(ConstantKey.ROAD_TYPE_PAGESIZE, 1000 + "");
            tvTitle.setText(lmInfo.getLmName());
//            tvTitle.setText(lminfo.getLmName());
        }
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_movie_list;
    }

    @Override
    protected Fragment getMenuFragment() {
        fragment = new MenuFragment();
        return fragment;

    }

    @Override
    protected void initView() {
        initGrid();


//        view = findViewById(R.id.view_nextpage);
        tvTitle = ((TextView) findViewById(R.id.txt_title));
        ivSwitch = findViewById(R.id.txt_arrow_left);
//        frameLayout = (FrameLayout) findViewById(R.id.frame_content);
        ll_tags_space = (LinearLayout) findViewById(R.id.ll_tags_space);
        txtTvTags = ((TextView) findViewById(R.id.tv_tags));
        tvFilter = (TextView) findViewById(R.id.tv_filter_tag);

        list = new ArrayList<>();

        movieFilterFragment = new MovieFilterFragment();

        mSlidingMenu.setOnOpenListener(this);
        mSlidingMenu.setOnCloseListener(this);
    }

    private void initGrid() {
        mGrid = ((FocusedGridView) findViewById(R.id.grid));
        mGrid.setAutoChangeLine(false);
        mGrid.setItemScaleValue(1.05f, 1.05f);
        mGrid.setFocusResId(R.drawable.bg_transparent);
        mGrid.setFocusShadowResId(R.drawable.bg_transparent);
        mGrid.setFocusViewId(R.id.layout_root);

        mGrid.setOnItemClickListener(this);

        mGrid.setOnItemSelectedListener(new FocusedBasePositionManager.FocusItemSelectedListener() {
            @Override
            public void onItemSelected(View paramView1, int paramInt, boolean paramBoolean, View paramView2) {
                if (paramBoolean) {
                    if (paramView1 != null) {
                        Object tag = paramView1.getTag();
                        if (tag != null) {
                            if (tag instanceof MovieAdapter.ViewHolder) {
                                focusedHolder = (MovieAdapter.ViewHolder) tag;
                                focusedHolder.txtTitle.setTextColor(Color.WHITE);
                                focusedHolder.layout_focus.setBackgroundResource(R.drawable.bg_vod_blue);
                            }
                        }
                    }
                } else {
                    if (focusedHolder != null) {
                        focusedHolder.txtTitle.setTextColor(colorTextUnselected);
                        focusedHolder.layout_focus.setBackgroundResource(R.drawable.bg_vod_black);
                    }
                    focusedHolder = null;
                }
            }
        });
        mGrid.setOnScrollListener(this);

        adapter = new MovieAdapter(this, mDataSet);
        mGrid.setAdapter(adapter);
    }

    @Override
    public void onMenuItemClickListener(View selectedMenuView, LmInfoObj lmInfo) {
        LogUtil.d("slf 菜单点击 -+-", lmInfo.getLmName());
        if(this.lminfo!=null && this.lminfo.getLmId()!=null && this.lminfo.getLmId().equals(lmInfo.getLmId())){
            return;
        }
        if (selectedMenuView != null) {
            this.mSelectedMenuView = selectedMenuView;
        }
        this.lminfo = lmInfo;
        pagenum = 1;
        mDataSet.clear();
        loadData();
    }

    private void loadData() {
        Log.d("slf " + TAG, "loadData()");
        Map<String, String> params = new HashMap<>();
        params.put(ConstantKey.ROAD_TYPE_LMID, lminfo.getLmId());
        params.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);
        params.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);
        if (pagenum > 0) {
            params.put(ConstantKey.ROAD_MY_PAGE_NUMBER, pagenum + "");
        }
        params.put(ConstantKey.ROAD_TYPE_PAGESIZE, 1000 + "");

        mTag = new StringBuffer();
        if (tagsMap != null) {
            if (tagsMap.get(ConstantKey.ROAD_TYPE_FCATEGORY) != null && !((String) tagsMap.get(ConstantKey.ROAD_TYPE_FCATEGORY)).equals("全部")) {
                params.put(ConstantKey.ROAD_TYPE_FCATEGORY, tagsMap.get(ConstantKey.ROAD_TYPE_FCATEGORY));
                mTag.append(tagsMap.get(ConstantKey.ROAD_TYPE_FCATEGORY) + "    ");
            }
            if (tagsMap.get(ConstantKey.ROAD_TYPE_ORIGIN) != null && !((String) tagsMap.get(ConstantKey.ROAD_TYPE_ORIGIN)).equals("全部")) {
                params.put(ConstantKey.ROAD_TYPE_ORIGIN, tagsMap.get(ConstantKey.ROAD_TYPE_ORIGIN));
                mTag.append(tagsMap.get(ConstantKey.ROAD_TYPE_ORIGIN) + "    ");
            }
            if (tagsMap.get(ConstantKey.ROAD_TYPE_YEAR) != null && !((String) tagsMap.get(ConstantKey.ROAD_TYPE_YEAR)).equals("全部")) {
                params.put(ConstantKey.ROAD_TYPE_YEAR, tagsMap.get(ConstantKey.ROAD_TYPE_YEAR));
                mTag.append(tagsMap.get(ConstantKey.ROAD_TYPE_YEAR) + "    ");
            }
        }
        LogUtil.d("slf", "筛选项: " + mTag.toString());
        tvFilter.setText(mTag.toString());
        tvFilter.requestLayout();

        ll_tags_space.invalidate();
        new GetVodListTask(this).execute(params);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.d("slf " + TAG, "按键-" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showContent();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            if (movieFilterFragment.isAdded()) {
                if (movieFilterFragment.isHidden()) {
                    transaction.show(movieFilterFragment).commit();
                    setFousable(false);
                } else {
                    transaction.hide(movieFilterFragment).commit();
                    setFousable(true);
                }

            } else {
                transaction.add(R.id.frame_content, movieFilterFragment).show(movieFilterFragment).commit();
                setFousable(false);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && isFocusOnMenu) {
            mSlidingMenu.showContent(true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (movieFilterFragment != null && movieFilterFragment.isAdded() && !movieFilterFragment.isHidden()) {
                LogUtil.d("slf " + TAG, "隐藏筛选菜单");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(movieFilterFragment);
                transaction.commit();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final String TAG = MovieListActivity.class.getName();

    private void setFousable(boolean fosuable) {
        int size = mGrid.getChildCount();
        for (int i = 0; i < size; i++) {
            View v = mGrid.getChildAt(i);
            v.setFocusable(fosuable);
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            if (result.code == TaskResult.SUCCESS) {
                if (isNeedClear) {
                    isNeedClear = false;
                    mDataSet.clear();
                }
                if (result.data != null) {
                    CommonSearchInvokeResult<VodDetailObj> invokeResult = (CommonSearchInvokeResult<VodDetailObj>) result.data;
                    List<VodDetailObj> rtList = invokeResult.getRtList();
                    this.totalNum = invokeResult.getTotalNum();
                    if (rtList != null && rtList.size() > 0) {
                        mDataSet.addAll(rtList);
                    }
//                    else {
//                        if (pagenum > 0) {
//                            pagenum--;
//                            ToastUtil.toast("没有更多的数据了");
//                        }
//                    }

                }
                txtTvTags.setText(String.valueOf(this.totalNum));
                adapter.notifyDataSetChanged();
                LogUtil.d("slf " + TAG, "notifyDataSetChanged");

                // 模拟按键  解决初始加载不出数据bug（原因暂时未知）
                String keyCommand = "input keyevent " + KeyEvent.KEYCODE_BUTTON_13;
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec(keyCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                if (movieFilterFragment == null && movieFilterFragment.isHidden()) {
//                    mHander.postDelayed(foused, 500);
//                }
            } else {

            }
        }
    }

    private Map<String, String> tagsMap;

    public void filterList(Map<String, String> tags) {
        this.tagsMap = tags;
        this.isNeedClear = true;
        this.pagenum = 1;
        Log.d("slf " + TAG, "filterList(***)");
        loadData();
    }

    @Override
    public void onClose() {
        isFocusOnMenu = false;
        ivSwitch.setVisibility(View.VISIBLE);
        mSlidingMenu.getMenu().setVisibility(View.INVISIBLE);
        // 关闭左侧菜单时，让GridView中第一个元素获取焦点
//        View child = mGrid.getChildAt(0);
//        if (child != null) {
//            child.requestFocus();
//        }
//        fouseList();


    }

    @Override
    public void onOpen() {
        isFocusOnMenu = true;
//        fragment.fousced();
        mSlidingMenu.getMenu().setVisibility(View.VISIBLE);
        if (mSelectedMenuView != null) {
            mSelectedMenuView.requestFocus();
        }
        ivSwitch.setVisibility(View.INVISIBLE);
    }

    public void fouseList() {
//        View view = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());
//        if (view != null) {
//            view.requestFocus();
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VodDetailObj vodInfo = mDataSet.get(position);
        if (vodInfo != null) {
            Utility.intoVODDetailActivity(this, vodInfo.getId());
        }
    }


    ///////////////////////////////////////////////////////////////////////
    // 加载更多

    private int mScrollState;
    private int totalNum = 0;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        LogUtil.d("scrollState", " " + scrollState);
        this.mScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (0 == this.mScrollState) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                // TODO 加载更多影片
                if (totalItemCount < this.totalNum) {
                    this.pagenum++;
                    this.isNeedClear = false;
                    Log.d("slf " + TAG, "onScroll(***) totalItemCount="+totalItemCount +"  totalNum="+totalNum);
                    loadData();
                }
            }
        }

    }
}
