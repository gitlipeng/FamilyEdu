package com.family.familyedu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.family.familyedu.adapter.ShareAdapter;
import com.family.familyedu.bean.ShareBean;
import com.family.familyedu.util.Util;
import com.family.familyedu.widget.SelectPopupWindow;
import com.test.woshifangnu.db.UserDao;
import com.test.woshifangnu.domin.User;
import com.test.woshifangnu.util.Constant;

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

    protected void loginHX(final String name,final String password) {
        EMChatManager.getInstance().login(name.toString(), password.toString(), new EMCallBack() {

            @Override
            public void onSuccess() {
                // 登陆成功，保存用户名密码
                BaseApplication.getInstance().setUserName(name.toString());
                BaseApplication.getInstance().setPassword(password.toString());

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    // conversations in case we are auto login
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    // 处理好友和群组
                    processContactsAndGroups();
                    // 此方法传入一个字符串String类型的参数，返回成功或失败的一个Boolean类型的返回值
                    EMChatManager.getInstance().updateCurrentUserNick(name);
                    hideDialog();
                    loginSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
                            BaseApplication.getInstance().logout(null);
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance()
                        .updateCurrentUserNick(
                                BaseApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
            }
        });
    }

    private void processContactsAndGroups() throws EaseMobException {
        // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
        List<String> usernames = EMContactManager.getInstance()
                .getContactUserNames();
        EMLog.d("roster", "contacts size: " + usernames.size());
        Map<String, User> userlist = new HashMap<String, User>();
        for (String username : usernames) {
            User user = new User();
            user.setUsername(username);
            userlist.put(username, user);
        }
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = "Application_and_notify";
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = "group_chat";
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 存入内存
        BaseApplication.getInstance().setContactList(userlist);
        System.out.println("----------------" + userlist.values().toString());
        // 存入db
        UserDao dao = new UserDao(this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);

        // 获取黑名单列表
        List<String> blackList = EMContactManager.getInstance()
                .getBlackListUsernamesFromServer();
        // 保存黑名单
        EMContactManager.getInstance().saveBlackList(blackList);

        // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
        EMGroupManager.getInstance().getGroupsFromServer();
    }

    protected void loginSuccess(){

    }
}
