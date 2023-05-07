package com.ccdt.ottclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.ui.adapter.SearchDefaultAdapter;
import com.ccdt.ottclient.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页面  右侧-默认
 */
public class SearchDefaultFragment extends BaseFragment implements SearchDefaultAdapter.OnItemClickListener {
    public GridView grid;
    public RecyclerView recycler;
    public List<String> dataSet;
    public SearchDefaultAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_default, container, false);
        recycler = ((RecyclerView) v.findViewById(R.id.recycler_search_default));
        dataSet = new ArrayList<>();
        dataSet.add("奔跑吧兄弟第三季");
        dataSet.add("盗墓笔记");
        dataSet.add("花千骨");
        dataSet.add("极限挑战");
        dataSet.add("终结者：创世纪");
        dataSet.add("权力的游戏 第六季");
        dataSet.add("急诊室");
        dataSet.add("实习医生");
        dataSet.add("笑傲江湖");
        dataSet.add("暮光之城");
        adapter = new SearchDefaultAdapter(mContext, dataSet);
        adapter.setOnItemClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
        return v;
    }


    @Override
    public void onItemClickListener(int position) {

        ToastUtil.ToastMessage(mContext, "此功能暂未开通");

//        if (mOnItemClickListener != null) {
//            mOnItemClickListener.setOnItemClickListener(dataSet.get(position));
//        }
    }

    public View getNeedFocusView() {
        View ret = null;
        RecyclerView.ViewHolder holder = recycler.findViewHolderForLayoutPosition(0);
        if (holder != null) {
            ret = holder.itemView;
        }
        return ret;
    }


    private SearchDefaultFragment.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(SearchDefaultFragment.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void setOnItemClickListener(String data);
    }
}
