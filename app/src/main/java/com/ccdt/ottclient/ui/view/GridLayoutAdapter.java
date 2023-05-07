package com.ccdt.ottclient.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class GridLayoutAdapter extends BaseAdapter implements
		GridLayoutAdapterInterface {

    protected List<GridLayoutItem> list;
    protected LayoutInflater layoutInflater;

    public GridLayoutAdapter(Context context, List<GridLayoutItem> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getWidth(int position) {
        if (list.get(position).getWidth() == 0) {
            return DEFAULT_WIDTH;
        }
        return list.get(position).getWidth();
    }

    @Override
    public int getHeight(int position) {
        if (list.get(position).getHeight() == 0) {
            return DEFAULT_HEIGHT;
        }
        return list.get(position).getHeight();
    }

    @Override
    public int getWidthSize(int position) {
        if (list.get(position).getWidthSize() == 0) {
            return DEFAULT_WIDTHSIZE;
        }
        return list.get(position).getWidthSize();
    }

    @Override
    public int getHeightSize(int position) {
        if (list.get(position).getHeightSize() == 0) {
            return DEFAULT_HEIGHTSIZE;
        }
        return list.get(position).getHeightSize();
    }

    @Override
    public int getRightMargin(int position) {
        if (list.get(position).getRightMargin() == 0) {
            return DEFAULT_RIGHTMARGIN;
        }
        return list.get(position).getRightMargin();
    }

    @Override
    public int getBottomMargin(int position) {
        if (list.get(position).getBottomMargin() == 0) {
            return DEFAULT_BOTTOMMARGIN;
        }
        return list.get(position).getBottomMargin();
    }

    @Override
    public int getNextFocusUpId() {
        return 0;
    }
    
    @Override
    public int getNextFocusDownId() {
        return 0;
    }

}
