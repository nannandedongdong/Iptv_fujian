package com.ccdt.ottclient.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.ccdt.ottclient.utils.LogUtil;

public class MovieDetailLinearLayout extends LinearLayout {

    private static final String TAG = MovieDetailLinearLayout.class.getName();

    private Scroller mScroller = null; //Scroller对象实例

    private Context mContext;

    public MovieDetailLinearLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MovieDetailLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MovieDetailLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mScroller = new Scroller(mContext);
    }

    public Scroller getScroller() {
        return mScroller;
    }

    // 由父视图调用用来请求子视图根据偏移值 mScrollX,mScrollY重新绘制
    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        Log.d(TAG, "computeScroll");
        // 如果返回true，表示动画还没有结束
        // 因为前面startScroll，所以只有在startScroll完成时 才会为false
        if (mScroller.computeScrollOffset()) {
            LogUtil.e(TAG, mScroller.getCurrX() + "======" + mScroller.getCurrY());
            // 产生了动画效果，根据当前值 每次滚动一点
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            LogUtil.e(TAG, "### getleft is " + getLeft() + " ### getRight is " + getRight());
            //此时同样也需要刷新View ，否则效果可能有误差
            postInvalidate();
        } else
            LogUtil.i(TAG, "have done the scoller -----");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {

        LogUtil.i("startScroll-startScroll-" + getWidth() + " dx - " + dx);
        mScroller.startScroll(startX, startY, dx, dy, duration);
//        computeScroll();
        postInvalidate();
    }


    public void startScroll(int startX, int startY, int dx, int dy) {
        LogUtil.i("startScroll-startScroll-" + getWidth() + " dx - " + dx);
        mScroller.startScroll(startX, startY, dx, dy);
//        computeScroll();
        postInvalidate();
    }
}
