package com.family.familyedu;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.family.familyedu.adapter.ShareAdapter;
import com.family.familyedu.bean.ShareBean;
import com.family.familyedu.util.Util;
import com.family.familyedu.widget.SelectPopupWindow;

public class BaseActivity extends FragmentActivity {
	private static final String TAG = "BaseActivity";
	protected RelativeLayout bottomView = null;
	protected RelativeLayout mainView = null;
	protected RelativeLayout titleView = null;
	public TextView tv_subtitle = null;
	public TextView tv_title = null;
	protected RelativeLayout.LayoutParams mathLayoutParams = new RelativeLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	protected LayoutInflater layoutInflater;
	protected View currentView = null;
	private ProgressDialog pd;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.a_base);
		this.titleView = ((RelativeLayout) findViewById(R.id.baseTop));
		this.mainView = ((RelativeLayout) findViewById(R.id.baseMain));
		this.bottomView = ((RelativeLayout) findViewById(R.id.baseBottom));
		this.tv_title = ((TextView) this.titleView
				.findViewById(R.id.txtBaseTitle));
		this.tv_subtitle = ((TextView) this.titleView
				.findViewById(R.id.txtBaseSubtitle));

		layoutInflater = getLayoutInflater();
	}

	protected boolean isLogin() {
		if (Util.getUserInfo(this) == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			return false;
		}
		return true;
	}

	protected void setTitleText(String paramString) {
		if ((paramString == null) || (paramString.equals("")))
			return;
		TextView localTextView = (TextView) findViewById(R.id.txtBaseTitle);
		localTextView.setText(paramString);
		if (this.titleView.getVisibility() != 0)
			this.titleView.setVisibility(0);
		localTextView.setVisibility(0);
	}

	protected void setTopLeftButton(String paramString, int paramInt,
			View.OnClickListener paramOnClickListener) {
		if (this.titleView == null)
			return;
		ImageButton localImageButton = (ImageButton) findViewById(R.id.btnBaseLeft);
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
		ImageButton localImageButton = (ImageButton) findViewById(R.id.btnBaseRight2);
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
		ImageButton localImageButton = (ImageButton) findViewById(R.id.btnBaseRight3);
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
		ImageButton localImageButton = (ImageButton) findViewById(R.id.btnBaseRight);
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
		Button localButton = (Button) findViewById(R.id.btnBaseRightTxt);
		if (paramOnClickListener != null)
			localButton.setOnClickListener(paramOnClickListener);
		localButton.setText(paramString);
		if (paramInt2 > 0)
			localButton.setTextColor(paramInt2);
		if (paramInt1 > 0)
			localButton.setBackgroundResource(paramInt1);
		if (this.titleView.getVisibility() != 0)
			this.titleView.setVisibility(0);
		localButton.setVisibility(0);
	}

	public void showDialog(String... param) {
		pd = ProgressDialog.show(this, "请求中...", "请稍后...");
		pd.setCancelable(true);
	}

	public void hideDialog() {
		if (pd != null)
			pd.dismiss();
	}

	// @Override
	// public void startActivity(Intent intent) {
	// super.startActivity(intent);
	// setStartTransition();
	// }
	//
	// @Override
	// public void startActivityForResult(Intent intent, int requestCode) {
	// super.startActivityForResult(intent, requestCode);
	// setStartTransition();
	// }
	//
	// private void setStartTransition() {
	// overridePendingTransition(R.anim.activity_right_in,
	// R.anim.activity_left_out);
	// }
	//
	// public void finish() {
	// super.finish();
	// overridePendingTransition(R.anim.activity_left_in,
	// R.anim.activity_right_out);
	// }
	private View shareView = null;
	private GridView gv_share = null;
	private ShareAdapter m_shareAdapter = null;
	private List<ShareBean> m_shareList = new ArrayList<ShareBean>();
	private RelativeLayout rl_shareCancel = null;
	private SelectPopupWindow m_selectPopupWindow = null;

	protected void showShare() {
		m_shareList.clear();

		ShareBean localShareBean = new ShareBean();
		localShareBean.setShareName(getString(R.string.telnet_qq));
		localShareBean.setResId(R.drawable.share_qq);
		m_shareList.add(localShareBean);

		localShareBean = new ShareBean();
		localShareBean.setShareName(getString(R.string.sina_weibo));
		localShareBean.setResId(R.drawable.share_sina);
		m_shareList.add(localShareBean);

		localShareBean = new ShareBean();
		localShareBean.setShareName(getString(R.string.qq_space));
		localShareBean.setResId(R.drawable.share_qzone);
		m_shareList.add(localShareBean);

		localShareBean = new ShareBean();
		localShareBean.setShareName(getString(R.string.weixin_friend));
		localShareBean.setResId(R.drawable.share_wechat);
		m_shareList.add(localShareBean);

		localShareBean = new ShareBean();
		localShareBean.setShareName(getString(R.string.qq_friend));
		localShareBean.setResId(R.drawable.share_friends);
		m_shareList.add(localShareBean);

		this.shareView = getLayoutInflater().inflate(R.layout.u_share, null);
		this.gv_share = ((GridView) this.shareView.findViewById(R.id.gvShare));
		this.m_shareAdapter = new ShareAdapter(this, this.m_shareList);
		this.gv_share.setAdapter(this.m_shareAdapter);
		this.gv_share.setOnItemClickListener(this.shareClickListener);
		this.rl_shareCancel = ((RelativeLayout) this.shareView
				.findViewById(R.id.rlShareCancel));
		this.rl_shareCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				m_selectPopupWindow.dismiss();
			}
		});
		this.m_selectPopupWindow = new SelectPopupWindow(this, this.shareView);
		this.m_selectPopupWindow.addView();
		this.m_selectPopupWindow.setFocusable(true);
		m_selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		m_selectPopupWindow.setOutsideTouchable(true);
		this.m_selectPopupWindow.showAtLocation(this.currentView, 80, 0, 0);
	}

	private AdapterView.OnItemClickListener shareClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {
			switch (paramInt) {
			case 0:
				Util.showShare(context, QQ.NAME);
				break;
			case 1:
				Util.showShare(context, SinaWeibo.NAME);
				break;
			case 2:
				Util.showShare(context, QZone.NAME);
				break;
			case 3:
				Util.showShare(context, Wechat.NAME);
				break;
			case 4:
				Util.showShare(context, WechatMoments.NAME);
				break;
			default:
				break;
			}
		}
	};
}
