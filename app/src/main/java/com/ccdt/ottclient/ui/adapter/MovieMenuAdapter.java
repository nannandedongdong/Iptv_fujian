package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.LmInfoObj;

import java.util.List;

public class MovieMenuAdapter extends BaseAdapter {

    private List<LmInfoObj> mDataSet;
    private Context mContext;

    public MovieMenuAdapter(Context context, List<LmInfoObj> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_moive_menu, parent, false);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_menu);
        LmInfoObj lmInfo = mDataSet.get(position);
        if (lmInfo != null) {
            tvTitle.setText(lmInfo.getLmName());
        }
        return convertView;
    }
}