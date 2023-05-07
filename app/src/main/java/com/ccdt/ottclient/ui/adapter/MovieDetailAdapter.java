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
import com.ccdt.ottclient.model.VodRecommendObj;
import com.ccdt.ottclient.utils.ToastUtil;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;


public class MovieDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<VodRecommendObj> recommendObjs;
    private final LayoutInflater mInflater;

    public MovieDetailAdapter(Context context, List<VodRecommendObj> recommendObjs) {
        this.mContext = context;
        this.recommendObjs = recommendObjs;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (recommendObjs != null) {
            ret = recommendObjs.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return recommendObjs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_movie_detail, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = ((TextView) convertView.findViewById(R.id.txtTitle));
            holder.imgHaibao = ((ImageView) convertView.findViewById(R.id.imgHaibao));
            holder.layout_focus = convertView.findViewById(R.id.layout_focus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        VodRecommendObj recommendObj = recommendObjs.get(position);
        if (recommendObj != null) {
            Utility.displayImage(recommendObj.getShowUrl(), holder.imgHaibao);
            holder.txtTitle.setText(recommendObj.getMzName());
        }

        return convertView;
    }

    public static class ViewHolder {

        public View layout_focus;
        public TextView txtTitle;
        public ImageView imgHaibao;
    }

}
