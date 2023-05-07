package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ccdt.ottclient.R;

public class NewsMainRightAdapter extends BaseAdapter {

    private Context mContext;

    public NewsMainRightAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_news_main_right, parent, false);

        if (v != null) {

        }

        return v;
    }
}
