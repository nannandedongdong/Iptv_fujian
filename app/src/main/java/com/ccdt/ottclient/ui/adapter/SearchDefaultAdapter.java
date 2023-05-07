package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccdt.ottclient.R;

import java.util.List;

public class SearchDefaultAdapter extends RecyclerView.Adapter<SearchDefaultAdapter.ViewHolder> {

    private List<String> mDataSet;
    private Context mContext;

    public SearchDefaultAdapter(Context context, List<String> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_search_default, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtTitle.setText(mDataSet.get(position));
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClickListener(position);
                }
            }
        });
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
        public TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = ((TextView) itemView.findViewById(R.id.txt_task_title));
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

}
