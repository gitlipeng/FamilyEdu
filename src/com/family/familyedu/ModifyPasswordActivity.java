package com.family.familyedu;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import cn.bmob.v3.listener.SaveListener;

import com.family.familyedu.bean.BaseData;
import com.family.familyedu.bean.User;
import com.family.familyedu.inter.TaskListener;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.LogUtil;
import com.family.familyedu.util.Util;

/**
 * 注册
 * 
 * @author user
 * 
 */
public class ModifyPasswordActivity extends BaseActivity implements
		OnClickListener, TaskListener {
	/**
	 * 注册帐号
	 */
	private String oldpwd;

	/**
	 * 注册密码
	 */
	private String password;
	/**
	 * 原密码
	 */
	private EditText mOldPassword;

	/**
	 * 密码
	 */
	private EditText mPassword;

	/**
	 * 密码确认
	 */
	private EditText mConfimPassword;

	/**
	 * 修改密码按钮
	 */
	private Button mRetPasswordBtn;

	private BaseTask task;

	private BaseData baseData;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		task = new BaseTask(this, this);
		baseData = new BaseData();
		initView();
	}

	private void initView() {
		currentView = layoutInflater.inflate(R.layout.modifypassword, null);
		mainView.addView(this.currentView, mathLayoutParams);

		setTitleText(getString(R.string.register));
		setTopLeftButton("", R.drawable.return_back, this);

		mOldPassword = (EditText) findViewById(R.id.old_password);
		mPassword = (EditText) findViewById(R.id.pwd);
		mConfimPassword = (EditText) findViewById(R.id.confim_pwd);
		mRetPasswordBtn = (Button) findViewById(R.id.btn_register);
		mRetPasswordBtn.setOnClickListener(this);
	}

	/**
	 * 验证数据
	 * 
	 * @return
	 */
	private boolean checkData() {
		String oldPassword = mOldPassword.getText().toString().trim();
		String password = mPassword.getText().toString().trim();
		String confimPassword = mConfimPassword.getText().toString().trim();

		// 邮箱地址是否为空
		if ("".equals(oldPassword)) {
			mOldPassword.setError(Util.getTextError("请输入原密码"));
			mOldPassword.requestFocus();
			return false;
		} else {
			mOldPassword.setError(null);
		}

		// 密码是否为空
		if ("".equals(password)) {
			mPassword.setError(Util.getTextError("请输入密码"));
			mPassword.requestFocus();
			return false;
		} else {
			mPassword.setError(null);
		}

		// 密码长度
		// if(password.length() < 6){
		// mPassword.setError(Util.getTextError(getString(R.string.error_msg_30)));
		// mPassword.requestFocus();
		// return false;
		// }else{
		// mPassword.setError(null);
		// }

		// 密码格式
		// if (!Util.checkPasswordNum(password)) {
		// mPassword.setError(Util.getTextError(getString(R.string.error_msg_31)));
		// mPassword.requestFocus();
		// return false;
		// }else{
		// mPassword.setError(null);
		// }

		// 密码确认是否为空
		if ("".equals(confimPassword)) {
			mConfimPassword.setError(Util.getTextError("请输入确认密码"));
			mConfimPassword.requestFocus();
			return false;
		} else {
			mConfimPassword.setError(null);
		}

		// 密码和密码确认是否一致
		if (!password.equals(confimPassword)) {
			mConfimPassword.setError(Util.getTextError("密码与确认密码不一致，请重新输入"));
			mConfimPassword.requestFocus();
			return false;
		} else {
			mConfimPassword.setError(null);
		}

		return true;
	}

	private void register() {
		JSONObject jsonObject = new JSONObject();
		try {
			oldpwd = mOldPassword.getText().toString().trim();
			password = mPassword.getText().toString().trim();
			jsonObject.put("username", Util.getUserInfo(this).getUsername());
			jsonObject.put("password", oldpwd);
			jsonObject.put("newPassword", password);
			task.requestData(Constants.FECHANGEUSERPWD, jsonObject, baseData,
					"parseRegisterData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBaseLeft:
			// 返回
			finish();
			break;
		case R.id.btn_register:
			// 注册
			if (checkData()) {
				register();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void requestSuccess(Object object) {
		if (object.getClass().equals(BaseData.class)) {
			// 注册成功
			baseData = (BaseData) object;
			Util.savePassword(password);
			finish();
		}
	}

	@Override
	public void requestFail(Object object) {

	}

}
