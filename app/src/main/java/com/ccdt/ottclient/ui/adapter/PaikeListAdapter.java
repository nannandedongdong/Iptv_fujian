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

public class PaikeListAdapter extends BaseAdapter {

    Context mContext;
//    private int mScreenWidth;
    private LayoutInflater inflater;
    private List<PaikeInfoObj> mDataSet;

    public PaikeListAdapter(Context context, List<PaikeInfoObj> dataSet) {
        mContext = context;
        this.mDataSet = dataSet;
        this.inflater = LayoutInflater.from(context);
//        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        WindowManager mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = mWm.getDefaultDisplay();
//        DisplayMetrics dm = new DisplayMetrics();
//        display.getMetrics(dm);
//        mScreenWidth = dm.widthPixels;
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
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_paike_list, parent, false);
            holder = new ViewHolder();
            holder.imgPaike = ((ImageView) contentView.findViewById(R.id.img_paike_choose));
            holder.txtTitle = ((TextView) contentView.findViewById(R.id.txt_paike_choose_title));
            holder.txtDesc = ((VerticalScrollTextView) contentView.findViewById(R.id.txt_paike_choose_desc));
            holder.layout_focus = contentView.findViewById(R.id.layout_focus);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        PaikeInfoObj paikeInfo = mDataSet.get(position);
        if (paikeInfo != null) {
            holder.txtTitle.setText(paikeInfo.getMzName());
//            holder.txtDesc.setText(paikeInfo.getMzName());
//            String desc = "初秋、清晨、古城，我们遇见，在那秋日印象中，如影随行、相遇相知而66666666666667777777788888888999999999000000011111111222222233333344444455555";
//            holder.txtDesc.setScrollText(desc);
            Utility.displayImage(paikeInfo.getShowUrl(), holder.imgPaike);
        }
        return contentView;
    }

    public static class ViewHolder {
        public ImageView imgPaike;
        public TextView txtTitle;
        public View layout_focus;
        public VerticalScrollTextView txtDesc;
    }
}


