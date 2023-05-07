package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.LmIconObj;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.ui.view.GridLayoutAdapter;
import com.ccdt.ottclient.ui.view.GridLayoutItem;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;

public class VodPageAdapter extends GridLayoutAdapter {

    private Context mContext;

    public VodPageAdapter(Context context, List<GridLayoutItem> list) {
        super(context, list);
        this.mContext = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_vod_list, null);
            holder = new ViewHolder();
            holder.iv_bg = ((ImageView) convertView.findViewById(R.id.iv_bg));
            holder.iv_up = ((ImageView) convertView.findViewById(R.id.iv_up));
            holder.txt_up = ((TextView) convertView.findViewById(R.id.txt_up));
            holder.layout_up = convertView.findViewById(R.id.layout_up);
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }

        setItemData(holder, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }

            }
        });
        return convertView;
    }


    private void setItemData(ViewHolder holder, int position) {
        if (holder != null) {
            VodGridItem gridItem = (VodGridItem) getItem(position);

            String iconUrl = null;
            String backgroundUrl = null;
            String lmName = "";

            LmInfoObj lmInfo = gridItem.lmInfoObj;
            if (lmInfo != null) {
                lmName = lmInfo.getLmName();
                List<LmIconObj> lmIconList = lmInfo.getLmIcon();
                if (lmIconList != null) {
                    for (int i = 0; i < lmIconList.size(); i++) {
                        LmIconObj lmIcon = lmIconList.get(i);
                        if (lmIcon != null) {
                            String signCode = lmIcon.getSignCode();
                            if ("icon".equals(signCode)) {
                                iconUrl = lmIcon.getImageUrl();
                            } else if ("background".equals(signCode)) {
                                backgroundUrl = lmIcon.getImageUrl();
                            }
                        }
                    }
                }
            }

            holder.txt_up.setText(lmName);

            switch (position) {
                case 0:
                case 2:
                case 4:
                case 6:
//                    ImageLoader.getInstance().displayImage(backgroundUrl, holder.iv_bg);
                    Utility.displayImage(backgroundUrl, holder.iv_bg, 0);
                    holder.iv_up.setVisibility(View.INVISIBLE);
                    holder.layout_up.setBackgroundResource(bgArr[position]);
                    String bgColor = "#49B276";
                    switch (position){
                        case 0:
                            // 绿
                            bgColor = "#49B276";
                            break;
                        case 2:
                            // 黄
                            bgColor = "#D7A716";
                            break;
                        case 4:
                            // 红
                            bgColor = "#EF6A3D";
                            break;
                        case 6:
                            // 蓝
                            bgColor = "#2D7EDB";
                            break;
                        default:
                            bgColor = "#49B276";
                            break;
                    }
                    holder.iv_bg.setBackgroundColor(Color.parseColor(bgColor));
                    break;
                case 1:
                case 3:
                case 5:
                case 7:
//                    ImageLoader.getInstance().displayImage(backgroundUrl, holder.iv_up);
                    Utility.displayImage(backgroundUrl, holder.iv_up, 0);
                    holder.iv_up.setVisibility(View.VISIBLE);
                    holder.layout_up.setBackgroundColor(Color.TRANSPARENT);
                    holder.iv_bg.setBackgroundResource(bgArr[position]);
                    break;
            }
        }
    }

    public class ViewHolder {
        ImageView iv_bg;
        ImageView iv_up;
        TextView txt_up;
        View layout_up;
    }

    private int[] bgArr = {
            R.drawable.bg_vod_page_green,
            R.drawable.bg_red,
            R.drawable.bg_vod_page_yellow,
            R.drawable.bg_blue,
            R.drawable.bg_vod_page_red,
            R.drawable.bg_orange,
            R.drawable.bg_vod_page_blue,
            R.drawable.bg_green
    };


    public static class VodGridItem extends GridLayoutItem {
        public int bg;
        public LmInfoObj lmInfoObj;
    }


    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @Override
    public int getNextFocusUpId() {
        return R.id.main_tab_1;
    }
}