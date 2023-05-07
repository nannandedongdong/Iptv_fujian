package com.ccdt.ottclient.ui.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.DigitalClock;

import java.util.Calendar;

public class DigitalClockView extends DigitalClock {

	public DigitalClockView(Context context) {
		super(context);
		initClock(context);
	}

	Calendar mCalendar;

	private final static String SYSTEM_TIME_FORMAT = "kk:mm";
	private FormatChangeObserver mFormatChangeObserver;

	private Runnable mTicker;
	private Handler mHandler;

	private boolean mTickerStopped = false;

	String mFormat;

	public DigitalClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initClock(context);
	}

	private void initClock(Context context) {

		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}

		mFormatChangeObserver = new FormatChangeObserver();
		getContext().getContentResolver().registerContentObserver(
				Settings.System.CONTENT_URI, true, mFormatChangeObserver);

	}

	@Override
	protected void onAttachedToWindow() {
		mTickerStopped = false;
		super.onAttachedToWindow();
		mHandler = new Handler();

		mTicker = new Runnable() {
			public void run() {
				if (mTickerStopped)
					return;
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				setText(DateFormat.format(SYSTEM_TIME_FORMAT, mCalendar));
				invalidate();
				long now = SystemClock.uptimeMillis();
				long next = now + (1000 - now % 1000);
				mHandler.postAtTime(mTicker, next);
			}
		};
		new Thread(mTicker).start();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mTickerStopped = true;
	}

	private class FormatChangeObserver extends ContentObserver {
		public FormatChangeObserver() {
			super(new Handler());
		}

	}
}
