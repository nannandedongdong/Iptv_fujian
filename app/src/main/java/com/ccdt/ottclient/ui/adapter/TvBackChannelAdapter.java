package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.ChannelObj;

import java.util.ArrayList;

public class TvBackChannelAdapter extends BaseAdapter {


    private final LayoutInflater mInflater;
    private ArrayList<ChannelObj> channelData = new ArrayList<>();

    public TvBackChannelAdapter(Context context, ArrayList<ChannelObj> channelData) {
        this.channelData = channelData;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (channelData != null) {
            ret = channelData.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return channelData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item__tv_channel, null);
        }
        TextView tvChannel = (TextView) convertView.findViewById(R.id.tv_channel_name);
        String index = String.valueOf(position + 1);
        if (index.length() == 1) {
            index = String.format("00%s", index);
        } else if(index.length() == 2){
            index = String.format("0%s", index);
        }
        tvChannel.setText(String.format("%s   %s", index, channelData.get(position).getMzName()));
        return convertView;
    }

}