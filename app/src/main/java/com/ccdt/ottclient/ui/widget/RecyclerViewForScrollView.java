package com.ccdt.ottclient.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewForScrollView extends RecyclerView {
    public RecyclerViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewForScrollView(Context context, AttributeSet attrs,
                                     int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(expandSpec, heightMeasureSpec);
    }
}
