package com.family.familyedu;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.family.familyedu.bean.BaseData;
import com.family.familyedu.bean.PositionBean;
import com.family.familyedu.bean.PositionDetailData;
import com.family.familyedu.inter.TaskListener;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.Util;

public class PositionDetailActivity extends BaseActivity implements
		OnClickListener, TaskListener {
	private BaseTask task;
	private PositionDetailData positionDetailData;
	private PositionBean positionBean;
	private Button actionBtn;
	private BaseData baseData;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		task = new BaseTask(this, this);
		positionDetailData = new PositionDetailData();
		baseData = new BaseData();
		initView();
	}

	private void initView() {
		currentView = layoutInflater.inflate(R.layout.position_detail, null);
		mainView.addView(this.currentView, mathLayoutParams);

		setTitleText("详情");
		setTopLeftButton("", R.drawable.return_back, this);

		Intent intent = getIntent();
		if (intent != null) {
			positionBean = (PositionBean) intent
					.getSerializableExtra("positionBean");
			if (positionBean != null) {
				((TextView) findViewById(R.id.tv_title)).setText(positionBean
						.getTitle());
				((TextView) findViewById(R.id.tvAddress)).setText(positionBean
						.getArea());
				((TextView) findViewById(R.id.tv_price)).setText(positionBean
						.getPrice() + "元/小时");
				((TextView) findViewById(R.id.tv_time)).setText(positionBean
						.getDuration() + "小时");
				((TextView) findViewById(R.id.tv_data)).setText(positionBean
						.getCreatedAt());
				if (positionBean.getLearnClassID() != null) {
					((TextView) findViewById(R.id.tv_class))
							.setText(positionBean.getLearnClassID().getName());
				}

				((TextView) findViewById(R.id.tvContent)).setText(positionBean
						.getContent());

				// 状态
				TextView statusTv = (TextView) findViewById(R.id.tv_status);
				String status = positionBean.getStatus();
				if ("10".equals(status)) {
					statusTv.setText("等待申请");
				} else if ("20".equals(status)) {
					statusTv.setText("已申请");
				} else if ("30".equals(status)) {
					statusTv.setText("选择家教");
				} else if ("40".equals(status)) {
					statusTv.setText("已确认");
				} else if ("100".equals(status)) {
					statusTv.setText("已完成");
				} else if ("0".equals(status)) {
					statusTv.setText("已关闭");
				}

				actionBtn = (Button) findViewById(R.id.actionBtn);
				actionBtn.setOnClickListener(this);
				requestData();
			}
		}
	}

	private void requestData() {
		try {
			JSONObject object = new JSONObject();
			object.put("positionID", positionBean.getObjectId());
			object.put("userID", Util.getUserId(this));
			task.requestData(Constants.FEGETPOSITIONDETAIL, object,
					positionDetailData, "parsePositionDetailData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBaseLeft:
			// 返回
			finish();
			break;
		case R.id.actionBtn:
			if ("close".equals(positionDetailData.getAction())) {
				// 关闭
				close();
			} else if ("cancelApply".equals(positionDetailData.getAction())) {
				// 取消
				cancle();
			} else if ("apply".equals(positionDetailData.getAction())) {
				// 申请
				apply();
			}
			break;
		default:
			break;
		}
	}

	private void apply() {
		try {
			JSONObject object = new JSONObject();
			object.put("positionID", positionBean.getObjectId());
			object.put("userID", Util.getUserId(this));
			task.requestData(Constants.FEAPPLYPOSITION, object,
					baseData, "parseBaseData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void cancle() {
		try {
			JSONObject object = new JSONObject();
			object.put("positionID", positionBean.getObjectId());
			object.put("userID", Util.getUserId(this));
			object.put("positionApplyID", positionDetailData.getPositionApplyID());
			
			task.requestData(Constants.FECANCLEPOSITION, object,
					baseData, "parseBaseData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			JSONObject object = new JSONObject();
			object.put("positionID", positionBean.getObjectId());
			object.put("userID", Util.getUserId(this));
			task.requestData(Constants.FECLOSEPOSITION, object,
					baseData, "parseBaseData");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void requestSuccess(Object object) {
		if (PositionDetailData.class == object.getClass()) {
			positionDetailData = (PositionDetailData) object;

			((TextView) findViewById(R.id.tv_applycount))
					.setText(positionDetailData.getApplyCount());

			if ("".equals(positionDetailData.getAction())) {
				actionBtn.setVisibility(View.GONE);
			}
			actionBtn.setText(positionDetailData.getBtnTitle());
		}else if(BaseData.class == object.getClass()){
			finish();
		}
	}

	@Override
	public void requestFail(Object object) {

	};

}
