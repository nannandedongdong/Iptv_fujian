package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.VisitAddress;

import java.util.List;

public class VODPlayerMenuAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    //    private String[] items = {"16:9","4:3","超清","高清","标清"};
    private String[] items = {"16:9", "4:3"};
    private int[] images = {R.drawable.icon_scale, R.drawable.icon_dpi};
    private List<VisitAddress> mDataSet;

    public VODPlayerMenuAdapter(Context context, List<VisitAddress> dataSet) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataSet = dataSet;
    }

    @Override
    public int getCount() {
        int ret = items.length;
        if (mDataSet != null) {
            ret += mDataSet.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        if (position < 2) {
            return items[position];
        } else {
            return mDataSet.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 2) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_vod_player_menu, null);
        }

        TextView tvItem = (TextView) convertView.findViewById(R.id.tv_menu_text);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_menu_icom);
        if (position == 0) {
            ivIcon.setImageResource(images[0]);
        } else if (position == 2) {
            ivIcon.setImageResource(images[1]);
        }
        if (position < 2) {
            tvItem.setText(items[position]);
        } else {
            tvItem.setText(mDataSet.get(position-2).getFormat());
        }

        return convertView;
    }
}
