package com.family.familyedu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tpl.OnLoginListener;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Util;
import com.test.woshifangnu.db.UserDao;
import com.test.woshifangnu.domin.User;
import com.test.woshifangnu.util.Constant;

/**
 * @author lipeng
 * 
 */
public class LoginActivity extends BaseActivity implements Callback,
		PlatformActionListener, OnClickListener {
	private static final int REQUESTCODE_REGISTER = 1;
	private static final int MSG_SMSSDK_CALLBACK = 1;
	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;
	private static final int MSG_AUTH_COMPLETE = 4;

	private static final int REQUEST_AUTH = 101;
	private static final String TAG = "LoginActivity";
	private Button btn_login = null;
	private Button btn_loginQQ = null;
	private Button btn_loginWeibo = null;
	private View currentView = null;
	private EditText edt_password = null;
	private EditText edt_username = null;
	private TextView tv_register = null;
	private Handler handler;
	private OnLoginListener signupListener;
	private String m_strPassword = "";
	private String m_strUsername = "";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initView();
		handler = new Handler(this);
	}

	private void initView() {
		currentView = layoutInflater.inflate(R.layout.login, null);
		mainView.addView(this.currentView, mathLayoutParams);

		setTitleText(getString(R.string.login));
		setTopLeftButton("", R.drawable.return_back, this);

		this.edt_username = ((EditText) this.currentView
				.findViewById(R.id.edtLoginUser));
		this.edt_password = ((EditText) this.currentView
				.findViewById(R.id.edtLoginPassword));
		this.tv_register = ((TextView) this.currentView
				.findViewById(R.id.tvLoginRegister));
		this.btn_login = ((Button) this.currentView.findViewById(R.id.btnLogin));
		this.btn_loginWeibo = ((Button) this.currentView
				.findViewById(R.id.btnLoginWeibo));
		this.btn_loginQQ = ((Button) this.currentView
				.findViewById(R.id.btnLoginQQ));
		this.btn_login.setOnClickListener(this);
		this.btn_loginWeibo.setOnClickListener(this);
		this.btn_loginQQ.setOnClickListener(this);
		this.tv_register.setOnClickListener(this);
	}

	/** 设置授权回调，用于判断是否进入注册 */
	public void setOnLoginListener(OnLoginListener l) {
		this.signupListener = l;
	}

	// public void login(View v) {
	// Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
	// authorize(wechat);
	// }
	//
	// public void weibo(View v) {
	// Platform wechat = ShareSDK.getPlatform(SinaWeibo.NAME);
	// authorize(wechat);
	// }

	private void authorize(Platform plat) {
		if (plat == null) {
			return;
		}
		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		// plat.SSOSetting(true);
		plat.showUser(null);
	}

	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { platform.getName(), res };
			handler.sendMessage(msg);
		}
	}

	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}

	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_AUTH_CANCEL: {
			// 取消授权
			Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT)
					.show();
			Util.showSToast(this, R.string.auth_cancel);

		}
			break;
		case MSG_AUTH_ERROR: {
			// 授权失败
			Util.showSToast(this, R.string.auth_error);
		}
			break;
		case MSG_AUTH_COMPLETE: {
			// 授权成功
			Object[] objs = (Object[]) msg.obj;
			String platform = (String) objs[0];
			HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
			Util.showSToast(this, R.string.auth_complete + platform);
		}
			break;
		}
		return false;
	}

	private boolean checkData() {
		this.m_strUsername = this.edt_username.getText().toString();
		this.m_strPassword = this.edt_password.getText().toString();
		if (TextUtils.isEmpty(m_strUsername)) {
			this.edt_username.setError("用户名不能为空");
			edt_username.requestFocus();
			return false;
		} else {
			edt_username.setError(null);
		}
		if (TextUtils.isEmpty(m_strPassword)) {
			this.edt_password.setError("密码不能为空");
			edt_password.requestFocus();
			return false;
		} else {
			edt_password.setError(null);
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBaseLeft:
			// 返回
			Intent localIntent = new Intent();
			localIntent.setAction("out_login_action");
			sendBroadcast(localIntent);
			finish();
			break;
		case R.id.tvLoginRegister:
			// 注册
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivityForResult(intent, REQUESTCODE_REGISTER);
			break;
		case R.id.btnLogin:
			// 登录
			if (checkData()) {
				login();
			}
			break;
		default:
			break;
		}
	}
	
	private void login(){
		BaseTask task = new BaseTask(this);
		showDialog();
		task.login(m_strUsername, m_strPassword, new SaveListener() {
			@Override
			public void onSuccess() {
				Util.savePassword(m_strPassword);// 记住密码
				loginHX(m_strUsername, m_strUsername);//登录环信
			}

			@Override
			public void onFailure(int code, String msg) {
				hideDialog();
				if(code == 101){
					msg = "用户名或密码不正确";
				}
				Util.showSToast(LoginActivity.this, msg);
			}
		});
	}

    @Override
    protected void loginSuccess() {
        sendLoginReceiver();
        finish();
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 不登录 ，直接返回
			// setResult(RESULT_CANCELED);
			// finish();
			Intent localIntent = new Intent();
			localIntent.setAction("out_login_action");
			sendBroadcast(localIntent);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void sendLoginReceiver() {
		Intent localIntent = new Intent();
		localIntent.putExtra("login_status", 1);
		localIntent.setAction("out_login_action");
		sendBroadcast(localIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		switch (requestCode) {
		case REQUESTCODE_REGISTER:
			if (resultCode == RESULT_OK) {
				// 注册成功
				sendLoginReceiver();
				this.finish();
			}
			break;

		default:
			break;
		}
	}
	

	
}
