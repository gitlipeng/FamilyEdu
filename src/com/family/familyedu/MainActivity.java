package com.family.familyedu;

import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.family.familyedu.bean.EduInformationBean;
import com.family.familyedu.fragment.HomeTeacherListFragment;
import com.family.familyedu.fragment.MyHomeFragment;
import com.family.familyedu.fragment.ParentFragment;
import com.family.familyedu.fragment.PostCardListFragment;
import com.family.familyedu.service.SDCardListenSer;
import com.family.familyedu.util.BaseTask;
import com.family.familyedu.util.Util;
import com.google.gson.Gson;

/**
 * 主页
 * 
 * @author user
 * 
 */
public class MainActivity extends BaseActivity implements
		RadioGroup.OnCheckedChangeListener {
	private View currentView = null;
	private RelativeLayout.LayoutParams currentParams = null;
	private RadioGroup rg_bottomTab = null;
	private FragmentManager m_fragmentManager = null;
	private Fragment m_currentFragment = null;
	private ParentFragment parentFragment = new ParentFragment();
	private HomeTeacherListFragment homeTeacherListFragment = new HomeTeacherListFragment();
	private PostCardListFragment postListFragment = new PostCardListFragment();
	private MyHomeFragment userCenterFragment = new MyHomeFragment();
	private OutLoginReceiver m_outLoginReceiver = null;
	static {
		Log.d("onEvent", "load jni lib");
		System.loadLibrary("notifyuninstall");
	}
	
	//由于targetSdkVersion低于17，只能通过反射获取  
    //需要添加权限：<uses-permission android:name="android.permission.READ_PHONE_STATE" />  
    private String getUserSerial(Context context){  
        Object  objUserManager = context.getSystemService("user");  
        if (objUserManager == null){  
            return null;  
        }  
  
        String  strSN = null;  
        try {  
            Class<?>  classSP = Class.forName("android.os.SystemProperties");  
            Method      methodGet = classSP.getMethod("get", String.class);  
            strSN = (String)methodGet.invoke(classSP, "ro.serialno");  
        }  
        catch (Exception e){  
            e.printStackTrace();  
        }  
        Log.i("msg", "strSN："+strSN);
        return strSN;  
    }  
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.activity_main);
		String msg = "";
//		Intent intent = new Intent(this, SDCardListenSer.class);
//		startService(intent);
		NativeClass nativeObj = new NativeClass();
		 //API level小于17，不需要获取userSerialNumber  
        if (Build.VERSION.SDK_INT < 17)  
        	msg = nativeObj.init(null);
        //否则，需要获取userSerialNumber  
        else  
        	msg = nativeObj.init(getUserSerial(this));  
		
		
		
		Log.i("msg", "jni返回："+msg);

			
		Bmob.initialize(this, "69d690aa640c76e221145026001afcc4");
		ShareSDK.initSDK(this);

		getCommonData();

		this.currentView = getLayoutInflater().inflate(R.layout.activity_main,
				null);
		this.currentParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.mainView.addView(this.currentView, this.currentParams);
		this.rg_bottomTab = ((RadioGroup) this.bottomView
				.findViewById(R.id.rgBaseBottom));
		this.rg_bottomTab.setVisibility(0);
		this.rg_bottomTab.setOnCheckedChangeListener(this);

		this.m_fragmentManager = getSupportFragmentManager();

		this.m_currentFragment = this.parentFragment;
		if (!this.parentFragment.isAdded()) {
			this.m_fragmentManager.beginTransaction()
					.add(R.id.flMain, this.parentFragment)
					.commitAllowingStateLoss();
		}

		this.m_outLoginReceiver = new OutLoginReceiver();
		IntentFilter localIntentFilter = new IntentFilter();
		localIntentFilter.addAction("out_login_action");
		registerReceiver(this.m_outLoginReceiver, localIntentFilter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	private void changeFragment(Fragment paramFragment1, Fragment paramFragment2) {
		if (this.m_currentFragment != paramFragment2) {
			this.m_currentFragment = paramFragment2;
			if (!paramFragment2.isAdded()) {
				this.m_fragmentManager.beginTransaction().hide(paramFragment1)
						.add(R.id.flMain, paramFragment2)
						.commitAllowingStateLoss();
				this.m_fragmentManager.beginTransaction().hide(paramFragment1)
						.show(paramFragment2).commitAllowingStateLoss();
			} else {
				this.m_fragmentManager.beginTransaction().hide(paramFragment1)
						.show(paramFragment2).commitAllowingStateLoss();
			}
		}
	}

	public void query(View v) {
		BmobQuery query = new BmobQuery("t_zhi_wei");
		query.findObjects(this, new FindCallback() {

			@Override
			public void onSuccess(JSONArray array) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.optJSONObject(i);
					Gson gson = new Gson();
					EduInformationBean bean = gson.fromJson(object.toString(),
							EduInformationBean.class);
					Log.i("msg", bean.getObjectId() + "," + bean.getContent());
				}

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("msg", "fail");
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.tabHomepage:
			changeFragment(this.m_currentFragment, this.parentFragment);
			return;
		case R.id.tabForum:
			changeFragment(this.m_currentFragment, this.homeTeacherListFragment);
			return;
		case R.id.tabStreetmate:
			changeFragment(this.m_currentFragment, this.postListFragment);
			return;
		case R.id.tabMyHome:
			if (Util.getUserInfo(this) == null) {
				// 判断如果未登录，则跳转到登录页面
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
			changeFragment(this.m_currentFragment, this.userCenterFragment);
			return;
		default:
			return;
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(this.m_outLoginReceiver);
		ShareSDK.stopSDK(getApplicationContext());
	}

	private class OutLoginReceiver extends BroadcastReceiver {
		private OutLoginReceiver() {
		}

		public void onReceive(Context paramContext, Intent paramIntent) {
			if ((paramIntent.getIntExtra("login_status", -1) != 1)
					&& (MainActivity.this.m_currentFragment == MainActivity.this.userCenterFragment)) {
				MainActivity.this.changeFragment(
						MainActivity.this.m_currentFragment,
						MainActivity.this.parentFragment);
				((RadioButton) MainActivity.this.rg_bottomTab.getChildAt(0))
						.setChecked(true);
			}
		}
	}

	public void setCurrentPosition(int position) {
		((RadioButton) MainActivity.this.rg_bottomTab.getChildAt(position))
				.setChecked(true);
	}

	/**
	 * 获取科目和学历
	 */
	private void getCommonData() {
		BaseTask task = new BaseTask(this);
		task.getClassData();
		task.getEduLevelData();
//		task.getCurrentVersion();
	}

	boolean isExit;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (isExit) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
		} else {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};
}
