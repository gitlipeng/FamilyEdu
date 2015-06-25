package com.family.familyedu;

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
import com.family.familyedu.inter.TaskListener;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.Util;

/**
 * 发布交流帖子
 * 
 * @author user
 * 
 */
public class PostCardAvtivity extends BaseActivity implements OnClickListener,
		TaskListener {
	BaseTask task;
	/**
	 * 标题
	 */
	private EditText edtPostTitle;
	/**
	 * 内容
	 */
	private EditText edtPostContent;

	private TextView tvType;
	/**
	 * 类型
	 */
	private String[] typeArray;
	private int m_iSelectClass;
	private BaseData saveData;
	/**
	 * "reply"代表回复
	 */
	private String type;
	
	private String postId;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		task = new BaseTask(this, this);
		saveData = new BaseData();
		if (getIntent() != null) {
			type = getIntent().getStringExtra("type");
			postId = getIntent().getStringExtra("postId");
		}
		initView();
		typeArray = getResources().getStringArray(R.array.type);
	}

	private void initView() {
		currentView = layoutInflater.inflate(R.layout.postcard, null);
		mainView.addView(this.currentView, mathLayoutParams);

		edtPostTitle = (EditText) currentView.findViewById(R.id.edtPostTitle);
		tvType = (TextView) currentView.findViewById(R.id.tvType);
		tvType.setOnClickListener(this);

		if ("reply".equals(type)) {
			setTitleText("回复");
			setTopRightButtonTxt("回复", -1, -1, this);
			edtPostTitle.setVisibility(View.GONE);
			tvType.setVisibility(View.GONE);
			findViewById(R.id.ivline1).setVisibility(View.GONE);
			findViewById(R.id.ivline2).setVisibility(View.GONE);
		} else {
			setTitleText("发帖");
			setTopRightButtonTxt("发布", -1, -1, this);
		}
		setTopLeftButton("", R.drawable.return_back, this);

		edtPostContent = (EditText) currentView
				.findViewById(R.id.edtPostContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBaseLeft:
			// 返回
			finish();
			break;
		case R.id.tvType:
			// 类型
			new AlertDialog.Builder(this)
					.setSingleChoiceItems(typeArray, this.m_iSelectClass,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramDialogInterface,
										int paramInt) {
									m_iSelectClass = paramInt;
									tvType.setText(typeArray[m_iSelectClass]);
									paramDialogInterface.dismiss();

								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.btnBaseRightTxt:
			if ("reply".equals(type)) {
				reply();
			} else {
				save();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 回复
	 */
	private void reply() {
		// 回复
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userID", Util.getUserId(this));
			jsonObject.put("postsID", postId);
			jsonObject.put("content", edtPostContent.getText());

			task.requestData(Constants.FEDOREPLYPOSTS, jsonObject, saveData,
					"parseBaseData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存
	 */
	private void save() {
		// 保存
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("fromUserID", Util.getUserId(this));
			jsonObject.put("title", edtPostTitle.getText());
			jsonObject.put("type", typeArray[m_iSelectClass]);
			jsonObject.put("content", edtPostContent.getText());

			task.requestData(Constants.FEADDPOSTS, jsonObject, saveData,
					"parseBaseData");
		} catch (JSONException e) {
			e.printStackTrace();
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
