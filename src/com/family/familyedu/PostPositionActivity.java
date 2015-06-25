package com.family.familyedu;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.family.familyedu.bean.BaseData;
import com.family.familyedu.bean.ClassBean;
import com.family.familyedu.bean.User;
import com.family.familyedu.inter.TaskListener;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.Util;

public class PostPositionActivity extends BaseActivity implements OnClickListener,
		TaskListener {
	BaseTask task;
	/**
	 * 标题
	 */
	private EditText edtPostTitle;
	/**
	 * 地址
	 */
	private EditText edtAddress;
	/**
	 * 科目
	 */
	private TextView tvClass;
	/**
	 * 单价
	 */
	private EditText edtPrice;
	/**
	 * 时长
	 */
	private EditText edtTime;
	/**
	 * 内容
	 */
	private EditText edtPostContent;
	/**
	 * 总科目
	 */
	private String[] classArray;
	private int m_iSelectClass;
	private BaseData saveData;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		task = new BaseTask(this, this);
		saveData = new BaseData();
		initView();
		setClassData();
	}

	private void initView() {
		currentView = layoutInflater.inflate(R.layout.postposition, null);
		mainView.addView(this.currentView, mathLayoutParams);

		setTitleText("发布家教信息");
		setTopLeftButton("", R.drawable.return_back, this);
		setTopRightButtonTxt("发布", -1, -1, this);
		edtPostTitle = (EditText) currentView.findViewById(R.id.edtPostTitle);
		edtAddress = (EditText) currentView.findViewById(R.id.edtAddress);
		tvClass = (TextView) currentView.findViewById(R.id.tvClass);
		tvClass.setOnClickListener(this);
		edtPrice = (EditText) currentView.findViewById(R.id.edtPrice);
		edtTime = (EditText) currentView.findViewById(R.id.edtTime);
		edtPostContent = (EditText) currentView
				.findViewById(R.id.edtPostContent);
	}
	/**
	 * 设置学科数据源
	 */
	private void setClassData(){
		if(Constants.classNameList != null){
			classArray = new String[Constants.classNameList.size()];
			for(int i = 0 ;i < Constants.classNameList.size(); i++){
				classArray[i] = Constants.classNameList.get(i);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBaseLeft:
			// 返回
			finish();
			break;
		case R.id.tvClass:
			//科目
			new AlertDialog.Builder(this)
			.setSingleChoiceItems(classArray,
					this.m_iSelectClass,
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface paramDialogInterface,
								int paramInt) {
							m_iSelectClass = paramInt;
							tvClass.setText(classArray[m_iSelectClass]);
							paramDialogInterface.dismiss();
							
						}
					}).setNegativeButton("取消", null).show();
			break;
		case R.id.btnBaseRightTxt:
			//保存
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("fromUserID", Util.getUserId(this));
				jsonObject.put("title",edtPostTitle.getText());
				jsonObject.put("area",edtAddress.getText());
				jsonObject.put("price",edtPrice.getText());
				jsonObject.put("duration",edtTime.getText());
				jsonObject.put("learnClassID",Constants.classList.get(m_iSelectClass).getObjectId());
				jsonObject.put("content",edtPostContent.getText());
				
				task.requestData(Constants.FEADDPOSITION, jsonObject, saveData,
						"parseBaseData");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void requestSuccess(Object object) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void requestFail(Object object) {

	}
}
