package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.ccdt.ottclient.ui.adapter.PaikeTaskDetailAdapter;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.viva.tv.app.widget.FocusedBasePositionManager;
import io.viva.tv.app.widget.FocusedGridView;

public class PaikeTaskDetailActivity extends BaseActivity implements TaskCallback, AdapterView.OnItemClickListener {

    private static final String TAG = PaikeTaskDetailActivity.class.getSimpleName();

    private RecyclerView recycler;
    private List<PaikeInfoObj> mPaikeInfoList = new ArrayList<>();
    private CommonSearchInvokeResult<PaikeInfoObj> mInvokeResult;
    private PaikeTaskDetailAdapter adapter;
    private TextView txtTitle;
    private String jobId;
    private TextView txtPaikeNum;
    private int currentPageNumber;

    private static final int PAGE_SIZE = 1000;


    public static void actionStart(Context context, String lmName,String jobId) {
        Intent intent = new Intent(context, PaikeTaskDetailActivity.class);
        intent.putExtra("lmName", lmName);
        intent.putExtra("jobId",jobId);
        context.startActivity(intent);
    }
    private FocusedGridView mGrid;
    private View focusedView;
    private void initGrid(){
        mGrid = ((FocusedGridView) findViewById(R.id.grid));
        mGrid.setItemScaleValue(1.05f, 1.05f);
        mGrid.setFocusResId(R.drawable.bg_transparent);
        mGrid.setFocusShadowResId(R.drawable.bg_transparent);
        mGrid.setFocusViewId(R.id.layout_focus);
        adapter = new PaikeTaskDetailAdapter(this, mPaikeInfoList);
        mGrid.setAdapter(adapter);

        mGrid.setOnItemClickListener(this);
        mGrid.setOnItemSelectedListener(new FocusedBasePositionManager.FocusItemSelectedListener() {
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

//        mGrid.setOnScrollListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPageNumber = 1;
        Intent intent = getIntent();
        String lmName = intent.getStringExtra("lmName");
        jobId = intent.getStringExtra("jobId");
        txtTitle.setText(lmName);

        requestData();
    }

    private void requestData() {
        if (!TextUtils.isEmpty(jobId)) {
            Map<String, String> params = new HashMap<>();
            String userId = Account.getInstance().userId;
            params.put(ConstantKey.ROAD_TYPE_PAGEYEMA, String.valueOf(currentPageNumber));
            params.put(ConstantKey.ROAD_TYPE_PAGESIZE, String.valueOf(PAGE_SIZE));
            params.put(ConstantKey.ROAD_TYPE_JOBID,jobId);
            new GetPaikeListTask(this).execute(params);
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_paike_choose;
    }

    @Override
    public void initWidget() {
        txtTitle = ((TextView) findViewById(R.id.txt_title));
        txtPaikeNum = ((TextView) findViewById(R.id.txtPaikeNum));

        initGrid();

    }


    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
//            mPaikeInfoList.clear();
            if (result.data != null) {
                mInvokeResult = (CommonSearchInvokeResult<PaikeInfoObj>) result.data;
//                            mPaikeInfoList = mInvokeResult.getRtList();
                if (mInvokeResult.getRtList() != null) {
                    for (PaikeInfoObj pkInfo : mInvokeResult.getRtList()) {
                        if (pkInfo != null) {
                            mPaikeInfoList.add(pkInfo);
                        }
                    }
                }
            }
            txtPaikeNum.setText(String.valueOf(mPaikeInfoList.size()));
            adapter.notifyDataSetChanged();
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
}
