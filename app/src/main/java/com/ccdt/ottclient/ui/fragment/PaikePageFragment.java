package com.ccdt.ottclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.model.PaikeInfoObj;
import com.ccdt.ottclient.provider.SQLDataItemModel;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetPaikeListTask;
import com.ccdt.ottclient.tasks.impl.GetSubLmTask;
import com.ccdt.ottclient.ui.activity.PaikeListActivity;
import com.ccdt.ottclient.ui.activity.PaikeTaskActivity;
import com.ccdt.ottclient.ui.adapter.PaikePageAdapter;
import com.ccdt.ottclient.ui.adapter.PaikePageAdapter.PaikeGridItem;
import com.ccdt.ottclient.ui.view.GridLayoutItem;
import com.ccdt.ottclient.ui.view.GridLayoutView;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaikePageFragment extends BaseIndicatorFragment implements PaikePageAdapter.OnItemClickListener, TaskCallback {

    private static final String TAG = PaikePageFragment.class.getName();
    /**
     * index 0:拍客精选 1:拍客任务
     */
    public List<LmInfoObj> mLmInfoList;
    private CommonSearchInvokeResult<PaikeInfoObj> mInvokeResult;
    private List<GridLayoutItem> mPaikeInfoList = new ArrayList<>();
    private PaikePageAdapter adapter;
    private GridLayoutView gridViewLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_paike_main, container, false);
        gridViewLayout = (GridLayoutView) root.findViewById(R.id.grid_vod);
        adapter = new PaikePageAdapter(getActivity(), mPaikeInfoList);
        adapter.setOnItemClickListener(this);
        adapter.setmIndicatorTabId(getIndicatorTabId());

        gridViewLayout.setGridLayoutViewAdapter(adapter);
        gridViewLayout.setChildFocusChangeListener(new GridLayoutView.OnChildFocusChangeListener() {
            @Override
            public void onChildFocusChange(View v, boolean hasFocus) {
                View view_focus = v.findViewById(R.id.view_focus);
                if (view_focus != null) {
                    if(hasFocus){
                        view_focus.setBackgroundResource(R.drawable.bg_paike_blue);
                    } else {
                        view_focus.setBackgroundResource(R.drawable.bg_paike_black);
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GetSubLmTask.executeTask(this, getLmId());
        Map<String, String> params = new HashMap<>();
        params.put(ConstantKey.ROAD_TYPE_LMID, getLmId());
        params.put(ConstantKey.ROAD_TYPE_PAGESIZE, "10");
        new GetPaikeListTask(this).execute(params);
    }

    @Override
    public void onItemClick(int viewType, int position) {
        switch (viewType) {
            case PaikePageAdapter.VIEW_TYPE_CHOICENESS:
                // 拍客精选
                if (mLmInfoList != null && mLmInfoList.size() > 0) {
                    LmInfoObj lmInfoObj = mLmInfoList.get(0);
                    if (lmInfoObj != null) {
                        PaikeListActivity.actionStart(mContext, lmInfoObj.getLmId(), lmInfoObj.getLmName(), PaikeListActivity.PAIKE_TYPE_JINGXUAN);
                    }
                }
                break;
            case PaikePageAdapter.VIEW_TYPE_TASK:
                // 拍客任务
                if (mLmInfoList != null && mLmInfoList.size() > 1) {
                    LmInfoObj lmInfoObj = mLmInfoList.get(1);
                    if (lmInfoObj != null) {
                        PaikeTaskActivity.actionStart(mContext, lmInfoObj);
                    }
                }
//                ToastUtil.toast("拍客任务 : " + position);
                break;
            case PaikePageAdapter.VIEW_TYPE_COMMON:
//                ToastUtil.toast("拍客推荐 : " + position);
                PaikeInfoObj paikeInfoObj = ((PaikeGridItem) mPaikeInfoList.get(position)).linfoObj;
                if (paikeInfoObj != null) {
                    Utility.intoPlayerActivity(mContext,
                            paikeInfoObj.getMzName(),
                            paikeInfoObj.getBfUrl(),
                            paikeInfoObj.getShowUrl(),
                            paikeInfoObj.getId(),
                            SQLDataItemModel.TYPE_MAKING);
                }

                break;
            default:
                break;
        }
    }


    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            if (result.code == TaskResult.SUCCESS) {
                switch (result.taskId) {
                    case Constants.TASK_GETSUBLM:
                        // 拍客精选、拍客任务
                        if (result.data != null) {
                            // index 0:拍客精选 1:拍客任务
                            mLmInfoList = (List<LmInfoObj>) result.data;
                        }
                        break;

                    case Constants.TASK_GETPAIKELIST:
                        mPaikeInfoList.clear();
                        PaikeGridItem item_normal = new PaikeGridItem();
                        mPaikeInfoList.add(item_normal);
                        mPaikeInfoList.add(item_normal);
                        if (result.data != null) {
                            mInvokeResult = (CommonSearchInvokeResult<PaikeInfoObj>) result.data;
//                            mPaikeInfoList = mInvokeResult.getRtList();
                            if (mInvokeResult.getRtList() != null) {
                                for (PaikeInfoObj pkInfo : mInvokeResult.getRtList()) {
                                    if (pkInfo != null) {
                                        PaikeGridItem item = new PaikeGridItem();
                                        item.linfoObj = pkInfo;
                                        mPaikeInfoList.add(item);
                                    }
                                }
                            }
                        }
                        showData();
                        adapter.notifyDataSetChanged();
                        break;
                    default:

                        break;
                }
            }
        }
    }
    private void showData() {
        for(int i = 0;i<mPaikeInfoList.size();i++){
            GridLayoutItem item = mPaikeInfoList.get(i);
            if (i < 2) {
                item.setWidth(290);
                item.setHeight(240);
            }else {
                item.setWidth(395);
                item.setHeight(240);
            }
        }
    }
    @Override
    public void onGetFocus() {
        gridViewLayout.getChildAt(0).requestFocus();
        super.onGetFocus();

    }
}
