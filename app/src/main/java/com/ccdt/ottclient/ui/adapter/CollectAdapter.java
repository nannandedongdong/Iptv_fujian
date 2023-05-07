package com.ccdt.ottclient.ui.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.CollectionObj;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;

public class CollectAdapter extends BaseAdapter {

    private Context mContext;
    private List<CollectionObj> mDataSet;
    private final LayoutInflater mInflater;



    public CollectAdapter(Context context, List<CollectionObj> dataSet) {
        this.mDataSet = dataSet;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_collected, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = ((TextView) convertView.findViewById(R.id.txtTitle));
            holder.imgHaibao = ((ImageView) convertView.findViewById(R.id.imgHaibao));
            holder.layout_focus = convertView.findViewById(R.id.layout_focus);
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }


        CollectionObj obj = mDataSet.get(position);
        holder.imgHaibao.setScaleType(ImageView.ScaleType.FIT_XY);
        if (obj != null) {
            holder.txtTitle.setText(obj.getMzName());
            String haibaoUrl = null;
            if (!TextUtils.isEmpty(obj.getHaibao()) && obj.getHaibao().startsWith("http")) {
                haibaoUrl = obj.getHaibao();
            }

            if("2".equals(obj.getMzType())){
                holder.imgHaibao.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

            Utility.displayImage(haibaoUrl, holder.imgHaibao);
        }


        return convertView;
    }

    public static class ViewHolder {
        public View layout_focus;
        public TextView txtTitle;
        public ImageView imgHaibao;
    }


}
