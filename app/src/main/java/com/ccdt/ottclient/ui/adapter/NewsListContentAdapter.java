package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.NewsInfoObj;
import com.ccdt.ottclient.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsListContentAdapter
        extends RecyclerView.Adapter<NewsListContentAdapter.ViewHolder> {

    private Context mContext;
    private List<NewsInfoObj> mDataSet;
    private boolean isInit = false;

    public NewsListContentAdapter(Context context, List<NewsInfoObj> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        isInit = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_news_list_content, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NewsInfoObj newsInfo = null;
        if (mDataSet != null) {
            newsInfo = mDataSet.get(position);
        }

        if (newsInfo != null) {
            if (isInit && position == 0) {
                holder.itemView.requestFocus();
            }

            if (position == 0) {
                holder.itemView.setId(R.id.news_list_index_0);
            }

            holder.imageContainer.removeAllViews();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });

            ImageView childImage = null;
            int width = (int) mContext.getResources().getDimension(R.dimen.width_news_list_content_image);
            int marginLeft = (int) mContext.getResources().getDimension(R.dimen.margin_left_news_list_content_image);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
            ArrayList<String> newsImgs = newsInfo.getNewsImgs();
            if (newsImgs != null && newsImgs.size() > 0) {
                for (int i = 0; i < newsImgs.size(); i++) {
                    if(i >= 3){
                        break;
                    }
                    String url = newsImgs.get(i);
                    if(!TextUtils.isEmpty(url)){
                        childImage = new ImageView(mContext);
                        childImage.setScaleType(ImageView.ScaleType.FIT_XY);
                        params.leftMargin = marginLeft;
                        childImage.setLayoutParams(params);
                        holder.imageContainer.addView(childImage);
                        Utility.displayImage(url, childImage);
                    }
                }
            }

            holder.txtNewsTitle.setText(newsInfo.getMzName());
            holder.txtNewsFrom.setText(newsInfo.getSource());
            holder.txtNewsTime.setText(newsInfo.getUpdateTime());
        }


    }

    @Override
    public int getItemCount() {
        int ret = 0;

        if (mDataSet != null) {
            ret += mDataSet.size();
        }

        return ret;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout imageContainer;
        public TextView txtNewsTitle;
        public TextView txtNewsFrom;
        public TextView txtNewsTime;

        public ViewHolder(View itemView) {
            super(itemView);
            imageContainer = ((LinearLayout) itemView.findViewById(R.id.image_container));
            txtNewsTitle = ((TextView) itemView.findViewById(R.id.txtNewsTitle));
            txtNewsFrom = ((TextView) itemView.findViewById(R.id.txtNewsFrom));
            txtNewsTime = ((TextView) itemView.findViewById(R.id.txtNewsTime));
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
