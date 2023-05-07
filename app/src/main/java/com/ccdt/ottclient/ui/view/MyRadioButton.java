package com.ccdt.ottclient.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.ccdt.ottclient.R;

/**
 * Created by hanyue on 2015/12/25.
 */
public class MyRadioButton extends RadioButton {
    private String weekText;
    private Paint myPaint;
    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        myPaint = new Paint();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton);
        weekText = ta.getString(R.styleable.MyRadioButton_weekText);
        myPaint.setTextSize(20);
        myPaint.setColor(R.color.white);
        ta.recycle();
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!TextUtils.isEmpty(weekText)){
            myPaint.setColor(Color.WHITE);
            myPaint.setTextAlign(Paint.Align.LEFT);
            Rect bounds = new Rect();
            myPaint.getTextBounds(weekText, 0, weekText.length(), bounds);
            canvas.drawText(weekText, getMeasuredWidth() / 2 - bounds.width() / 2, (getMeasuredHeight()/2)-6, myPaint);
        }
        super.onDraw(canvas);
    }

    public void setWeekText(String s){
        weekText = s;
        invalidate();
    }
}
