package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.VodRecommendObj;
import com.ccdt.ottclient.utils.Utility;

import java.util.List;

public class VodDetailAdapter extends RecyclerView.Adapter<VodDetailAdapter.ViewHolder> {

    private List<VodRecommendObj> mDataSet;
    private LayoutInflater inflater;
    private Animation bigAnim;

    public VodDetailAdapter(Context context, List<VodRecommendObj> recommendObjs) {
        this.mDataSet = recommendObjs;
        inflater = LayoutInflater.from(context);
        bigAnim = AnimationUtils.loadAnimation(context, R.anim.anim_big_common);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_vod_detail, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        VodRecommendObj recommendObj = mDataSet.get(position);
        if (recommendObj != null) {
            Utility.displayImage(recommendObj.getShowUrl(), holder.imgHaibao);
            holder.txtTitle.setText(recommendObj.getMzName());
        }
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.layout_focus.setBackgroundResource(R.drawable.bg_vod_blue);
                } else {
                    holder.layout_focus.setBackgroundResource(R.drawable.bg_vod_black);
                }
            }
        });

        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (mDataSet != null) {
            ret = mDataSet.size();
        }
        return ret;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout_focus;
        public TextView txtTitle;
        public ImageView imgHaibao;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = ((TextView) itemView.findViewById(R.id.txtTitle));
            imgHaibao = ((ImageView) itemView.findViewById(R.id.imgHaibao));
            layout_focus = itemView.findViewById(R.id.layout_focus);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l){
        this.mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
