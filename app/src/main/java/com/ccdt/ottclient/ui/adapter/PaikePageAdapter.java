package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.PaikeInfoObj;
import com.ccdt.ottclient.ui.view.GridLayoutAdapter;
import com.ccdt.ottclient.ui.view.GridLayoutItem;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;

public class PaikePageAdapter extends GridLayoutAdapter {

    private static final String TAG = "PaikePageAdapter";

    public static final int VIEW_TYPE_CHOICENESS = 0;
    public static final int VIEW_TYPE_TASK = 1;
    public static final int VIEW_TYPE_COMMON = 2;

    private int mIndicatorTabId;

    public int getmIndicatorTabId() {
        return mIndicatorTabId;
    }

    public void setmIndicatorTabId(int mIndicatorTabId) {
        this.mIndicatorTabId = mIndicatorTabId;
    }

    private Context mContext;
    private Handler mHandler = new Handler();

    public PaikePageAdapter(Context context, List<GridLayoutItem> list) {
        super(context,list);
        this.mContext = context;
        this.list = list;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       final int viewType =  getItemViewType(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder = null;
        if (convertView == null){
            if (viewType == VIEW_TYPE_CHOICENESS || viewType == VIEW_TYPE_TASK) {
                convertView = inflater.inflate(R.layout.item_paike_main_head, parent, false);
            } else if (viewType == VIEW_TYPE_COMMON) {
                convertView = inflater.inflate(R.layout.item_paike_main, parent, false);
            }
            holder = new ViewHolder(convertView);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (viewType == VIEW_TYPE_CHOICENESS) {
            // 拍客精选入口
            holder.img.setImageResource(R.drawable.ic_paike_choiceness);
            convertView.setId(R.id.paike_page_item_choose);
        } else if (viewType == VIEW_TYPE_TASK) {
            // 拍客任务入口
            holder.img.setImageResource(R.drawable.ic_paike_task);
            convertView.setId(R.id.paike_page_item_task);
        } else {
            // 拍客精选内容
            PaikeInfoObj paikeInfoObj = ((PaikeGridItem) list.get(position)).linfoObj;
            if (paikeInfoObj != null) {
                Utility.displayImage(paikeInfoObj.getShowUrl(), holder.img);
                holder.title.setText(paikeInfoObj.getMzName());
                holder.num.setText(paikeInfoObj.getCollectNum());
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(viewType,position);
                }

            }
        });


        if (isFirstRow(position)) {
            if (mIndicatorTabId > 0) {
                convertView.setNextFocusUpId(mIndicatorTabId);
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_CHOICENESS;
        } else if (position == 1) {
            return VIEW_TYPE_TASK;
        } else {
            return VIEW_TYPE_COMMON;
        }
    }


    public class ViewHolder  {
        public ImageView img;
        public ImageView icon;
        public TextView title;
        public TextView num;

        public ViewHolder(View itemView) {
            img = ((ImageView) itemView.findViewById(R.id.item_paike_main_img));
            icon = ((ImageView) itemView.findViewById(R.id.item_paike_main_collected_icon));
            title = ((TextView) itemView.findViewById(R.id.item_paike_main_title));
            num = ((TextView) itemView.findViewById(R.id.item_paike_main_collected_count));
        }
    }
    public static class PaikeGridItem extends GridLayoutItem {
        public int bg;
        public PaikeInfoObj linfoObj;
    }
    public interface OnItemClickListener {
        void onItemClick(int viewType, int position);
    }

    private boolean isFirstRow(int position) {
        return position % 2 == 0;
    }

}
