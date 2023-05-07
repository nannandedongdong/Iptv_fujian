package com.ccdt.ottclient.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccdt.ottclient.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hezb
 * @Package com.ccdt.stb.vision.ui.view
 * @ClassName: GlobalStatusBar
 * @Description: 全局顶部状态条
 * @date 2015年6月5日 下午3:35:42
 */

public class GlobalStatusBar extends RelativeLayout {

    private static final int TIME_REFRESH_INTERVAL = 30*1000;

    private TextView txtTime;
    private TextView mDateText;
    private TextView mWeekText;

    public GlobalStatusBar(Context context) {
        super(context);
        init(context);
    }

    public GlobalStatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GlobalStatusBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.global_status_bar, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDateText = (TextView) findViewById(R.id.date_textview);
        mWeekText = (TextView) findViewById(R.id.week_textview);
        txtTime = ((TextView) findViewById(R.id.txtTime));
        showDateTime();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(TIME_REFRESH_INTERVAL);
                        handler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showDateTime();
        }
    };

    private void showDateTime() {
        String[] time = getNowTime().split(" ");
        mDateText.setText(time[0]);
        mWeekText.setText(time[1]);
        txtTime.setText(time[2]);
    }

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd EEEE HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

}
