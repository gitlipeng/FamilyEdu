package com.family.familyedu.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.RemoteViews;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.SaveListener;

import com.family.familyedu.R;
import com.family.familyedu.bean.BaseData;
import com.family.familyedu.bean.ClassBean;
import com.family.familyedu.bean.EduLevelBean;
import com.family.familyedu.bean.User;
import com.family.familyedu.bean.VersionBean;
import com.family.familyedu.inter.TaskListener;
import com.google.gson.Gson;

public class BaseTask {
	private Context context;
	private BaseData baseBean;
	private TaskListener listener;
	private boolean isHideProgress;
	private ProgressDialog pd;

	public BaseTask(Context context) {
		this.context = context;
	}

	public BaseTask(Context context, TaskListener listener) {
		this.context = context;
		this.listener = listener;
	}

	public BaseTask(Context context, TaskListener listener,
			Boolean isHideProgress) {
		this.context = context;
		this.listener = listener;
		this.isHideProgress = isHideProgress;
	}

	public void requestData(String requestMethodName, JSONObject jsonObject,
			BaseData paramBaseBean, final String gsonMethodName) {
		AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
		// 第一个参数是上下文对象，第二个参数是云端代码的方法名称，第三个参数是上传到云端代码的参数列表（JSONObject
		// cloudCodeParams），第四个参数是回调类
		this.baseBean = paramBaseBean;
		showDialog();
		ace.callEndpoint(context, requestMethodName, jsonObject,
				new CloudCodeListener() {
					@Override
					public void onSuccess(Object object) {
						try {
							JSONUtils json = JSONUtils.getInstance();
							Class<?> c = json.getClass();

							Method method = c.getMethod(
									gsonMethodName,
									new Class<?>[] { String.class,
											baseBean.getClass() });
							Object[] obj = { object.toString(), baseBean };
							baseBean = (BaseData) method.invoke(json, obj);

							Util.showSToast(context, baseBean.getMsg());
							if ("1".equals(baseBean.getSuccess())) {
								// 请求成功
								if (listener != null) {
									listener.requestSuccess(baseBean);
								}
							} else {
								// 请求失败
								if (listener != null) {
									listener.requestFail(baseBean);
								}
							}
							hideDialog();
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("反射方法调用出错，请检查参数设置");
							hideDialog();
						}
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						LogUtil.getInstance().i("fail" + arg1);
						Util.showSToast(context, arg1);
						if (listener != null) {
							listener.requestFail(baseBean);
						}
						hideDialog();
					}
				});
	}

	public void login(String m_strUsername, String m_strPassword,
			SaveListener listener) {
		User bean = new User();
		bean.setUsername(m_strUsername);
		bean.setPassword(m_strPassword);
		bean.login(context, listener);
	}

	/**
	 * 获取学科
	 */
	public void getClassData() {
		BmobQuery query = new BmobQuery("t_class");
		query.findObjects(context, new FindCallback() {
			@Override
			public void onSuccess(JSONArray array) {
				Constants.classNameList = new ArrayList<String>();
				Constants.classList = new ArrayList<ClassBean>();
				Gson gson = new Gson();
				if (array != null) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.optJSONObject(i);
						ClassBean bean = gson.fromJson(object.toString(),
								ClassBean.class);
						Constants.classNameList.add(bean.getName());
						Constants.classList.add(bean);
					}
					if (listener != null) {
						listener.requestSuccess(null);
					}
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (listener != null) {
					listener.requestSuccess(null);
				}
			}
		});

	}

	/**
	 * 获取学历
	 */
	public void getEduLevelData() {
		BmobQuery query = new BmobQuery("t_eduLevel");
		query.findObjects(context, new FindCallback() {
			@Override
			public void onSuccess(JSONArray array) {
				Constants.eduLevelList = new ArrayList<String>();
				Gson gson = new Gson();
				if (array != null) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.optJSONObject(i);
						EduLevelBean bean = gson.fromJson(object.toString(),
								EduLevelBean.class);
						Constants.eduLevelList.add(bean.getName());
					}
					if (listener != null) {
						listener.requestSuccess(null);
					}
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (listener != null) {
					listener.requestSuccess(null);
				}
			}
		});
	}
	 Notification notification;
	 private int progressCount = 0;
	 NotificationManager mNotificationManager;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(progressCount != 100){
				notification.contentView.setProgressBar(R.id.pb_progress, 100, progressCount, false);
				progressCount+=2;
				handler.sendEmptyMessageDelayed(0, 1000);
				mNotificationManager.notify(1, notification);
			}
		};
	};
	
	/**
	 * 版本更新
	 */
	public void getCurrentVersion() {
		BmobQuery query = new BmobQuery("t_sys_version");
		query.addWhereEqualTo("type", "Android");
		query.findObjects(context, new FindCallback() {
			@Override
			public void onSuccess(JSONArray array) {
				Gson gson = new Gson();
				if (array != null) {
					JSONObject object = array.optJSONObject(0);
					VersionBean bean = gson.fromJson(object.toString(),
							VersionBean.class);
					if (listener != null) {
						listener.requestSuccess(bean);
					}
					
					if(Util.getAppVersionName(context).equals(bean.getVersion())){
						//版本一致，无需更新
						new AlertDialog.Builder(context)
						.setMessage("检测到新版本，是否升级？")
						.setTitle("提示")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@SuppressLint("NewApi")
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										
										 notification = new Notification.Builder(context)
								         .setContentTitle("New mail from title")
								         .setContentText("升级")
								         .setSmallIcon(android.R.drawable.ic_input_add)
								         .build();
										mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

										// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
										notification.flags = Notification.FLAG_AUTO_CANCEL; // 自动终止
										notification.defaults = Notification.DEFAULT_SOUND; // 默认声音
										 
										RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_view);
										remoteViews.setProgressBar(R.id.pb_progress, 100,0, false);
										notification.contentView = remoteViews;
										
										mNotificationManager.notify(1, notification);
										handler.sendEmptyMessageDelayed(0, 1000);
										dialog.dismiss();
									}
								}).setNegativeButton("取消", null).show();
					}else{
						//提示升级
						
					}
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				if (listener != null) {
					listener.requestSuccess(null);
				}
			}
		});
	}

	private void showDialog() {
		if (!isHideProgress) {
			pd = ProgressDialog.show(context, "请求中...", "请稍后...");
			pd.setCancelable(true);
		}
	}

	private void hideDialog() {
		if (!isHideProgress) {
			pd.dismiss();
		}
	}

	public boolean isHideProgress() {
		return isHideProgress;
	}

	public void setHideProgress(boolean isHideProgress) {
		this.isHideProgress = isHideProgress;
	}

}
