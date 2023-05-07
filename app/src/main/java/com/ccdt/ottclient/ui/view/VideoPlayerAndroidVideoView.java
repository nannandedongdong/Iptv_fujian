package com.ccdt.ottclient.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.ccdt.ottclient.MyApp;

/*
 * 重写onMeasure方法使视频画幅比适应videoview宽高
 */
public class VideoPlayerAndroidVideoView extends VideoView {

	public VideoPlayerAndroidVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width,height);
	}



}
