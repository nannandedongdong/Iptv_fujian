package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.VisitAddress;

import java.util.List;

/**
 * @Package com.ccdt.stb.movies.ui.adapter
 * @ClassName: RateListAdapter
 * @Description: 多码率列表适配器
 * @author hezb
 * @date 2015年6月23日 上午11:07:01
 */

public class RateListAdapter extends BaseAdapter {
    
    private Context mContext;
    private List<VisitAddress> addressList;
    private LayoutInflater mInflater;
    
    private int selected = 0;//选中
    
    public RateListAdapter(Context context, List<VisitAddress> visitAddress) {
        this.mContext = context;
        this.addressList = visitAddress;
        mInflater = LayoutInflater.from(mContext);
    }
    
    @Override
    public int getCount() {
        return addressList == null ? 0 : addressList.size();
    }

    @Override
    public VisitAddress getItem(int position) {
        return addressList == null ? null : addressList.get(position);
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
            convertView = mInflater.inflate(R.layout.rate_list_item, null);
            holder.rateName = (TextView) convertView.findViewById(R.id.rate_name);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(getItem(position).getFormat())) {
            holder.rateName.setText(getItem(position).getFormat());
        } else {
            holder.rateName.setText(R.string.unknow_rate);
        }
        
        if (selected == position) {
            holder.rateName.setTextColor(mContext.getResources().getColor(R.color.yellow));
        } else {
//            holder.rateName.setTextColor(mContext.getResources().getColor(R.color.text_selected_grey_white_selector));
        }
        
        return convertView;
    }
    
    public void setOnSelectedChange(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }
    
    public int getSelected() {
        return selected;
    }

    class ViewHolder {
        TextView rateName;
    }
}
