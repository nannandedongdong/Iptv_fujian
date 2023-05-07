package com.ccdt.ottclient.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.ccdt.ottclient.utils.LogUtil;

public class ObservableScrollView extends HorizontalScrollView {

    private ScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }

//    @Override
//    public void fling(int velocityX) {
//        super.fling(velocityX/4);
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.d("scroll-----keyCode-----" + keyCode);
        return super.onKeyDown(keyCode, event);
    }
}