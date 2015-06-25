package com.family.familyedu.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.familyedu.BaseApplication;
import com.family.familyedu.R;

public class BaseFragment extends Fragment implements OnClickListener{
	protected View baseView = null;
	protected RelativeLayout mainView = null;
	protected RelativeLayout titleView = null;
	protected View currentView = null;
	protected RelativeLayout.LayoutParams mathLayoutParams = new RelativeLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	private ProgressDialog pd;
	
	private ProgressBar progressBar;
	
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.baseView = paramLayoutInflater.inflate(R.layout.f_base, null);
		this.titleView = ((RelativeLayout) this.baseView
				.findViewById(R.id.baseFragTop));
		this.mainView = ((RelativeLayout) this.baseView
				.findViewById(R.id.baseFragMain));
		progressBar = (ProgressBar)baseView.findViewById(R.id.progressBar);
		return baseView;
	}

	protected void setTitleText(String paramString) {
		if ((paramString == null) || (paramString.equals("")))
			return;
		TextView localTextView = (TextView) baseView.findViewById(
				R.id.txtBaseTitle);
		localTextView.setText(paramString);
		if (this.titleView.getVisibility() != 0)
			this.titleView.setVisibility(0);
		localTextView.setVisibility(0);
	}

	protected void setTopLeftButton(String paramString, int paramInt,
			View.OnClickListener paramOnClickListener) {
		if (this.titleView == null)
			return;
		ImageButton localImageButton = (ImageButton) baseView.findViewById(R.id.btnBaseLeft);
		if (paramOnClickListener != null)
			localImageButton.setOnClickListener(paramOnClickListener);
		if (paramInt > 0)
			localImageButton.setImageResource(paramInt);
		if (this.titleView.getVisibility() != 0)
			this.titleView.setVisibility(0);
		localImageButton.setVisibility(0);
	}

	protected void setTopRight2Button(String paramString, int paramInt,
			View.OnClickListener paramOnClickListener) {
		if (this.titleView == null)
			return;
		ImageButton localImageButton = (ImageButton) baseView.findViewById(R.id.btnBaseRight2);
		if (paramOnClickListener != null)
			localImageButton.setOnClickListener(paramOnClickListener);
		if (paramInt > 0)
			localImageButton.setImageResource(paramInt);
		if (this.titleView.getVisibility() != 0)
			this.titleView.setVisibility(0);
		localImageButton.setVisibility(0);
	}

	protected void setTopRight3Button(String paramString, int paramInt,
			View.OnClickListener paramOnClickListener) {
		if (this.titleView == null)
			return;
		ImageButton localImageButton = (ImageButton) baseView.findViewById(R.id.btnBaseRight3);
		if (paramOnClickListener != null)
			localImageButton.setOnClickListener(paramOnClickListener);
		if (paramInt > 0)
			localImageButton.setImageResource(paramInt);
		if (this.titleView.getVisibility() != 0)
			this.titleView.setVisibility(0);
		localImageButton.setVisibility(0);
	}

	protected void setTopRightButton(String paramString, int paramInt,
			View.OnClickListener paramOnClickListener) {
		if (this.titleView == null)
			return;
		ImageButton localImageButton = (ImageButton) baseView.findViewById(R.id.btnBaseRight);
		if (paramOnClickListener != null)
			localImageButton.setOnClickListener(paramOnClickListener);
		if (paramInt > 0)
			localImageButton.setImageResource(paramInt);
		if (this.titleView.getVisibility() != 0)
			this.titleView.setVisibility(0);
		localImageButton.setVisibility(0);
	}

	protected void setTopRightButtonTxt(String paramString, int paramInt1,
			int paramInt2, View.OnClickListener paramOnClickListener) {
		if (this.titleView == null)
			return;
		Button localButton = (Button) baseView.findViewById(R.id.btnBaseRightTxt);
		if (paramOnClickListener != null)
			localButton.setOnClickListener(paramOnClickListener);
		localButton.setText(paramString);
		if(paramInt2 > 0)
			localButton.setTextColor(paramInt2);
		if (paramInt1 > 0)
			localButton.setBackgroundResource(paramInt1);
		if (this.titleView.getVisibility() != 0)
			this.titleView.setVisibility(0);
		localButton.setVisibility(0);
	}

	@Override
	public void onClick(View v) {
		
	}
	
	public void showDialog(String... param) {
//		pd = ProgressDialog.show(BaseApplication.getInstance(), "请求中...", "请稍后...");
//		pd.setCancelable(true);
		progressBar.setVisibility(View.VISIBLE);
	}

	public void hideDialog() {
//		if(pd != null)
//			pd.dismiss();
		progressBar.setVisibility(View.GONE);
	}
}
