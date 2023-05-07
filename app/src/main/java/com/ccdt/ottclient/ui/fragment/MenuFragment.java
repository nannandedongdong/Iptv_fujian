package com.ccdt.ottclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetSubLmTask;
import com.ccdt.ottclient.ui.adapter.MenuAdapter;
import com.ccdt.ottclient.utils.LogUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class MenuFragment extends BaseFragment implements MenuAdapter.OnItemClickListener, TaskCallback {
    private RecyclerView recycler;
    private List<LmInfoObj> dataSet;
    private MenuAdapter adapter;
    private LinearLayoutManager layoutManager;
    private static final String TAG = MenuFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        recycler = ((RecyclerView) v.findViewById(R.id.recycler_menu));
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        dataSet = new ArrayList<>();

        adapter = new MenuAdapter(mContext, dataSet);
        adapter.setOnItemClickListener(this);

        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        recycler.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.line)
                        .sizeResId(R.dimen.divider_menu)
                        .build());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        LmInfoObj lmInfo = MyApp.getInstance().getOneLevelLmInfoByType(Constants.ONELEVEL_LM_TYPE_NEWS);
        LmInfoObj lmInfo = getLmInfo();
        if (lmInfo != null) {
            GetSubLmTask.executeTask(this, lmInfo.getLmId());
        }
    }



    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            if (result.code == 200) {
                switch (result.taskId) {
                    case Constants.TASK_GETSUBLM:
                        dataSet.clear();
                        List<LmInfoObj> list = (List<LmInfoObj>) result.data;
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                dataSet.add(list.get(i));
                            }
                        }
                        LogUtil.d(TAG, dataSet.toString());
                        adapter.notifyDataSetChanged();
//                        this.onItemClick(null, 0);
                        break;
                    default:
                        break;
                }
            } else {
                LogUtil.e(TAG, "菜单异常 error:" + result.message);
            }
        }

    }

    @Override
    public void onItemClick(View v, int position) {
        LmInfoObj lmInfo = dataSet.get(position);
        LogUtil.d("slf ", "点击了菜单 ---" + lmInfo.getLmName());
        if (lmInfo != null) {
            if (mOnMenuItemClickListener != null) {
                mOnMenuItemClickListener.onMenuItemClickListener(v, lmInfo);
            }
        }
    }

    private OnMenuItemClickListener mOnMenuItemClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mOnMenuItemClickListener = listener;
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClickListener(View selectedMenuView, LmInfoObj lmInfo);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void fousced() {
        View view = layoutManager.findViewByPosition(1);
        if (view != null) {
            view.requestFocus();
        }
    }
}
