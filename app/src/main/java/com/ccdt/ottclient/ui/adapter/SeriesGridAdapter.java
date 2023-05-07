package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.JuJiInfo;

import java.util.List;

/**
 * @author hezb
 * @Package com.ccdt.stb.vision.ui.adapter
 * @ClassName: SeriesGridAdapter
 * @Description: 选集Adapter
 * @date 2015年6月10日 下午2:25:14
 */

public class SeriesGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private int seriesItem = -1;
    private List<JuJiInfo> juJiInfos;

    public SeriesGridAdapter(Context context,
                             List<JuJiInfo> juJiInfos, int seriesItem) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.seriesItem = seriesItem;
        this.juJiInfos = juJiInfos;
    }

    @Override
    public int getCount() {
        return juJiInfos == null ? 0 : juJiInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return juJiInfos == null ? null : juJiInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_series, null);
            holder.textview = (TextView) convertView
                    .findViewById(R.id.vod_video_detail_series_item_text);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textview.setText(juJiInfos.get(position).getChapter());

        if (seriesItem == juJiInfos.get(position).juJiNum) {
            holder.textview.setTextColor(context.getResources().getColor(R.color.color_942529));
        } else {
            holder.textview.setTextColor(context.getResources()
                    .getColor(R.color.white));
        }
        return convertView;
    }

    /**
     * 改变选中剧集
     */
    public void setSeriesItem(int seriesItem) {
        this.seriesItem = seriesItem;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView textview;
    }
}