package com.ccdt.ottclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.LmIconObj;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetSubLmTask;
import com.ccdt.ottclient.ui.activity.MovieListActivity;
import com.ccdt.ottclient.ui.activity.OtherListActivity;
import com.ccdt.ottclient.utils.Utility;
import com.slf.frame.tv.widget.TvRelativeLayoutAsGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 影视分类
 * Created by hanyue on 2015/11/2.
 */
public class VodPageFragment extends BaseIndicatorFragment implements TaskCallback, TvRelativeLayoutAsGroup.OnChildSelectListener, View.OnClickListener {
    private List<LmInfoObj> lmInfoList;
    public View root;
    private static final String TAG = VodPageFragment.class.getName();
    private TvRelativeLayoutAsGroup layoutRoot;

    private ArrayList<View> layouts = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_vod_main, container, false);
        findViewById();
        return root;
    }


    private void findViewById() {
        layoutRoot = (TvRelativeLayoutAsGroup) root.findViewById(R.id.root);

        for (int i = 0; i < layoutIds.length; i++) {
            View v = root.findViewById(layoutIds[i]);
            v.setOnClickListener(this);
            v.setTag(i);
            if (i == 0 || i == 3 || i == 4 || i == 7) {
                v.setNextFocusUpId(getIndicatorTabId());
            }
            layouts.add(v);
        }

        layoutRoot.setOnChildSelectListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GetSubLmTask.executeTask(this, getLmId());

    }

    private void showData() {
        if (lmInfoList != null && lmInfoList.size() > 0) {
            for (int i = 0; i < layoutIds.length; i++) {
                if (i < lmInfoList.size()) {
                    LmInfoObj lmInfoObj = lmInfoList.get(i);
                    if (lmInfoObj != null) {
                        View v = layouts.get(i);
                        ImageView tag_img = (ImageView) v.findViewWithTag("tag_img");
                        TextView tag_txt = (TextView) v.findViewWithTag("tag_txt");
                        tag_txt.setText(lmInfoObj.getLmName());

                        List<LmIconObj> lmIconList = lmInfoObj.getLmIcon();
                        String iconUrl = null;
                        String backgroundUrl = null;
                        if (lmIconList != null) {
                            for (int j = 0; j < lmIconList.size(); j++) {
                                LmIconObj lmIcon = lmIconList.get(j);
                                if (lmIcon != null) {
                                    String signCode = lmIcon.getSignCode();
                                    if ("icon".equals(signCode)) {
                                        iconUrl = lmIcon.getImageUrl();
                                    } else if ("background".equals(signCode)) {
                                        backgroundUrl = lmIcon.getImageUrl();
                                    }
                                }
                            }
                        }
                        Utility.displayImage(backgroundUrl, tag_img, R.drawable.img_default);
                    }
                }
            }
        }
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            if (result.code == TaskResult.SUCCESS) {
                switch (result.taskId) {
                    case Constants.TASK_GETSUBLM:
                        if (result.data != null) {
                            lmInfoList = (List<LmInfoObj>) result.data;
                            showData();
                        }
                        break;
                }
            } else {

            }
        }
    }


    @Override
    public void onGetFocus() {
        super.onGetFocus();
        if (layouts != null && layouts.size() > 0) {
            layouts.get(0).requestFocus();
        }
    }

    @Override
    public void onChildSelect(View child, boolean hasFocus) {
//        View v = child.findViewWithTag("tag_focus");
//        if (v != null) {
//            if (hasFocus) {
//                v.setBackgroundResource(R.drawable.bg_focused_border);
//            } else {
//                v.setBackgroundColor(Color.TRANSPARENT);
//            }
//        }
    }

    private int[] layoutIds = {
            R.id.layout_1,
            R.id.layout_2,
            R.id.layout_3,
            R.id.layout_4,
            R.id.layout_5,
            R.id.layout_6,
            R.id.layout_7,
            R.id.layout_8
    };

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
            int position = (int) tag;
            if (lmInfoList != null) {
                if (position < lmInfoList.size()) {
                    LmInfoObj lmInfo = lmInfoList.get(position);
                    if (lmInfo != null) {
                        if (position == 0) {
                            MovieListActivity.actionStart(mContext, lmInfo);
                        } else {
                            OtherListActivity.actionStart(mContext, lmInfo);
                        }
                    }
                }
            }
        }
    }

}
