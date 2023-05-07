package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.model.NewsInfoObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetNewsListTask;
import com.ccdt.ottclient.ui.adapter.NewsListContentAdapter;
import com.ccdt.ottclient.ui.fragment.MenuFragment;
import com.ccdt.ottclient.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsListActivity extends SlidingMenuActivity
        implements MenuFragment.OnMenuItemClickListener,
        SlidingMenu.OnCloseListener, SlidingMenu.OnOpenListener, NewsListContentAdapter.OnItemClickListener, TaskCallback {

    private static final String TAG = NewsListActivity.class.getName();
    public MenuFragment menuFragment;
    private RecyclerView recycler;
    private ArrayList<NewsInfoObj> dataSet;
    public View txtArrowLeft;
    public LinearLayoutManager layoutManager;
    public NewsListContentAdapter adapter;
    private View mSelectedMenuView;
    private boolean isFocusOnMenu;
    private LmInfoObj lmInfo;
    private TextView txt_num;


    public static void actionStart(Context context, LmInfoObj lmInfo) {
        Intent intent = new Intent(context, NewsListActivity.class);
        intent.putExtra("lmInfo", lmInfo);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            lmInfo = intent.getParcelableExtra("lmInfo");
        }

        mSlidingMenu.setOnOpenListener(this);
        mSlidingMenu.setOnCloseListener(this);
        menuFragment.setOnMenuItemClickListener(this);
        menuFragment.setLmInfo(lmInfo);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_news_list;
    }

    @Override
    protected void initView() {
        recycler = ((RecyclerView) findViewById(R.id.recycler_news_list_content));
        txtArrowLeft = findViewById(R.id.txt_arrow_left);
        txt_num = ((TextView) findViewById(R.id.txt_num));
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        dataSet = new ArrayList<>();
        adapter = new NewsListContentAdapter(this, dataSet);
        adapter.setOnItemClickListener(this);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.DKGRAY)
                        .sizeResId(R.dimen.divider_news_list_content)
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());
//        recyclerChildFirst = recycler.getChildAt(0);


    }

    @Override
    protected Fragment getMenuFragment() {
        menuFragment = new MenuFragment();
        return menuFragment;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && isFocusOnMenu) {
            mSlidingMenu.showContent(true);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClose() {
        isFocusOnMenu = false;
        txtArrowLeft.setVisibility(View.VISIBLE);
        mSlidingMenu.getMenu().setVisibility(View.INVISIBLE);
//        View view = recycler.getChildAt(0);
        View view = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());
        if (view != null) {
            view.requestFocus();
        }
    }


    @Override
    public void onOpen() {
        isFocusOnMenu = true;
        txtArrowLeft.setVisibility(View.INVISIBLE);
        mSlidingMenu.getMenu().setVisibility(View.VISIBLE);
        if (mSelectedMenuView != null) {
            mSelectedMenuView.requestFocus();
        }
    }

    @Override
    public void onItemClick(int position) {
        String newsId = null;
        if (dataSet != null) {
            NewsInfoObj newsInfoObj = dataSet.get(position);
            if (newsInfoObj != null) {
                newsId = newsInfoObj.getId();
            }
        }
        NewsDetailActivity.actionStart(this, dataSet, position);
    }

    private static final int PAGE_SIZE = 2000;
    private int totalNum = 0;
    @Override
    public void onMenuItemClickListener(View selectedMenuView, LmInfoObj lmInfo) {
        if (selectedMenuView != null) {
            this.mSelectedMenuView = selectedMenuView;
        }
        if (lmInfo != null) {
            Map<String, String> map = new HashMap<>();
            map.put(ConstantKey.ROAD_TYPE_LMID, lmInfo.getLmId());
//            map.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, "content");
            map.put(ConstantKey.ROAD_TYPE_PAGESIZE, String.valueOf(PAGE_SIZE));
            new GetNewsListTask(this).execute(map);
        }
//        mSlidingMenu.showContent(true);
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        int id = result.taskId;
        switch (id) {
            case Constants.TASK_GETNEWSLIST:
                dataSet.clear();
                this.totalNum = 0;
                if (result.code == 200) {
                    CommonSearchInvokeResult<NewsInfoObj> invokeResult = (CommonSearchInvokeResult<NewsInfoObj>) result.data;
                    if (invokeResult != null) {
                        List<NewsInfoObj> rtList = invokeResult.getRtList();
                        this.totalNum = invokeResult.getTotalNum();
                        if (rtList != null) {
                            for (NewsInfoObj obj : rtList) {
                                if (obj != null) {
                                    dataSet.add(obj);
                                }
                            }
                        }
                    }

                } else {
                    LogUtil.e(TAG, result.message);
                }
                txt_num.setText(String.valueOf(this.totalNum));
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
