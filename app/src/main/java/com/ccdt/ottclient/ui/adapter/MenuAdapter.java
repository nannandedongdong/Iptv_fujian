package com.ccdt.ottclient.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccdt.ottclient.R;
import com.ccdt.ottclient.model.LmInfoObj;
import com.ccdt.ottclient.ui.activity.SearchActivity;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context mContext;
    private List<LmInfoObj> mDataSet;

    private static final int MENU_VIEW_TYPE_SEARCH = 1;
    private static final int MENU_VIEW_TYPE_DEFAULT = 2;
    private boolean isFirstInit = true;

    public MenuAdapter(Context context, List<LmInfoObj> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View v = null;

        if (viewType == MENU_VIEW_TYPE_SEARCH) {
            v = inflater.inflate(R.layout.item_news_list_menu_search, parent, false);
        } else if (viewType == MENU_VIEW_TYPE_DEFAULT) {
            v = inflater.inflate(R.layout.item_news_list_menu, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final int viewType = getItemViewType(position);
        if (viewType == MENU_VIEW_TYPE_DEFAULT) {
            holder.txtMenuTitle.setText(mDataSet.get(position - 1).getLmName());
        }


        if (viewType == MENU_VIEW_TYPE_DEFAULT) {
            holder.txtMenuTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position - 1);
                    }
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchActivity.actionStart(mContext);
                }
            });
        }
        if (position == 1 && isFirstInit) {
            isFirstInit = false;
            holder.txtMenuTitle.performClick();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MENU_VIEW_TYPE_SEARCH;
        } else {
            return MENU_VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public int getItemCount() {
        int ret = 1;
        if (mDataSet != null) {
            ret += mDataSet.size();
        }
        return ret;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtMenuTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMenuTitle = ((TextView) itemView.findViewById(R.id.txtMenuTitle));
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

}
