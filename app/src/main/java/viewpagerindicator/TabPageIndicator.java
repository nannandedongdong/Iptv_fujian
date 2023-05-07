package viewpagerindicator;

/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccdt.ottclient.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements
		PageIndicator {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";

	/**
	 * Interface for a callback when the selected tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 * 
		 * @param position
		 *            Position of the current center item.
		 */
		void onTabReselected(int position);
	}

	public interface OnTabFocusChangeListener {
		void onFocusChangeListener(TabView tab, boolean hasFocus);
	}

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			TabView tabView = (TabView) view;
			final int oldSelected = mViewPager.getCurrentItem();
			final int newSelected = tabView.getIndex();
			mViewPager.setCurrentItem(newSelected);
			if (oldSelected == newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(newSelected);
			}
		}
	};

	private final OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			hasFocusIndicator = hasFocus;
			if (v instanceof TabView) {
				TabView tab = (TabView) v;
				if (hasFocus) {
					tab.setTextSize(28);
					setCurrentItem(tab.mIndex);
				}else {
					tab.setTextSize(28);
				}
				if (mTabFocusChangeListener != null) {
					mTabFocusChangeListener.onFocusChangeListener(tab, hasFocus);
				}
			}
		}
	};

	private boolean hasFocusIndicator = false;

	public boolean hasFocusIndicator(){
		return this.hasFocusIndicator;
	}

	private final IcsLinearLayout mTabLayout;

	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mSelectedTabIndex;
	private int mSelectedTabId;

	public int getSelectedTabId() {
		return mSelectedTabId;
	}

	private OnTabReselectedListener mTabReselectedListener;
	private OnTabFocusChangeListener mTabFocusChangeListener;

	public TabPageIndicator(Context context) {
		this(context, null);
	}

	public TabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);

		mTabLayout = new IcsLinearLayout(context,
				R.attr.vpiTabPageIndicatorStyle);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,
				MATCH_PARENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	public void setOnTabFocusChangeListener(OnTabFocusChangeListener listener) {
		mTabFocusChangeListener = listener;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	private void addTab(int index, CharSequence text, int iconResId, int tabId) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setOnFocusChangeListener(mFocusChangeListener);
		tabView.setText(text);

		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0);
		}


		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				getTabViewLayoutWith(), MATCH_PARENT, 1);
		params.setMargins(20,10,20,10);
		mTabLayout.addView(tabView, params);
		tabView.setId(tabId);
//		tabView.setNextFocusDownId(R.id.gridlayoutmetro_item_first);
	}

	protected int getTabViewLayoutWith() {
		return 1;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	public static int onPagePosition = 0;//用于记录主页的当前页面
	@Override
	public void onPageSelected(int arg0) {
		setCurrentItem(arg0);
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
		//TODO
		pauseHomeVideoView(arg0);
		onPagePosition = arg0;
	}

	@Override
	public void setViewPager(ViewPager view) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		final PagerAdapter adapter = view.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		view.setOnPageChangeListener(this);
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		PagerAdapter adapter = mViewPager.getAdapter();
		IconPagerAdapter iconAdapter = null;
		if (adapter instanceof IconPagerAdapter) {
			iconAdapter = (IconPagerAdapter) adapter;
		}
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			CharSequence title = adapter.getPageTitle(i);
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			if (iconAdapter != null) {
				iconResId = iconAdapter.getIconResId(i);
			}
			int tabId = i;
			if (adapter instanceof AdapterWithTabId) {
				AdapterWithTabId awt = (AdapterWithTabId) adapter;
				tabId = awt.getTabId(i);
			}
			addTab(i, title, iconResId, tabId);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	@Override
	public void setCurrentItem(int item) {

		if (mViewPager == null) {
			return;
			// throw new IllegalStateException("ViewPager has not been bound.");
		}
		mSelectedTabIndex = item;
		mViewPager.setCurrentItem(item);

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				mSelectedTabId = child.getId();
				animateToTab(item);
			}
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}

	public class TabView extends TextView {
		private int mIndex;

		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPageIndicatorStyle);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}
	}

	/**
	 * the viewpager's adapter implements this interface so that each tab view
	 * has a view id
	 */
	public interface AdapterWithTabId {
		public int getTabId(int position);
	};
	
	/**
	 * 暂停主页视频
	 */
	private void pauseHomeVideoView(int position){
//
//		try {
//			
//	
//		if(position!=1){
//			if(TVLiveFragmentSTB.onPrepared && TVLiveFragmentSTB.mMediaPlayer.isPlaying()){
//				TVLiveFragmentSTB.mMediaPlayer.pause();
//				TVLiveFragmentSTB.homeSurfaceView.setVisibility(View.GONE);
//			}
//		}else{
//			TVLiveFragmentSTB.homeSurfaceView.setVisibility(View.VISIBLE);
//			if(!TVLiveFragmentSTB.onPrepared){
//				Log.d("hzb", "获取视频失败，尝试重新获取");
//				TVLiveFragmentSTB.mMediaPlayer.prepareAsync();
//			}else{
//				TVLiveFragmentSTB.mMediaPlayer.start();
//			}
//		}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
	}
}
