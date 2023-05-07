package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.viva.tv.app.widget.FocusedBasePositionManager;
import io.viva.tv.app.widget.FocusedGridView;


public class OtherListActivity extends BaseActivity implements TaskCallback, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private List<VodDetailObj> mDataSet = new ArrayList<>();
    private MovieAdapter adapter;
    private TextView txtTvTags, tvFilter;
    private LmInfoObj lminfo;
    private FocusedGridView mGrid;
    private TextView tvTitle;
    private View ivSwitch;
    private String lmid;
    private int colorTextUnselected;
    private MovieAdapter.ViewHolder focusedHolder;
    private View layout_root;

    public static void actionStart(Context context, LmInfoObj lmInfo) {
        Intent intent = new Intent(context, OtherListActivity.class);
        intent.putExtra("lmInfo", lmInfo);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagenum =1;
        colorTextUnselected = this.getResources().getColor(R.color.text_unselected_vod);
        Intent intent = getIntent();
        if (intent != null) {
            LmInfoObj lmInfo = intent.getParcelableExtra("lmInfo");
            lmid = lmInfo.getLmId();
            ivSwitch.setVisibility(View.INVISIBLE);
            tvTitle.setText(lmInfo.getLmName());
        }
        loadData();
    }

    private void loadData(){
        Map<String, String> params = new HashMap<>();
        params.put(ConstantKey.ROAD_TYPE_LMID, lmid);
        params.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);
        params.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);

        if (pagenum > 0) {
            params.put(ConstantKey.ROAD_MY_PAGE_NUMBER, pagenum + "");
        }
        params.put(ConstantKey.ROAD_TYPE_PAGESIZE, 1000 + "");

        new GetVodListTask(this).execute(params);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_movie_list;
    }

    @Override
    protected void initWidget() {
        tvTitle = ((TextView) findViewById(R.id.txt_title));
        ivSwitch = findViewById(R.id.txt_arrow_left);
        txtTvTags = ((TextView) findViewById(R.id.tv_tags));
        tvFilter = (TextView) findViewById(R.id.tv_filter_tag);
        layout_root = findViewById(R.id.layout_root);
        layout_root.setBackgroundResource(R.drawable.bg);
        adapter = new MovieAdapter(this, mDataSet);
        initGrid();
    }

    private void initGrid() {
        mGrid = ((FocusedGridView) findViewById(R.id.grid));
        mGrid.setAdapter(adapter);
        mGrid.setItemScaleValue(1.05f, 1.05f);
        mGrid.setFocusResId(R.drawable.bg_transparent);
        mGrid.setFocusShadowResId(R.drawable.bg_transparent);
        mGrid.setFocusViewId(R.id.layout_focus);

        mGrid.setOnItemClickListener(this);

        mGrid.setOnItemSelectedListener(new FocusedBasePositionManager.FocusItemSelectedListener() {
            @Override
            public void onItemSelected(View paramView1, int paramInt, boolean paramBoolean, View paramView2) {
                if(paramBoolean){
                    if (paramView1 != null) {
                        Object tag = paramView1.getTag();
                        if (tag != null) {
                            if(tag instanceof MovieAdapter.ViewHolder){
                                focusedHolder = (MovieAdapter.ViewHolder)tag;
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
    }


    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            this.totalNum = 0;
            if (result.code == TaskResult.SUCCESS) {
                if (result.data != null) {
                    CommonSearchInvokeResult<VodDetailObj> invokeResult = (CommonSearchInvokeResult<VodDetailObj>) result.data;
                    this.totalNum = invokeResult.getTotalNum();
                    List<VodDetailObj> rtList = invokeResult.getRtList();
                    if (rtList != null) {
                        for (VodDetailObj obj : rtList) {
                            if (obj != null) {
                                mDataSet.add(obj);
                            }
                        }
                    }
                }


            } else {

            }
            txtTvTags.setText(String.valueOf(this.totalNum));
            adapter.notifyDataSetChanged();
        }
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
    private int pagenum = 1;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.mScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(0 == this.mScrollState){
            if(firstVisibleItem + visibleItemCount == totalItemCount){
                // TODO 加载更多影片
                if(totalItemCount < this.totalNum){
                    pagenum++;
                    loadData();
                }
            }
        }

    }
}
