package com.ccdt.ottclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.NewsInfoObj;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetNewsListTask;
import com.ccdt.ottclient.ui.activity.NewsDetailActivity;
import com.ccdt.ottclient.ui.activity.NewsListActivity;
import com.ccdt.ottclient.ui.widget.NewsPageItemLayout;
import com.ccdt.ottclient.utils.LogUtil;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsPageFragment extends BaseIndicatorFragment implements TaskCallback {

    private static final String TAG = NewsPageFragment.class.getName();

    public View root;
//    public Animation bigAnim;
    public CommonSearchInvokeResult<NewsInfoObj> invokeResult;
    private View view_0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_news_main, container, false);
//        bigAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_big_common);
        view_0 = root.findViewById(R.id.btn_news_main_left_up);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < btnArr.length; i++) {
            View v = root.findViewById(btnArr[i]);
            setOnItemClickListener(v, i);
            setClickAndFocusEvent(v, i);
        }
        Map<String, String> map = new HashMap<>();
        map.put(ConstantKey.ROAD_TYPE_LMID, getLmId());
        map.put(ConstantKey.ROAD_TYPE_PAGEYEMA, "1");
        map.put(ConstantKey.ROAD_TYPE_PAGESIZE, "200");
        new GetNewsListTask(this).execute(map);
    }


    /**
     * 设置点击和焦点事件
     */
    private void setClickAndFocusEvent(View v, final int position) {
        if (isFirstRow(position)) {
            v.setNextFocusUpId(getIndicatorTabId());
        }
//        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    v.startAnimation(bigAnim);
//                } else {
//                    v.clearAnimation();
//                }
//            }
//        });

    }

    private int[] btnArr = {
            R.id.btn_news_main_left_up,
            R.id.btn_news_main_left_down,
            R.id.btn_news_main_center,
            R.id.btn_news_main_right_0,
            R.id.btn_news_main_right_1,
            R.id.btn_news_main_right_2,
            R.id.btn_news_main_right_3,
            R.id.btn_news_main_more
    };

    /**
     * 点击事件
     *
     * @param position
     */
    public void setOnItemClickListener(View v, final int position) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 7:
                        NewsListActivity.actionStart(mContext, getLmInfo());
                        break;
                    default:
                        String newsId = "";
                        if (invokeResult != null) {
                            List<NewsInfoObj> rtList = invokeResult.getRtList();
                            if (rtList != null) {
                                NewsInfoObj newsInfo = rtList.get(position);
                                if (newsInfo != null) {
                                    newsId = newsInfo.getId();
                                }
                            }
                        }
                        NewsDetailActivity.actionStart(mContext, (ArrayList<NewsInfoObj>) invokeResult.getRtList(), position);
                        break;
                }
            }
        });
    }

    private void setData() {
        if (invokeResult != null) {
            List<NewsInfoObj> rtList = invokeResult.getRtList();
            for (int i = 0; i < btnArr.length - 1; i++) {
                if (rtList != null && i < rtList.size()) {
                    NewsInfoObj newsInfo = rtList.get(i);

                    String title = TextUtils.isEmpty(newsInfo.getMzName()) ? "" : newsInfo.getMzName();
                    String from = TextUtils.isEmpty(newsInfo.getSource()) ? "" : newsInfo.getSource();
                    String time = newsInfo.getUpdateTime();

                    String imgUrl = newsInfo.getTitleImg();

//                    ArrayList<String> newsImgs = newsInfo.getNewsImgs();
//                    String imgUrl = null;
//                    if (newsImgs != null && newsImgs.size() > 0) {
//                        imgUrl = newsImgs.get(0);
//                    }

//                    LogUtil.d(TAG, newsInfo.toString());
                    switch (i) {
                        case 0:
                            TextView txtTitleLeftUp = (TextView) root.findViewById(R.id.txt_title_left_up);
                            TextView txtFromLeftUp = (TextView) root.findViewById(R.id.txt_from_left_up);
                            TextView txtTimeLeftUp = (TextView) root.findViewById(R.id.txt_time_left_up);
                            ImageView imgLeftUp = (ImageView) root.findViewById(R.id.img_left_up);

                            txtTitleLeftUp.setText(title);
                            txtFromLeftUp.setText(from);
                            txtTimeLeftUp.setText(time);
//                            ImageLoader.getInstance().displayImage(imgUrl, imgLeftUp);
                            Utility.displayImage(imgUrl, imgLeftUp);
                            break;
                        case 1:
                            TextView txtTitleLeftDown = (TextView) root.findViewById(R.id.txt_title_left_down);
                            TextView txtFromLeftDown = (TextView) root.findViewById(R.id.txt_from_left_down);
                            TextView txtTimeLeftDown = (TextView) root.findViewById(R.id.txt_time_left_down);

                            txtTitleLeftDown.setText(title);
                            txtFromLeftDown.setText(from);
                            txtTimeLeftDown.setText(time);
                            break;
                        case 2:
                            TextView txtTitleCenter = (TextView) root.findViewById(R.id.txt_title_center);
                            TextView txtFromCenter = (TextView) root.findViewById(R.id.txt_from_center);
                            TextView txtTimeCenter = (TextView) root.findViewById(R.id.txt_time_center);
                            ImageView imgCenter = (ImageView) root.findViewById(R.id.img_center);

                            txtTitleCenter.setText(title);
                            txtFromCenter.setText(from);
                            txtTimeCenter.setText(time);
                            Utility.displayImage(imgUrl, imgCenter);
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            NewsPageItemLayout itemLayout = (NewsPageItemLayout) root.findViewById(btnArr[i]);
                            itemLayout.setNewsTitle(title);
                            itemLayout.setNewsFrom(from);
                            itemLayout.setNewsTime(time);
                            Utility.displayImage(imgUrl, itemLayout.getNewsImageView());
                            break;
                        case 7:
                            break;
                    }
                }
            }

        }

    }

    /**
     * 判断是否在第一行，用于焦点向上到指示器
     *
     * @param position
     * @return
     */
    private boolean isFirstRow(int position) {
        boolean ret = false;

        switch (position) {
            case 0:
            case 2:
            case 3:
                ret = true;
                break;
            default:
                ret = false;
                break;
        }

        return ret;
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        int id = result.taskId;
        switch (id) {
            case Constants.TASK_GETNEWSLIST:
                if (result.code == 200) {
                    invokeResult = (CommonSearchInvokeResult<NewsInfoObj>) result.data;
                    setData();
                } else {
                    LogUtil.e(TAG, "" + result.message);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetFocus() {
        super.onGetFocus();
        view_0.requestFocus();
    }
}
