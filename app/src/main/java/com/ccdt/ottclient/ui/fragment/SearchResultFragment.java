package com.ccdt.ottclient.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ccdt.ottclient.Constants;
import com.ccdt.ottclient.R;
import com.ccdt.ottclient.config.ConstantKey;
import com.ccdt.ottclient.config.ConstantValue;
import com.ccdt.ottclient.model.CommonSearchInvokeResult;
import com.ccdt.ottclient.model.NewsInfoObj;
import com.ccdt.ottclient.model.PaikeInfoObj;
import com.ccdt.ottclient.model.VodDetailObj;
import com.ccdt.ottclient.provider.SQLDataItemModel;
import com.ccdt.ottclient.tasks.TaskCallback;
import com.ccdt.ottclient.tasks.TaskResult;
import com.ccdt.ottclient.tasks.impl.GetNewsListTask;
import com.ccdt.ottclient.tasks.impl.GetPaikeListTask;
import com.ccdt.ottclient.tasks.impl.GetVodListTask;
import com.ccdt.ottclient.ui.activity.NewsDetailActivity;
import com.ccdt.ottclient.ui.activity.VodDetailActivity;
import com.ccdt.ottclient.ui.adapter.SearchResultAdapter;
import com.ccdt.ottclient.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.viva.tv.app.widget.FocusedBasePositionManager;
import io.viva.tv.app.widget.FocusedGridView;

public class SearchResultFragment extends BaseFragment implements TaskCallback, View.OnClickListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener {

    private FocusedGridView mGrid;
    private SearchResultAdapter adapter;
    private List<VodDetailObj> vodDataSet = new ArrayList<>();
    private ArrayList<NewsInfoObj> newsDataSet = new ArrayList<>();
    private List<PaikeInfoObj> paikeDataSet = new ArrayList<>();

    private boolean isShow;
    private String keyword;
    private TextView txtMovieNum;
    private TextView txtPaikeNum;
    private TextView txtNewsNum;
    private int selectedId;
    private View btnMovie;
    private View btnPaike;
    private View btnNews;

    public static final int ID_BTN_MOVIE = R.id.btnMovie;
    public static final int ID_BTN_PAIKE = R.id.btnPaike;
    public static final int ID_BTN_NEWS = R.id.btnNews;

    private boolean isReset;
    private boolean isVisible;
    private View root;
    private SearchResultAdapter.ViewHolder mHolder;
    private int colorTextUnselected;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search_result, container, false);
        colorTextUnselected = mContext.getResources().getColor(R.color.text_unselected_vod);
        initGrid();

        txtMovieNum = ((TextView) root.findViewById(R.id.txtMovieNum));
        txtPaikeNum = ((TextView) root.findViewById(R.id.txtPaikeNum));
        txtNewsNum = ((TextView) root.findViewById(R.id.txtNewsNum));
        btnMovie = root.findViewById(ID_BTN_MOVIE);
        btnPaike = root.findViewById(ID_BTN_PAIKE);
        btnNews = root.findViewById(ID_BTN_NEWS);

        btnMovie.setOnClickListener(this);
        btnPaike.setOnClickListener(this);
        btnNews.setOnClickListener(this);
        btnMovie.setOnFocusChangeListener(this);
        btnPaike.setOnFocusChangeListener(this);
        btnNews.setOnFocusChangeListener(this);
//        adapter.setSetNextFocusDownId(this);
        return root;
    }

    private void initGrid() {
        mGrid = ((FocusedGridView) root.findViewById(R.id.grid));
        adapter = new SearchResultAdapter(mContext, vodDataSet, paikeDataSet, newsDataSet);
        adapter.setType(SearchResultAdapter.SEARCH_RESULT_TYPE_VOD);

        mGrid.setItemScaleValue(1.05f, 1.05f);
        mGrid.setFocusResId(R.drawable.bg_transparent);
        mGrid.setFocusShadowResId(R.drawable.bg_transparent);
        mGrid.setFocusViewId(R.id.layout_root);
        mGrid.setAutoChangeLine(false);
        mGrid.setAdapter(adapter);

        mGrid.setOnItemClickListener(this);
        mGrid.setOnItemSelectedListener(new FocusedBasePositionManager.FocusItemSelectedListener() {
            @Override
            public void onItemSelected(View paramView1, int paramInt, boolean paramBoolean, View paramView2) {
                if (paramBoolean) {
                    if (paramView1 != null) {
                        Object tag = paramView1.getTag();
                        if (tag != null) {
                            if (tag instanceof SearchResultAdapter.ViewHolder) {
                                mHolder = (SearchResultAdapter.ViewHolder) tag;
                                mHolder.txtTitle.setTextColor(Color.WHITE);
                                mHolder.layout_focus.setBackgroundResource(R.drawable.bg_vod_blue);
                            }
                        }
                    }
                } else {
                    if (mHolder != null) {
                        mHolder.txtTitle.setTextColor(colorTextUnselected);
                        mHolder.layout_focus.setBackgroundResource(R.drawable.bg_vod_black);
                        mHolder = null;
                    }
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        if (isVisible) {
//            int id = v.getProductId();
//            switch (id) {
//                case ID_BTN_MOVIE:
//                case ID_BTN_PAIKE:
//                case ID_BTN_NEWS:
//                    if (hasFocus) {
//                        View firstItemView = getItemViewForLayoutPosition(0);
//                        if (firstItemView != null) {
//                            v.setNextFocusDownId(firstItemView.getProductId());
//                        } else {
//                            v.setNextFocusDownId(id);
//                        }
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
    }


    public View getNeedFocusView() {
        View ret = getItemViewForLayoutPosition(0);
        if (ret == null) {
            ret = getSelectBtn();
        }
        return ret;
    }

    private View getItemViewForLayoutPosition(int position) {
        return mGrid.getChildAt(0);
    }


    private View getSelectBtn() {
        View ret = null;
        switch (selectedId) {
            case ID_BTN_MOVIE:
                ret = btnMovie;
                break;
            case ID_BTN_PAIKE:
                ret = btnPaike;
                break;
            case ID_BTN_NEWS:
                ret = btnNews;
                break;
            default:
                break;
        }
        return ret;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isShow = true;
        refreshResultFragment();
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void refreshResultFragment() {
        isReset = true;
        selectedId = R.id.btnMovie;
        if (isShow) {
            if (!TextUtils.isEmpty(keyword)) {
                initVod();
                initPaike();
                iniNews();
            }
        }
    }

    private void initVod() {
        Map<String, String> mapVod = new HashMap<>();
        mapVod.put(ConstantKey.ROAD_TYPE_KWORDS, keyword);
        mapVod.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);
        mapVod.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);
        mapVod.put(ConstantKey.ROAD_TYPE_PAGESIZE, "1000");
        mapVod.put(ConstantKey.ROAD_TYPE_TERMINAL, ConstantValue.TERMINAL_STB);
        new GetVodListTask(this).execute(mapVod);
    }

    private void initPaike() {
        Map<String, String> mapPaike = new HashMap<>();
        mapPaike.put(ConstantKey.ROAD_TYPE_KWORDS, keyword);
        mapPaike.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);
        mapPaike.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);
        mapPaike.put(ConstantKey.ROAD_TYPE_PAGESIZE, "1000");
        mapPaike.put(ConstantKey.ROAD_TYPE_TERMINAL, ConstantValue.TERMINAL_STB);
        new GetPaikeListTask(this).execute(mapPaike);
    }

    private void iniNews() {
        Map<String, String> mapNews = new HashMap<>();
        mapNews.put(ConstantKey.ROAD_TYPE_KWORDS, keyword);
        mapNews.put(ConstantKey.ROAD_TYPE_SHOWTYPE, ConstantValue.SHOWTYPE_VERTICAL);
        mapNews.put(ConstantKey.ROAD_TYPE_EQUIPTYPE, ConstantValue.EQUIPTYPE_BASE);
        mapNews.put(ConstantKey.ROAD_TYPE_PAGESIZE, "1000");
        mapNews.put(ConstantKey.ROAD_TYPE_TERMINAL, ConstantValue.TERMINAL_STB);
        new GetNewsListTask(this).execute(mapNews);
    }

    @Override
    public void onTaskFinished(TaskResult result) {
        if (result != null) {
            switch (result.taskId) {
                case Constants.TASK_GETMOVIELIST:
                    int vodNum = 0;
                    if (result.code == TaskResult.SUCCESS) {
                        vodDataSet.clear();
                        if (result.data != null) {
                            CommonSearchInvokeResult<VodDetailObj> invokeResult = (CommonSearchInvokeResult<VodDetailObj>) result.data;
                            List<VodDetailObj> rtList = invokeResult.getRtList();
                            vodNum = invokeResult.getTotalNum();
                            if (rtList != null) {
                                for (VodDetailObj obj : rtList) {
                                    if (obj != null) {
                                        vodDataSet.add(obj);
                                    }
                                }
                            }
                        }

                        if (isReset) {
                            adapter.notifyDataSetChanged();
                            isReset = false;
                        }
                    } else {

                    }
                    txtMovieNum.setText(String.valueOf(vodNum));
                    break;
                case Constants.TASK_GETPAIKELIST:
                    int paikeNum = 0;
                    if (result.code == TaskResult.SUCCESS) {
                        paikeDataSet.clear();
                        if (result.data != null) {
                            CommonSearchInvokeResult<PaikeInfoObj> invokeResult = (CommonSearchInvokeResult<PaikeInfoObj>) result.data;
                            paikeNum = invokeResult.getTotalNum();
                            List<PaikeInfoObj> rtList = invokeResult.getRtList();
                            if (rtList != null) {
                                for (PaikeInfoObj obj : rtList) {
                                    if (obj != null) {
                                        paikeDataSet.add(obj);
                                    }
                                }
                            }
                        }

//                        adapter.notifyDataSetChanged();
                    } else {

                    }
                    txtPaikeNum.setText(String.valueOf(paikeNum));
                    break;
                case Constants.TASK_GETNEWSLIST:
                    int newsNum = 0;
                    if (result.code == TaskResult.SUCCESS) {
                        newsDataSet.clear();
                        if (result.data != null) {
                            CommonSearchInvokeResult<NewsInfoObj> invokeResult = (CommonSearchInvokeResult<NewsInfoObj>) result.data;
                            newsNum = invokeResult.getTotalNum();
                            List<NewsInfoObj> rtList = invokeResult.getRtList();
                            if (rtList != null) {
                                for (NewsInfoObj obj : rtList) {
                                    if (obj != null) {
                                        newsDataSet.add(obj);
                                    }
                                }
                            }
                        }

//                        adapter.notifyDataSetChanged();
                    } else {

                    }
                    txtNewsNum.setText(String.valueOf(newsNum));
                    break;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.isShow = !hidden;
        refreshResultFragment();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case ID_BTN_MOVIE:
//                btnMovie.setNextFocusDownId(ID_BTN_MOVIE);
                changeType(id, SearchResultAdapter.SEARCH_RESULT_TYPE_VOD);
                break;
            case ID_BTN_PAIKE:
//                btnPaike.setNextFocusDownId(ID_BTN_PAIKE);
                changeType(id, SearchResultAdapter.SEARCH_RESULT_TYPE_PAIKE);
                break;
            case ID_BTN_NEWS:
//                btnNews.setNextFocusDownId(ID_BTN_NEWS);
                changeType(id, SearchResultAdapter.SEARCH_RESULT_TYPE_NEWS);
                break;
        }
    }

    private void changeType(int id, int type) {
        selectedId = id;
        adapter.setType(type);
        adapter.notifyDataSetChanged();
        mGrid.smoothScrollToPosition(0);
    }


    public void clearAllData() {
        txtMovieNum.setText("0");
        txtPaikeNum.setText("0");
        txtNewsNum.setText("0");

        vodDataSet.clear();
        newsDataSet.clear();
        paikeDataSet.clear();
        adapter.notifyDataSetChanged();
    }


//    @Override
//    public void onFistPositionInited(SearchResultAdapter.ViewHolder holder) {
//        if (holder != null && holder.layout_focus != null) {
//            int id = holder.layout_focus.getId();
//            btnMovie.setNextFocusDownId(id);
//            btnPaike.setNextFocusDownId(id);
//            btnNews.setNextFocusDownId(id);
//        }
//    }

    @Override
    public void onPause() {
        isVisible = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemViewType = adapter.getItemViewType(position);
        switch (itemViewType) {
            case SearchResultAdapter.SEARCH_RESULT_TYPE_VOD:
                onItemClickVod(position);
                break;
            case SearchResultAdapter.SEARCH_RESULT_TYPE_PAIKE:
                onItemClickPaike(position);
                break;
            case SearchResultAdapter.SEARCH_RESULT_TYPE_NEWS:
                onItemClickNews(position);
                break;
            default:
                break;
        }
    }

    private void onItemClickVod(int position) {
        if (vodDataSet != null && position < vodDataSet.size()) {
            VodDetailObj vodDetailObj = vodDataSet.get(position);
            if (vodDetailObj != null) {
                VodDetailActivity.actionStart(mContext, vodDetailObj.getId());
            }
        }
    }

    private void onItemClickNews(int position) {
        if (newsDataSet != null && position < newsDataSet.size()) {
            NewsInfoObj newsInfoObj = newsDataSet.get(position);
            if (newsInfoObj != null) {
                NewsDetailActivity.actionStart(mContext, newsDataSet, position);
            }
        }
    }

    private void onItemClickPaike(int position) {
        if (paikeDataSet != null && position < paikeDataSet.size()) {
            PaikeInfoObj paikeInfoObj = paikeDataSet.get(position);
            if (paikeInfoObj != null) {
                Utility.intoPlayerActivity(mContext,
                        paikeInfoObj.getMzName(),
                        paikeInfoObj.getBfUrl(),
                        paikeInfoObj.getShowUrl(),
                        paikeInfoObj.getId(),
                        SQLDataItemModel.TYPE_MAKING);
            }
        }
    }
}
