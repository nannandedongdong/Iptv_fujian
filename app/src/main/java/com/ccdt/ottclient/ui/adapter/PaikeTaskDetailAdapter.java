package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.PaikeInfoObj;
import com.ccdt.ottclient.ui.view.VerticalScrollTextView;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;

public class PaikeTaskDetailAdapter extends BaseAdapter {

    private List<PaikeInfoObj> mDataSet;
    private final LayoutInflater mInflater;


    public PaikeTaskDetailAdapter(Context context, List<PaikeInfoObj> dataSet) {
        mInflater = LayoutInflater.from(context);
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
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_paike_list, parent, false);
            holder = new ViewHolder();
            holder.imgPaike = ((ImageView) convertView.findViewById(R.id.img_paike_choose));
            holder.txtTitle = ((TextView) convertView.findViewById(R.id.txt_paike_choose_title));
            holder.txtDesc = ((VerticalScrollTextView) convertView.findViewById(R.id.txt_paike_choose_desc));
            holder.layout_focus = convertView.findViewById(R.id.layout_focus);
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }

        PaikeInfoObj paikeInfo = mDataSet.get(position);
        if (paikeInfo != null) {
            holder.txtTitle.setText(paikeInfo.getMzName());
//            holder.txtDesc.setText(paikeInfo.getMzName());
            String desc = "初秋、清晨、古城，我们遇见，在那秋日印象中，如影随行、相遇相知而66666666666667777777788888888999999999000000011111111222222233333344444455555";
            holder.txtDesc.setScrollText(desc);
            Utility.displayImage(paikeInfo.getShowUrl(), holder.imgPaike);
        }
        holder.txtDesc.setAutoScroll(false);

        return convertView;
    }


    public static class ViewHolder  {
        public ImageView imgPaike;
        public TextView txtTitle;
        public VerticalScrollTextView txtDesc;
        public View layout_focus;

    }


}
