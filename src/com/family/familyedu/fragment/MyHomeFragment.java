package com.family.familyedu.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.family.familyedu.MainActivity;
import com.family.familyedu.R;
import com.family.familyedu.UserCenterProfileActivity;
import com.family.familyedu.bean.User;
import com.family.familyedu.util.Util;
import com.test.woshifangnu.ChatHistoryActivity;

/**
 * 我家
 * @author user
 *
 */
public class MyHomeFragment extends BaseFragment {
	private static final int REQUESTCODE_LOGIN = 1;
	private static final int REQUESTCODE_UPDATA = 2;
	private MainActivity activity;
	private TextView mNickName;
	private OutLoginReceiver m_outLoginReceiver = null;
	/**
	 * 判断是否在登录页面点了返回，取消了登录操作
	 */
	private boolean cancleLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		initView(inflater);

		this.m_outLoginReceiver = new OutLoginReceiver();
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("out_login_action");
		activity.registerReceiver(this.m_outLoginReceiver, localIntentFilter);

		requestData();
		return baseView;
	}

	private void initView(LayoutInflater inflater) {
		currentView = inflater.inflate(R.layout.usercenterfragment, null);
		mainView.addView(this.currentView, mathLayoutParams);
		setTitleText("我家");
		mNickName = (TextView) currentView.findViewById(R.id.nickname);
		currentView.findViewById(R.id.about).setOnClickListener(
				this);
		currentView.findViewById(R.id.usercenter_myaccount).setOnClickListener(
				this);
		currentView.findViewById(R.id.ll_msg).setOnClickListener(
				this);
	}

	/**
	 * 初始化数据
	 */
	public void requestData() {
		if (Util.getUserInfo(activity) != null) {
			User userInfo = Util.getUserInfo(activity);
			mNickName.setText(userInfo.getNickname());
		} else {
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.usercenter_myaccount:
			// 个人资料维护
			Intent intent = new Intent(activity,
					UserCenterProfileActivity.class);
			startActivityForResult(intent, REQUESTCODE_UPDATA);
			break;
		case R.id.ll_msg:
			intent = new Intent(activity, ChatHistoryActivity.class);
			startActivity(intent);
			break;
		case R.id.about:
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE_LOGIN:
			if (resultCode == Activity.RESULT_OK) {
				// 登录成功
				// initData();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// 取消登陆
				// cancleLogin = true;
			}
			break;
		case REQUESTCODE_UPDATA:
			if (resultCode == Activity.RESULT_OK) {
				// 修改成功
				requestData();
			} else if (resultCode == 100) {
				// 登出
				activity.setCurrentPosition(0);
			}
			break;
		default:
			break;
		}
	}

	private class OutLoginReceiver extends BroadcastReceiver {
		private OutLoginReceiver() {
		}

		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getIntExtra("login_status", -1) == 1) {
				// 登陆成功
				requestData();
				return;
			} else {
				// 登陆失败
			}
		}
	}
}
