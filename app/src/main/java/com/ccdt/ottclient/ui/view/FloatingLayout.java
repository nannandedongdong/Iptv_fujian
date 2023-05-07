package com.ccdt.ottclient.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FloatingLayout extends FrameLayout {

	public FloatingLayout(Context context) {
		this(context, null, 0);
	}

	public FloatingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FloatingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public static class LayoutParams extends FrameLayout.LayoutParams {
		public int x, y;
		public boolean customPosition = false;

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getWidth() {
			return width;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getHeight() {
			return height;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getX() {
			return x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getY() {
			return y;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			ViewGroup.LayoutParams flp = (ViewGroup.LayoutParams) child.getLayoutParams();
			if (flp instanceof LayoutParams) {
				final LayoutParams lp = (LayoutParams) flp;
				if (lp.customPosition) {
					child.layout(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
				}
			}
		}
	}

}
