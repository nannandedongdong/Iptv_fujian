package com.ccdt.ottclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetSubLmTask;
import com.ccdt.ottclient.ui.adapter.MovieMenuAdapter;
import com.ccdt.ottclient.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


public class MovieMenuFragment extends BaseFragment implements TaskCallback {
    private static final String TAG = MovieMenuFragment.class.getName();
    private List<LmInfoObj> dataSet = new ArrayList<>();
    private MovieMenuAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LmInfoObj lmInfo = getLmInfo();
        if (lmInfo != null) {
            new GetSubLmTask(this).execute(lmInfo.getLmId());
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moive_menu, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lv_menu);

        adapter = new MovieMenuAdapter(mContext, dataSet);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnMenuItemClickListener != null) {
                    LmInfoObj lmInfo = dataSet.get(position);
                    if (lmInfo != null) {
                        mOnMenuItemClickListener.onMenuItemClickListener(view, lmInfo);
                    }
                }
            }
        });
        return view;
    }


    private OnMenuItemClickListener mOnMenuItemClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mOnMenuItemClickListener = listener;
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClickListener(View selectedMenuView, LmInfoObj lmInfo);
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
                LogUtil.e(TAG, "新闻列表页异常 error:" + result.message);
            }
        }
    }


}
