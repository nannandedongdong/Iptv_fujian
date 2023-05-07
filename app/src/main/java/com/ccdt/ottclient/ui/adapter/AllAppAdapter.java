package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.AppObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iSun on 2015/11/16.
 */
public class AllAppAdapter extends BaseAdapter {
    private List<AppObj> lancherInfos;
    private Context context;
    // private int index = -1;
//	private int[] bgSelector = { R.drawable.dark_no_shadow,
//			R.drawable.blue_no_shadow, R.drawable.green_no_shadow,
//			R.drawable.orange_no_shadow, R.drawable.pink_no_shadow };
//	private int randomTemp = -1;// 随机数缓存，用于避免连续产生2个相同的随机数
//	private int bgSize = bgSelector.length;

    public void setLauncher(List<AppObj> infos) {
        if (infos != null) {
            this.lancherInfos = infos;
        } else {
            this.lancherInfos = new ArrayList();
        }
    }

    public AllAppAdapter(Context context, List<AppObj> infos) {
        this.context = context;
        this.setLauncher(infos);
    }

    @Override
    public int getCount() {
        return lancherInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return lancherInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeData(List<AppObj> dateList) {
        this.lancherInfos = dateList;
        notifyDataSetChanged();
    }

    // public void changeData(ArrayList<AppBean> dataList, int index) {
    // this.lancherInfos = dataList;
    // this.index = index;
    // notifyDataSetChanged();
    // }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(
                R.layout.item_all_app, null);
        ImageView appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
        TextView tvName = (TextView) convertView.findViewById(R.id.app_name);
//		ImageView newApp = (ImageView) convertView.findViewById(R.id.new_app);
        AppObj info = lancherInfos.get(position);
        appIcon.setImageDrawable(info.getIcon());
        tvName.setText(info.getName());
        // if (isANewApp(info)) {
        // newApp.setVisibility(View.VISIBLE);
        // }
//		convertView.setLayoutParams(new GridView.LayoutParams(160, 160));
        // convertView.setBackgroundResource(bgSelector[createRandom(bgSize)]);
        return convertView;
    }

}
