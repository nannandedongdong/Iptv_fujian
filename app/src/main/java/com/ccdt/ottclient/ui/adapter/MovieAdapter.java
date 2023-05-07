package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.VodDetailObj;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;




public class MovieAdapter extends BaseAdapter {

    private static final String TAG = MovieAdapter.class.getName();

    private List<VodDetailObj> mDataSet;
    private Context mContext;
    private LayoutInflater inflater;

    public MovieAdapter(Context context, List<VodDetailObj> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mDataSet != null) {
            ret = mDataSet.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            holder = new ViewHolder();
            contentView = inflater.inflate(R.layout.item_moive_grid, null);
            holder.txtTitle = ((TextView) contentView.findViewById(R.id.txtTitle));
            holder.imgHaibao = ((ImageView) contentView.findViewById(R.id.imgHaibao));
            holder.layout_focus = contentView.findViewById(R.id.layout_focus);
            contentView.setTag(holder);
        } else  {
            holder = ((ViewHolder) contentView.getTag());
        }

        VodDetailObj vodInfo = mDataSet.get(position);

        if (vodInfo != null) {
            holder.txtTitle.setText(vodInfo.getMzName());
            Utility.displayImage(vodInfo.getShowUrl(), holder.imgHaibao);
        }

        return contentView;
    }

    public static class ViewHolder {
        public View layout_focus;
        public TextView txtTitle;
        public ImageView imgHaibao;
    }
}
