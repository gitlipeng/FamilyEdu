package com.family.familyedu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.family.familyedu.R;

public class PullScrollView extends ScrollView {

	private static final int SCROLL_DURATION = 400;// 滚动持续时间

	private static final float OFFSET_RADIO = 1.8F;

	private boolean initView = false;

	private LinearLayout mUserCenterBodyLayout;

	private Scroller mScroller;

	private float mLastY = -1.0F;

	private RelativeLayout.LayoutParams localLayoutParams;

	private ImageView mBackImageView;

	public PullScrollView(Context context) {
		super(context);
		this.mScroller = new Scroller(context, new DecelerateInterpolator());
	}

	public PullScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mScroller = new Scroller(context, new DecelerateInterpolator());
	}

	private void initView() {
		mUserCenterBodyLayout = (LinearLayout) findViewById(R.id.usercenter_body);
		mBackImageView = (ImageView) findViewById(R.id.backimage);
		localLayoutParams = (RelativeLayout.LayoutParams) this.mUserCenterBodyLayout.getLayoutParams();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
//		Log.i("msg", "onLayout");
		if (!this.initView) {
			this.initView = true;
			initView();
		}
	}

	private float moveYDistance = 0;

	private float startY = 0;

	private float endY = 0;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev){
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				endY = ev.getRawY();
//				Log.i("msg", "startY："+ startY + "endY:"  + endY);
				moveYDistance = Math.abs(endY - startY);
				if (moveYDistance < 15) {
//					Log.i("msg", "ACTION_MOVE小于15");
					return false;
				}
//				Log.i("msg", "ACTION_MOVE大于15");
				break;
		}
		boolean result = super.onInterceptTouchEvent(ev);
		return result;
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		if (this.mLastY == -1.0F)
			this.mLastY = paramMotionEvent.getRawY();// 第一次，获取Y的位置
		switch (paramMotionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
//				Log.i("msg", "ACTION_DOWN");
				this.mLastY = paramMotionEvent.getRawY();// 按下手指，获取当前Y位置
				break;
			case MotionEvent.ACTION_MOVE:
//				 Log.i("msg", "ACTION_MOVE");
				float f = paramMotionEvent.getRawY() - this.mLastY;// f>0：向下滑动，f<0：向上滑动
				this.mLastY = paramMotionEvent.getRawY();

				/** 更新头部高度以及箭头样式 */
				if (isNeedMove() && (f > 0 || localLayoutParams.topMargin > 0)) {
					// Log.i("msg", mLastY + "");
					if (localLayoutParams.topMargin + 60 < mBackImageView.getHeight() / OFFSET_RADIO || f < 0) {
						updateHeaderHeight(f / OFFSET_RADIO);
						return true;
					}
				}
				break;
			default:
				// 松开手后
				if (localLayoutParams.topMargin > 0) {
					resetHeaderHeight();
				}
				this.mLastY = -1.0F;
				break;
		}
		return super.onTouchEvent(paramMotionEvent);
	}

	private void updateHeaderHeight(float paramFloat) {
		int paramInt = (int) paramFloat + localLayoutParams.topMargin;
		if (paramInt < 0) {
			paramInt = 0;
		}
		localLayoutParams.topMargin = paramInt;
		this.mUserCenterBodyLayout.setLayoutParams(localLayoutParams);
		invalidate();
	}

	private void resetHeaderHeight() {
		int i = localLayoutParams.topMargin;
		if (i > 0) {
			this.mScroller.startScroll(0, i, 0, -i, SCROLL_DURATION);
			invalidate();
		}
	}

	public void computeScroll() {
		if (this.mScroller.computeScrollOffset()) {
			localLayoutParams.topMargin = this.mScroller.getCurrY();
			this.mUserCenterBodyLayout.setLayoutParams(localLayoutParams);
		}
		postInvalidate();
		super.computeScroll();
	}

	// 是否需要移动布局
	public boolean isNeedMove() {
		int offset = this.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}

}