package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.NewsInfoObj;
import com.ccdt.ottclient.model.PaikeInfoObj;
import com.ccdt.ottclient.model.VodDetailObj;
import com.ccdt.ottclient.ui.fragment.SearchResultFragment;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;

public class SearchResultAdapter extends BaseAdapter {

    private List<VodDetailObj> mVodDataSet;
    private List<NewsInfoObj> mNewsDataSet;
    private List<PaikeInfoObj> mPaikeDataSet;


    private int mType;
    public static final int SEARCH_RESULT_TYPE_VOD = 0;
    public static final int SEARCH_RESULT_TYPE_NEWS = 1;
    public static final int SEARCH_RESULT_TYPE_PAIKE = 2;
    private final LayoutInflater mInflater;


    public SearchResultAdapter(Context context, List<VodDetailObj> vodDataSet, List<PaikeInfoObj> paikeDataSet, List<NewsInfoObj> newsDataSet) {
        this.mVodDataSet = vodDataSet;
        this.mNewsDataSet = newsDataSet;
        this.mPaikeDataSet = paikeDataSet;
        mInflater = LayoutInflater.from(context);

    }

    public void setType(int type) {
        this.mType = type;
    }


    @Override
    public int getCount() {
        int ret = 0;
        switch (mType) {
            case SEARCH_RESULT_TYPE_VOD:
                if (mVodDataSet != null) {
                    ret = mVodDataSet.size();
                }
                break;
            case SEARCH_RESULT_TYPE_NEWS:
                if (mNewsDataSet != null) {
                    ret = mNewsDataSet.size();
                }
                break;
            case SEARCH_RESULT_TYPE_PAIKE:
                if (mPaikeDataSet != null) {
                    ret = mPaikeDataSet.size();
                }
                break;
            default:
                break;
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mPaikeDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_search_result, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = ((TextView) convertView.findViewById(R.id.txtTitle));
            holder.imgHaibao = ((ImageView) convertView.findViewById(R.id.imgHaibao));
            holder.layout_focus = convertView.findViewById(R.id.layout_focus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        String title = "";
        String showUrl = "";

        switch (mType) {
            case SEARCH_RESULT_TYPE_VOD:
                VodDetailObj vod = mVodDataSet.get(position);
                if (vod != null) {
                    title = vod.getMzName();
                    showUrl = vod.getShowUrl();
                }
                break;
            case SEARCH_RESULT_TYPE_NEWS:
                NewsInfoObj news = mNewsDataSet.get(position);
                if (news != null) {
                    title = news.getMzName();
                    showUrl = news.getTitleImg();
                }

                break;
            case SEARCH_RESULT_TYPE_PAIKE:
                PaikeInfoObj paike = mPaikeDataSet.get(position);
                if (paike != null) {
                    title = paike.getMzName();
                    showUrl = paike.getShowUrl();
                }
                break;
            default:
                break;
        }
        holder.txtTitle.setText(title);
        Utility.displayImage(showUrl, holder.imgHaibao);
        if (position < 4) {
            int id = SearchResultFragment.ID_BTN_MOVIE;
            switch (mType) {
                case SEARCH_RESULT_TYPE_VOD:
                    id = SearchResultFragment.ID_BTN_MOVIE;
                    break;
                case SEARCH_RESULT_TYPE_PAIKE:
                    id = SearchResultFragment.ID_BTN_PAIKE;
                    break;
                case SEARCH_RESULT_TYPE_NEWS:
                    id = SearchResultFragment.ID_BTN_NEWS;
                    break;
                default:
                    break;
            }
            holder.layout_focus.setNextFocusUpId(id);
//            if (position == 0) {
//                holder.layout_focus.setId(R.id.recycler_index_0);
//                if (mOnFistPositionInited != null) {
//                    mOnFistPositionInited.onFistPositionInited(holder);
//                }
//            }
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return mType;
    }

    public static class ViewHolder {
        public View layout_focus;
        public TextView txtTitle;
        public ImageView imgHaibao;
    }
}
