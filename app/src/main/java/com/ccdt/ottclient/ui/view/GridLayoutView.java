package com.ccdt.ottclient.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdt.ottclient.R;


/**
 * @author hezb
 * @Description: 修改盒子主页网格布局效果
 * @date 2015年6月17日 下午9:25:31
 */
public class GridLayoutView extends TvHorizontalScrollView implements
        OnFocusChangeListener {

    private static final String TAG = GridLayoutView.class.getSimpleName();

    private Context context;
    private GridLayoutAdapterInterface gridAdapter;
    private GridLayout gridLayout;

    private static final float SCALE_FROM = 1.0f;
    private static final float SCALE_TO = 1.05f;
    private static final int DURATION = 150;
    private ScaleAnimation focusAnimation;
    private ScaleAnimation unFocusAnimation;
    private static final float SCALE_BIG_TO = 1.05f;//大海报的放大
    private ScaleAnimation bigFA;
    private ScaleAnimation bigUnFA;
    private ImageView focusView;

    private int start_row;
    private int start_column;
    private int rowSize;
    private int columnSize;
    private int onecolumn;
    private int twocolumn;
    private int mOver;

    public GridLayoutView(Context context) {
        this(context, null);

    }

    public GridLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public GridLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.context = context;
        mOver = context.getResources().getDimensionPixelOffset(R.dimen.px33);
        ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.grid_layout, this, true);
        gridLayout = (GridLayout) findViewById(R.id.homepage_gridLayout);

        focusView = (ImageView) findViewById(R.id.focus_imageView);

        focusAnimation = new ScaleAnimation(SCALE_FROM, SCALE_TO,
                SCALE_FROM, SCALE_TO, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        focusAnimation.setDuration(DURATION);
        focusAnimation.setFillAfter(true);
        unFocusAnimation = new ScaleAnimation(SCALE_TO, SCALE_FROM, SCALE_TO,
                SCALE_FROM, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        unFocusAnimation.setDuration(DURATION);
        unFocusAnimation.setFillAfter(false);
        bigFA = new ScaleAnimation(SCALE_FROM, SCALE_BIG_TO,
                SCALE_FROM, SCALE_BIG_TO, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        bigFA.setDuration(DURATION);
        bigFA.setFillAfter(true);
        bigUnFA = new ScaleAnimation(SCALE_BIG_TO, SCALE_FROM, SCALE_BIG_TO,
                SCALE_FROM, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        bigUnFA.setDuration(DURATION);
        bigUnFA.setFillAfter(false);

    }

    public void setGridLayoutViewAdapter(
            final GridLayoutAdapterInterface gridLayoutAdapter) {
        gridAdapter = gridLayoutAdapter;
        if (gridAdapter != null) {
            if (gridAdapter instanceof BaseAdapter) {
                final Adapter adapter = (Adapter) gridAdapter;
                adapter.registerDataSetObserver(new DataSetObserver() {

                    @Override
                    public void onChanged() {
                        updateUI(adapter);
                        super.onChanged();
                    }

                    @Override
                    public void onInvalidated() {
                        updateUI(adapter);
                        super.onInvalidated();
                    }
                });
                updateUI(adapter);
            }
        }
    }

    @SuppressLint("NewApi")
    private void updateUI(Adapter adapter) {

        start_row = 1;
        start_column = 1;

        onecolumn = 1;
        twocolumn = 1;

        for (int position = 0; position < adapter.getCount(); position++) {
            View child = adapter.getView(position, null, this);
            child.setOnFocusChangeListener(this);
            int row = calculateStartPose(gridAdapter.getWidthSize(position),
                    gridAdapter.getHeightSize(position));
            // 设置向下焦点返回下标题
            if (gridAdapter.getNextFocusDownId() != 0) {
                if (row == 2 || gridAdapter.getHeightSize(position) >= 2) {
                    child.setNextFocusDownId(gridAdapter.getNextFocusDownId());
                }
                // 设置向上焦点返回上标题
            } else if (gridAdapter.getNextFocusUpId() != 0) {
                if (row == 1) {
                    child.setNextFocusUpId(gridAdapter.getNextFocusUpId());
                }
            }


            child.setLayoutParams(getViewParams(position, gridAdapter));
            gridLayout.addView(child);
            calculateEndPose();
        }
        //左右间距
        TextView textView = new TextView(context);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params = new GridLayout.LayoutParams();
        params.height = 40;
        params.width = 100;
        textView.setFocusable(false);
        params.rowSpec = GridLayout.spec(0, 1);
        params.columnSpec = GridLayout.spec(0, 1);
        textView.setLayoutParams(params);
        gridLayout.addView(textView);

        textView = new TextView(context);
        params = new GridLayout.LayoutParams();
        params.height = 20;
        params.width = 80;
        textView.setFocusable(false);
        params.rowSpec = GridLayout.spec(gridLayout.getRowCount(), 1);
        params.columnSpec = GridLayout.spec(gridLayout.getColumnCount(), 1);
        textView.setLayoutParams(params);
        gridLayout.addView(textView);
//
        gridLayout.getChildAt(0).requestFocus();

    }

    int preScrollX = 0;

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return super.computeScrollDeltaToGetChildRectOnScreen(rect);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        System.gc();// 提示系统回收垃圾

        if (mchildFocusChangeListener != null) {
            mchildFocusChangeListener.onChildFocusChange(v, hasFocus);
        }

        if (hasFocus) {

            int shadowWith = (int) (v.getWidth() + mOver * 2);
            int shadowHeight = (int) (v.getHeight() + mOver * 2);
            FloatingLayout.LayoutParams focusParams = new FloatingLayout.LayoutParams(shadowWith, shadowHeight);
            focusParams.setX((int) (v.getX() - mOver));
            focusParams.setY((int) (v.getY() - mOver));
            focusParams.customPosition = true;
            focusView.setLayoutParams(focusParams);

            v.destroyDrawingCache();
            v.setDrawingCacheEnabled(true);
            if (v.getDrawingCache() != null) {
                Bitmap cache = Bitmap.createBitmap(v.getDrawingCache());
                v.setDrawingCacheEnabled(false);
                focusView.setImageBitmap(cache);

                focusView.setVisibility(View.VISIBLE);
                if (v.getHeight() >= 424) { // 大于等于424
                    focusView.startAnimation(bigFA);
                } else {
                    focusView.startAnimation(focusAnimation);
                }
            }

        } else {

            focusView.setVisibility(View.GONE);
            focusView.startAnimation(unFocusAnimation);
            if (v.getHeight() >= 424) { // 大于等于424
                focusView.startAnimation(bigUnFA);
            } else {
                focusView.startAnimation(unFocusAnimation);
            }
        }

    }

    @SuppressLint("NewApi")
    private GridLayout.LayoutParams getViewParams(int position,
                                                  GridLayoutAdapterInterface gridAdapter) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rightMargin = gridAdapter.getRightMargin(position);
        params.bottomMargin = gridAdapter.getBottomMargin(position);
        params.width = gridAdapter.getWidth(position);
        params.height = gridAdapter.getHeight(position);

        params.rowSpec = GridLayout.spec(start_row, rowSize);
        params.columnSpec = GridLayout.spec(start_column, columnSize);
        params.width = params.width * columnSize;
        params.height = params.height * rowSize;
        params.setGravity(Gravity.FILL);
        return params;
    }

    private int calculateStartPose(int widthSize, int heightSize) {
        columnSize = widthSize;
        rowSize = heightSize;

        if (rowSize >= 2) {
            rowSize = 2;
            if (start_row == 2) {
                start_row = 1;
            } else {
                if (twocolumn > onecolumn) {
                    onecolumn = twocolumn;
                    start_row = 1;
                }
            }
        } else {
            if (start_row == 2 && onecolumn == twocolumn) {
                start_row = 1;
            }
        }

        if (start_row == 1) {
            start_column = onecolumn;
        }

        if (start_row == 2) {
            start_column = twocolumn;
        }
        return start_row;
    }

    private void calculateEndPose() {
        if (start_row == 1) {
            onecolumn += columnSize;
        } else if (start_row == 2) {
            twocolumn += columnSize;
        }

        if (rowSize >= 2) {
            start_row = 1;
            if (twocolumn > onecolumn) {
                onecolumn = twocolumn;
            } else {
                twocolumn = onecolumn;
            }
        } else {
            if (start_row == 2) {
                if (twocolumn - onecolumn >= 0) {
                    start_row = 1;
                }
            } else {
                if (onecolumn - twocolumn >= 0) {
                    start_row = 2;
                }
            }
        }
    }

    private OnChildFocusChangeListener mchildFocusChangeListener;

    public void setChildFocusChangeListener(
            OnChildFocusChangeListener mchildFocusChangeListener) {
        this.mchildFocusChangeListener = mchildFocusChangeListener;
    }

    public interface OnChildFocusChangeListener {
        void onChildFocusChange(View v, boolean hasFocus);
    }

}
