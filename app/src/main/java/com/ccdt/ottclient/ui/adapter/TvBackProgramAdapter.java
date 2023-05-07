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
import com.ccdt.ottclient.model.ProgramObj;
import com.ccdt.ottclient.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TvBackProgramAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private ArrayList<ProgramObj> programmData = new ArrayList<>();
    private final SimpleDateFormat dateFormat;

    public TvBackProgramAdapter(Context context, ArrayList<ProgramObj> programmData) {
        this.programmData = programmData;
        mInflater = LayoutInflater.from(context);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (programmData != null) {
            ret = programmData.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return programmData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_tv_programm, parent, false);
            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProgramObj programObj = programmData.get(position);
        LogUtil.i("节目信息", programObj.toString());
        if (programObj != null) {
            String time = programObj.startTime.split(" ")[1];
            holder.tvTime.setText(time);
            holder.tvName.setText(programObj.epgName);

            try {
                // 节目开始时间
                long startTime = dateFormat.parse(programObj.startTime).getTime();
                // 节目结束时间
                long endTime = dateFormat.parse(programObj.endTime).getTime();
                // 系统当前时间
                long currentTime = System.currentTimeMillis();

                if (currentTime < startTime) {
                    // 未播放
                    holder.imageView.setVisibility(View.GONE);
                } else if (currentTime >= endTime) {
                    if (!TextUtils.isEmpty(programObj.backAddress)) {
                        // 已播放+已录制
                        holder.imageView.setVisibility(View.VISIBLE);
                        holder.imageView.setImageResource(R.drawable.icon_back);
                    } else {
                        // 已播放+未录制
                        holder.imageView.setVisibility(View.GONE);
                    }
                } else {
                    // 正在播放
                    holder.imageView.setImageResource(R.drawable.icon_playing);
                    holder.imageView.setVisibility(View.VISIBLE);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvTime;
        public TextView tvName;
        public ImageView imageView;
    }
}