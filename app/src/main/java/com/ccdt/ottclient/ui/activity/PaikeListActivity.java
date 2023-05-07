package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ccdt.ottclient.Account;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.PaikeInfoObj;
import com.ccdt.ottclient.provider.SQLDataItemModel;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetPaikeListTask;
import com.ccdt.ottclient.ui.adapter.PaikeListAdapter;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.viva.tv.app.widget.FocusedBasePositionManager;
import io.viva.tv.app.widget.FocusedGridView;

public class PaikeListActivity extends BaseActivity implements TaskCallback, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private static final String TAG = PaikeListActivity.class.getSimpleName();

    private FocusedGridView grid;
    private List<PaikeInfoObj> mPaikeInfoList = new ArrayList<>();
//    private CommonSearchInvokeResult<PaikeInfoObj> mInvokeResult;
    private PaikeListAdapter adapter;
    private TextView txtTitle;

    public static final int PAIKE_TYPE_JINGXUAN = 0;
    public static final int PAIKE_TYPE_WODE = 1;
    private TextView txtPaikeNum;
    private int type;
    private String lmId;
    private int currentPageIndex;

    private boolean isInit;


    private static final int PAGE_SIZE = 500;


    public static void actionStart(Context context, String lmId, String lmName, int type) {
        Intent intent = new Intent(context, PaikeListActivity.class);
        intent.putExtra("lmId", lmId);
        intent.putExtra("lmName", lmName);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isInit = true;
        currentPageIndex = 1;
        Intent intent = getIntent();
        lmId = intent.getStringExtra("lmId");
        String lmName = intent.getStringExtra("lmName");
        type = intent.getIntExtra("type", PAIKE_TYPE_JINGXUAN);
        txtTitle.setText(lmName);

        requestData();
    }

    private void requestData() {
        if (!TextUtils.isEmpty(lmId)) {
            Map<String, String> params = new HashMap<>();
            String userId = Account.getInstance().userId;
            if (type == PAIKE_TYPE_WODE && !TextUtils.isEmpty(userId)) {
                params.put("memberId", userId);
            } else {
                params.put(ConstantKey.ROAD_TYPE_LMID, lmId);
            }
            params.put(ConstantKey.ROAD_TYPE_PAGEYEMA, String.valueOf(currentPageIndex));
            params.put(ConstantKey.ROAD_TYPE_PAGESIZE, String.valueOf(PAGE_SIZE));

            new GetPaikeListTask(this).execute(params);
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_paike_list;
    }

    private View focusedView;

    @Override
    public void initWidget() {
        grid = ((FocusedGridView) findViewById(R.id.grid));
        grid.setItemScaleValue(1.05f, 1.05f);
        grid.setFocusResId(R.drawable.bg_transparent);
        grid.setFocusShadowResId(R.drawable.bg_transparent);
        grid.setFocusViewId(R.id.layout_focus);

        txtTitle = ((TextView) findViewById(R.id.txt_title));
        txtPaikeNum = ((TextView) findViewById(R.id.txtPaikeNum));
        adapter = new PaikeListAdapter(this, mPaikeInfoList);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(this);
        grid.setOnItemSelectedListener(new FocusedBasePositionManager.FocusItemSelectedListener() {
            @Override
            public void onItemSelected(View paramView1, int paramInt, boolean paramBoolean, View paramView2) {
                if(paramBoolean){
                    if (paramView1 != null) {
                        focusedView = paramView1.findViewById(R.id.layout_focus);
                        if (focusedView != null) {
                            focusedView.setBackgroundResource(R.drawable.bg_vod_blue);
                        }
                    }
                } else {
                    if (focusedView != null) {
                        focusedView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    focusedView = null;
                }

            }
        });

        grid.setOnScrollListener(this);

    }


    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            if (result.data != null) {
                CommonSearchInvokeResult<PaikeInfoObj> mInvokeResult = (CommonSearchInvokeResult<PaikeInfoObj>) result.data;
//                            mPaikeInfoList = mInvokeResult.getRtList();
                this.totalNum = mInvokeResult.getTotalNum();
                if (mInvokeResult.getRtList() != null) {
                    for (PaikeInfoObj pkInfo : mInvokeResult.getRtList()) {
                        if (pkInfo != null) {
                            mPaikeInfoList.add(pkInfo);
                        }
                    }
                }
                adapter.notifyDataSetChanged();

                txtPaikeNum.setText(String.valueOf(this.totalNum));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mPaikeInfoList != null && position < mPaikeInfoList.size()) {
            PaikeInfoObj paikeInfoObj = mPaikeInfoList.get(position);
            if (paikeInfoObj != null) {
                Utility.intoPlayerActivity(this,
                        paikeInfoObj.getMzName(),
                        paikeInfoObj.getBfUrl(),
                        paikeInfoObj.getShowUrl(),
                        paikeInfoObj.getId(),
                        SQLDataItemModel.TYPE_MAKING);
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // 加载更多

    private int mScrollState;
    private int totalNum;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.mScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        LogUtil.d("mScrollState", " "+mScrollState);
        if(0 == this.mScrollState){
            if(firstVisibleItem + visibleItemCount == totalItemCount){
                if(totalItemCount < totalNum){
                    // TODO 加载更多
                    currentPageIndex ++;
                    requestData();
                }
            }
        }
    }
}
