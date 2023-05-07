package com.ccdt.ottclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.CollectionObj;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetCollectedListTask;
import com.ccdt.ottclient.ui.adapter.CollectAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.viva.tv.app.widget.FocusedBasePositionManager;
import io.viva.tv.app.widget.FocusedGridView;

public class CollectedActivity extends BaseActivity implements TaskCallback, AdapterView.OnItemClickListener {

    private TextView txtTitle;
    private List<CollectionObj> mDataSet = null;
    private CollectAdapter adapter;
    private TextView txtNum;
    private static final int PAGE_SIZE = 2000;
    private int currentPageNumber;
    private FocusedGridView mGrid;
    private CollectAdapter.ViewHolder focusedHolder;
    private int colorTextUnselected;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CollectedActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colorTextUnselected = this.getResources().getColor(R.color.text_unselected_vod);
        currentPageNumber = 1;

    }


    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);//海报类型
//        map.put(ConstantKey.ROAD_TYPE_SHOWFIELDS, "memberId,zbSource,id,logoUrl,mzId,mzType,mzName,validFlag,createTime,haibao");
        map.put(ConstantKey.ROAD_TYPE_PAGEYEMA, String.valueOf(currentPageNumber));
        map.put(ConstantKey.ROAD_TYPE_PAGESIZE, String.valueOf(PAGE_SIZE));
        map.put(ConstantKey.ROAD_TYPE_MZTYPE, "0,1");
        new GetCollectedListTask(this).execute(map);
    }


    private void initGrid() {
        mDataSet = new ArrayList<>();
        adapter = new CollectAdapter(this, mDataSet);
        mGrid = ((FocusedGridView) findViewById(R.id.grid));
        mGrid.setItemScaleValue(1.05f, 1.05f);
        mGrid.setFocusResId(R.drawable.bg_transparent);
        mGrid.setFocusShadowResId(R.drawable.bg_transparent);
        mGrid.setFocusViewId(R.id.layout_root);
        mGrid.setAdapter(adapter);
        mGrid.setOnItemClickListener(this);

        mGrid.setOnItemSelectedListener(new FocusedBasePositionManager.FocusItemSelectedListener() {
            @Override
            public void onItemSelected(View paramView1, int paramInt, boolean paramBoolean, View paramView2) {
                if (paramBoolean) {
                    if (paramView1 != null) {
                        Object tag = paramView1.getTag();
                        if (tag != null) {
                            if (tag instanceof CollectAdapter.ViewHolder) {
                                focusedHolder = (CollectAdapter.ViewHolder) tag;
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
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_collected;
    }

    @Override
    protected void initWidget() {
        initGrid();
        txtTitle = ((TextView) findViewById(R.id.txtTitle));
        txtNum = ((TextView) findViewById(R.id.txtNum));
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_GETCOLLECTEDLIST:
                    mDataSet.clear();
                    if (result.code == TaskResult.SUCCESS) {
                        CommonSearchInvokeResult<CollectionObj> invokeResult = (CommonSearchInvokeResult<CollectionObj>) result.data;
                        if (invokeResult != null) {
                            if (invokeResult.getRtList() != null) {
                                for (CollectionObj obj : invokeResult.getRtList()) {
                                    if (obj != null) {
                                        mDataSet.add(obj);
                                    }
                                }
                            }
                            txtNum.setText(String.valueOf(invokeResult.getTotalNum()));
                        }

                    } else {

                    }
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.ToastMessage(this, "点击了 position=" + position);
        CollectionObj collectionObj = mDataSet.get(position);
        String mzId = collectionObj.getMzId();
        if (!TextUtils.isEmpty(mzId)) {
            VodDetailActivity.actionStart(this, mzId);
        }

    }
}
