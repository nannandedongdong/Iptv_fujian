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
import com.ccdt.ottclient.model.PlayHistoryObj;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;


public class PlayHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<PlayHistoryObj> mDataSet;
    private final LayoutInflater mInflater;

    public PlayHistoryAdapter(Context context, List<PlayHistoryObj> dataSet) {
        this.mDataSet = dataSet;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }



//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        PlayHistoryObj obj = mDataSet.get(position);
//        if (obj != null) {
//            holder.txtTitle.setText(obj.getTitle());
//
//            String haibaoUrl = null;
//
//            if (!TextUtils.isEmpty(obj.getPosterUrl()) && obj.getPosterUrl().startsWith("http")) {
//                haibaoUrl = obj.getPosterUrl();
//            }
//
//            Utility.displayImage(haibaoUrl, holder.imgHaibao);
//        }
//
//        if (isFirstInit && position == 0) {
//            holder.itemView.requestFocus();
//            zoomIn(holder);
//            isFirstInit = false;
//        }
//
//        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    zoomIn(holder);
//                } else {
//                    v.clearAnimation();
//                    holder.layout_focus.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(position);
//                }
//            }
//        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mOnItemLongClickListener.onItemLongClick(position);
//                return true;
//            }
//        });
//    }
//
//    private void zoomIn(ViewHolder holder) {
//        holder.itemView.bringToFront();
//        holder.itemView.requestLayout();
//        holder.itemView.invalidate();
//        holder.layout_focus.setVisibility(View.VISIBLE);
//        holder.itemView.startAnimation(bigAnim);
//    }



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
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_collected, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = ((TextView) convertView.findViewById(R.id.txtTitle));
            holder.imgHaibao = ((ImageView) convertView.findViewById(R.id.imgHaibao));
            holder.layout_focus = convertView.findViewById(R.id.layout_focus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        PlayHistoryObj obj = mDataSet.get(position);
        if (obj != null) {
            holder.txtTitle.setText(obj.getTitle());

            String haibaoUrl = null;

            if (!TextUtils.isEmpty(obj.getPosterUrl()) && obj.getPosterUrl().startsWith("http")) {
                haibaoUrl = obj.getPosterUrl();
            }

            Utility.displayImage(haibaoUrl, holder.imgHaibao);
        }


        return convertView;
    }

    public static class ViewHolder  {

        public View layout_focus;
        public TextView txtTitle;
        public ImageView imgHaibao;

    }



}

