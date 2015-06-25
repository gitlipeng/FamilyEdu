package com.family.familyedu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.family.familyedu.bean.BaseData;
import com.family.familyedu.bean.User;
import com.family.familyedu.inter.TaskListener;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.CustomerDatePickerDialog;
import com.family.familyedu.util.Util;

public class UserCenterProfileActivity extends BaseActivity implements
		OnClickListener, TaskListener {
	private EditText etEPName;
	private TextView tvEPSex;
	private TextView tvEPBirthday;
	private EditText etEPAge;
	private TextView tvEPTel;
	private TextView tvEPAddress;
	private TextView tvEPType;
	private TextView tvEPLevel;
	private TextView tvEPSilledClass;
	private EditText edtEPProfession;
	private EditText edtEPEduClass;
	private BaseTask task;

	private BaseData updateData;

	private User userInfo;

	private LinearLayout llTeacherLayout;
	private int m_iDay = 0;
	private int m_iMonth = 0;
	private int m_iSelectSex = 0;
	private int m_iSelectLevel = 0;
	private boolean[] m_iSelectEduClass;
	private int m_iYear = 0;
	private Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(Message paramMessage) {
			switch (paramMessage.what) {
			default:
				return;
			case 1:
			}
			showDialog(2);
		}
	};
	/**
	 * 学历
	 */
	private String[] levelArray;

	/**
	 * 擅长科目
	 */
	private String[] skilledClassArray;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		task = new BaseTask(this, this);
		updateData = new BaseData();
		initView();
		initData();
	}

	private void initView() {
		currentView = layoutInflater.inflate(R.layout.usercenterprofile, null);
		mainView.addView(this.currentView, mathLayoutParams);

		setTitleText("个人资料");
		setTopLeftButton("", R.drawable.return_back, this);
		setTopRightButtonTxt("保存", -1, -1, this);

		etEPName = (EditText) currentView.findViewById(R.id.etEPName);
		tvEPSex = (TextView) currentView.findViewById(R.id.tvEPSex);
		tvEPSex.setOnClickListener(this);
		tvEPBirthday = (TextView) currentView.findViewById(R.id.tvEPBirthday);
		tvEPBirthday.setOnClickListener(this);
		etEPAge = (EditText) currentView.findViewById(R.id.etEPAge);
		tvEPTel = (TextView) currentView.findViewById(R.id.tvEPTel);
		tvEPAddress = (TextView) currentView.findViewById(R.id.tvEPAddress);
		tvEPType = (TextView) currentView.findViewById(R.id.tvEPType);
		tvEPLevel = (TextView) currentView.findViewById(R.id.tvEPLevel);
		tvEPLevel.setOnClickListener(this);
		tvEPSilledClass = (TextView) currentView
				.findViewById(R.id.tvEPSilledClass);
		tvEPSilledClass.setOnClickListener(this);
		edtEPProfession = (EditText) currentView
				.findViewById(R.id.edtEPProfession);
		edtEPProfession.setOnClickListener(this);
		edtEPEduClass = (EditText) currentView.findViewById(R.id.edtEPEduClass);
		llTeacherLayout = (LinearLayout) currentView
				.findViewById(R.id.ll_teacher);

		currentView.findViewById(R.id.btnModifyPassword).setOnClickListener(
				this);
		currentView.findViewById(R.id.btnLogout).setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	private void initData() {
		userInfo = Util.getUserInfo(this);
		// 名称
		etEPName.setText(userInfo.getNickname());
		if (userInfo.getNickname() != null) {
			etEPName.setSelection(userInfo.getNickname().length());
		}
		// 性别
		if (Constants.SEX_W.equals(userInfo.getSex())) {
			m_iSelectSex = 1;
			tvEPSex.setText("女");
		} else {
			m_iSelectSex = 0;
			tvEPSex.setText("男");
		}
		// 年龄
		etEPAge.setText(userInfo.getAge());
		// 生日
		try {
			Calendar localCalendar = Calendar.getInstance();
			if (TextUtils.isEmpty(userInfo.getBirthday())) {
				this.m_iYear = 1900;
				this.m_iMonth = 1;
			} else {
				Date localDate = new SimpleDateFormat("yyyy-MM").parse(userInfo
						.getBirthday());
				localCalendar.setTime(localDate);
				this.m_iYear = localCalendar.get(Calendar.YEAR);
				this.m_iMonth = localCalendar.get(Calendar.MONTH) + 1;
			}
		} catch (Exception localException) {
		}

		tvEPBirthday.setText(userInfo.getBirthday());

		// 电话
		tvEPTel.setText(userInfo.getTel());
		// 地址
		tvEPAddress.setText(userInfo.getAddress());
		// 身份
		if (Constants.USERTYPE_PARENT.equals(userInfo.getUserType())) {
			tvEPType.setText(getString(R.string.parent));
			llTeacherLayout.setVisibility(View.GONE);
		} else {
			tvEPType.setText(getString(R.string.teacher));
			llTeacherLayout.setVisibility(View.VISIBLE);
		}
		// 学历
		if (Constants.eduLevelList != null) {
			levelArray = new String[Constants.eduLevelList.size()];
			levelArray = Constants.eduLevelList.toArray(levelArray);
		} else {
			levelArray = getResources().getStringArray(R.array.userlevel);
		}
		tvEPLevel.setText(userInfo.getEduLevel());
		for (int i = 0; i < levelArray.length; i++) {
			if (levelArray[i].equals(userInfo.getEduLevel())) {
				m_iSelectLevel = i;
				break;
			}
		}

		// 擅长科目
		if (Constants.classNameList != null) {
			skilledClassArray = new String[Constants.classNameList.size()];
			skilledClassArray = Constants.classNameList
					.toArray(skilledClassArray);
		} else {
			skilledClassArray = getResources().getStringArray(
					R.array.silledclass);
		}
		tvEPSilledClass.setText(userInfo.getSkilledClass());
		m_iSelectEduClass = new boolean[skilledClassArray.length];
		for (int i = 0; i < skilledClassArray.length; i++) {
			if (skilledClassArray[i].equals(userInfo.getSkilledClass())) {
				m_iSelectEduClass[i] = true;
			} else {
				m_iSelectEduClass[i] = false;
			}
		}

		// 职业
		edtEPProfession.setText(userInfo.getProfession());
		// 辅导年级
		edtEPEduClass.setText(userInfo.getEduClass());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBaseLeft:
			// 返回
			finish();
			break;
		case R.id.btnBaseRightTxt:
			// 保存
			save();
			break;
		case R.id.tvEPSex:
			// 性别
			// sexDialog.show();
			new AlertDialog.Builder(this)
					.setSingleChoiceItems(new String[] { "男", "女" },
							this.m_iSelectSex,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
									m_iSelectSex = paramInt;
									paramDialogInterface.dismiss();
									switch (paramInt) {
									case 0:
										tvEPSex.setText("男");
										break;
									case 1:
										tvEPSex.setText("女");
										break;
									default:
										break;
									}
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.tvEPBirthday:
			// 生日
			Message localMessage = new Message();
			localMessage.what = 1;
			handler.sendMessage(localMessage);
			break;
		case R.id.tvEPLevel:
			// 学历
			new AlertDialog.Builder(this)
					.setSingleChoiceItems(levelArray, this.m_iSelectLevel,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
									m_iSelectLevel = paramInt;
									tvEPLevel
											.setText(levelArray[m_iSelectLevel]);
									paramDialogInterface.dismiss();

								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.tvEPSilledClass:
			// 擅长科目
			new AlertDialog.Builder(this)
					.setMultiChoiceItems(skilledClassArray, m_iSelectEduClass,
							new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									m_iSelectEduClass[which] = isChecked;
								}

							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String content = "";
									for (int i = 0; i < m_iSelectEduClass.length; i++) {
										if (m_iSelectEduClass[i]) {
											if ("".equals(content)) {
												content = content
														+ skilledClassArray[i];
											} else {
												content = content + ","
														+ skilledClassArray[i];
											}
										}
									}
									tvEPSilledClass.setText(content);
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.btnModifyPassword:
			// 修改密码
			Intent intent = new Intent(this,ModifyPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.btnLogout:
			// 退出
			new AlertDialog.Builder(this)
					.setMessage("是否注销当前账号？")
					.setTitle("提示")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Util.Logout(UserCenterProfileActivity.this);
									setResult(100);
									finish();
								}
							}).setNegativeButton("取消", null).show();

			break;
		default:
			break;
		}
	};

	private void save() {
		JSONObject jsonObject = new JSONObject();
		try {
			User bean = Util.getUserInfo(this);
			jsonObject.put("userName", bean.getUsername());
			jsonObject.put("userPwd", Util.getPassword());
			jsonObject.put("userType", bean.getUserType());
			JSONObject userInfoObject = new JSONObject();
			userInfoObject.put("nickname", etEPName.getText());
			userInfoObject.put("sex", m_iSelectSex == 1 ? Constants.SEX_W
					: Constants.SEX_M);
			userInfoObject.put("age", etEPAge.getText());
			userInfoObject.put("birthday", tvEPBirthday.getText());
			userInfoObject.put("tel", tvEPTel.getText());
			userInfoObject.put("address", tvEPAddress.getText());
			userInfoObject.put("eduLevel", tvEPLevel.getText());
			userInfoObject.put("skilledClass", tvEPSilledClass.getText());
			userInfoObject.put("profession", edtEPProfession.getText());
			userInfoObject.put("eduClass", edtEPEduClass.getText());
			jsonObject.put("userInfo", userInfoObject);
			task.requestData(Constants.FEUPDATEINFO, jsonObject, updateData,
					"parseBaseData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void requestSuccess(Object object) {
		if (object.getClass().equals(BaseData.class)) {
			// 修改信息成功
			updateData = (BaseData) object;
			BaseTask task = new BaseTask(this);
			User bean = Util.getUserInfo(this);
			task.login(bean.getUsername(), Util.getPassword(),
					new SaveListener() {
						@Override
						public void onSuccess() {
							setResult(RESULT_OK);
							finish();
						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
		}
	}

	@Override
	public void requestFail(Object object) {
		// TODO Auto-generated method stub

	}

	private DatePickerDialog.OnDateSetListener dataSelectListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker paramDatePicker, int paramInt1,
				int paramInt2, int paramInt3) {
			m_iYear = paramInt1;
			m_iMonth = paramInt2;
			m_iDay = paramInt3;
			TextView localTextView = tvEPBirthday;
			StringBuilder localStringBuilder1 = new StringBuilder().append(
					m_iYear).append("-");
			Object localObject1;
			StringBuilder localStringBuilder2;
			if (m_iMonth < 9) {
				localObject1 = "0" + (m_iMonth + 1);
			} else {
				localObject1 = Integer.valueOf(m_iMonth + 1);
			}
			localStringBuilder2 = localStringBuilder1.append(localObject1);
			localTextView.setText(localStringBuilder2);
		};
	};

	@Deprecated
	protected Dialog onCreateDialog(int paramInt) {
		switch (paramInt) {
		case 2:
			CustomerDatePickerDialog dialog = new CustomerDatePickerDialog(
					this, this.dataSelectListener, this.m_iYear, this.m_iMonth,
					this.m_iDay);
			return dialog;
		default:
			return null;
		}

	}

	protected void onPrepareDialog(int paramInt, Dialog paramDialog) {
		switch (paramInt) {
		case 2:
			((CustomerDatePickerDialog) paramDialog).updateDate(this.m_iYear,
					this.m_iMonth, this.m_iDay);
			break;
		default:
			return;
		}

	}
}
