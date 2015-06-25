package com.family.familyedu.widget;

import com.family.familyedu.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Category extends LinearLayout {
	private static final String TAG = "Category";
	private Context context;

	public Category(Context paramContext) {
		super(paramContext);
		this.context = paramContext;
	}

	public Category(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.context = paramContext;
	}

	private View getDivider() {
		View localView = new View(this.context);
		int i = getResources().getColor(R.color.line);
		int j = (int) this.context.getResources().getDimension(R.dimen.line);
		localView.setBackgroundColor(i);
		localView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, j));
		return localView;
	}

	public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
		if (getChildCount() != 0)
			addView(getDivider());
		int i = (int) this.context.getResources().getDimension(R.dimen.row_height);
		if ((paramLayoutParams.height != LayoutParams.MATCH_PARENT)
				&& (paramLayoutParams.height != LayoutParams.WRAP_CONTENT))
			;
		for (int j = paramLayoutParams.height;; j = i) {
			super.addView(paramView, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, j));
			return;
		}
	}

	public void setBackgroundResource(int paramInt) {
		super.setBackgroundResource(R.drawable.shape_category_bg);
	}

	public void setOrientation(int paramInt) {
		super.setOrientation(VERTICAL);
	}
}