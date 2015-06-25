package com.family.familyedu.util;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.family.familyedu.BaseApplication;
import com.family.familyedu.R;
import com.family.familyedu.bean.User;

public class Util {

	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
	/**
	 * ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br> 请看“ShareSDK
	 * 使用说明文档”，SDK下载目录中 </br> 或者看网络集成文档
	 * http://wiki.mob.com/Android_%E5%BF%AB%E9%
	 * 80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 * 
	 * 
	 * 平台配置信息有三种方式： 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc
	 * /cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 */
	public static void showShare(Context context, boolean silent,
			String platform, boolean captureView, View view) {
		final OnekeyShare oks = new OnekeyShare();

		oks.setNotification(R.drawable.ic_launcher,
				context.getString(R.string.app_name));
		// oks.setAddress("12345678901");
		oks.setText("text");
		oks.setLatitude(23.056081f);
		oks.setLongitude(113.385708f);
		oks.setSilent(silent);
		oks.setShareFromQQAuthSupport(true);
		if (platform != null) {
			oks.setPlatform(platform);
		}

		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();

		// 在自动授权时可以禁用SSO方式
		// if(!CustomShareFieldsPage.getBoolean("enableSSO", true))
		// oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		// oks.setCallback(new OneKeyShareCallback());

		// 去自定义不同平台的字段内容
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());

		// 去除注释，演示在九宫格设置自定义的图标
		// Bitmap logo = BitmapFactory.decodeResource(menu.getResources(),
		// R.drawable.ic_launcher);
		// String label = menu.getResources().getString(R.string.app_name);
		// OnClickListener listener = new OnClickListener() {
		// public void onClick(View v) {
		// String text = "Customer Logo -- ShareSDK " +
		// ShareSDK.getSDKVersionName();
		// Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
		// oks.finish();
		// }
		// };
		// oks.setCustomerLogo(logo, label, listener);

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
		// oks.addHiddenPlatform(SinaWeibo.NAME);
		// oks.addHiddenPlatform(TencentWeibo.NAME);

		// 为EditPage设置一个背景的View
		oks.setEditPageBackground(view);

		// 设置kakaoTalk分享链接时，点击分享信息时，如果应用不存在，跳转到应用的下载地址
		oks.setInstallUrl("http://www.mob.com");
		// 设置kakaoTalk分享链接时，点击分享信息时，如果应用存在，打开相应的app
		oks.setExecuteUrl("kakaoTalkTest://starActivity");

		oks.show(context);
	}

	public static void showLToast(Context context, String paramString) {
		if (paramString == null)
			return;
		Toast.makeText(context, paramString, 1).show();
	}

	public static void showLToast(Context context, int paramInt) {
		Toast.makeText(context, paramInt, 1).show();
	}

	public static void showSToast(Context context, String paramString) {
		if (paramString == null)
			return;
		Toast.makeText(context, paramString, 0).show();
	}

	public static void showSToast(Context context, int paramInt) {
		Toast.makeText(context, paramInt, 1).show();
	}

	/**
	 * 获取错误提示
	 * 
	 * @param error
	 *            错误提示
	 * @return spanned
	 */
	public static Spanned getTextError(String error) {
		return Html.fromHtml("<font color=#808183>" + error + "</font>");
	}

	public static Boolean checkPasswordNum(String password) {
		return true;
	}

	public static void savePassword(String password) {
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.PASSWORD, password);
	}

	public static String getPassword() {
		return PreferenceUtil.getValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.PASSWORD, "");
	}

	public static void saveUserInfo(User bean) {
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.OBJECTID,
				bean.getObjectId());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.USERNAME,
				bean.getUsername());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.PASSWORD,
				bean.getPassword());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.NICKNAME,
				bean.getNickname());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.ADDRESS,
				bean.getAddress());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.BIRTHDAY,
				bean.getBirthday());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.EDULEVEL,
				bean.getEduLevel());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.PROFESSION,
				bean.getProfession());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.SEX, bean.getSex());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.SKILLEDCLASS,
				bean.getSkilledClass());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.TEL, bean.getTel());
		PreferenceUtil.saveValue(BaseApplication.getInstance(),
				Constants.PREFERENCES_NAME, AccountInfo.USERTYPE,
				bean.getUserType());
	}

	// public static UserBean getUserInfo(){
	// UserBean bean = new UserBean();
	// Context context = BaseApplication.getInstance();
	// String name = Constants.PREFERENCES_NAME;
	// bean.setObjectId(PreferenceUtil.getValue(context, name,
	// AccountInfo.OBJECTID, ""););
	// bean.setUsername();
	// bean.setPassword();
	// bean.setNickname();
	// bean.setAddress();

	// return userInfo;
	// }
	public static User getUserInfo(Context context) {
		User userInfo = BmobUser.getCurrentUser(context, User.class);
		return userInfo;
	}

	public static String getUserId(Context context) {
		if (getUserInfo(context) == null) {
			return "";
		} else {
			return getUserInfo(context).getObjectId();
		}
	}

	public static void Logout(Context context) {
		BmobUser.logOut(context); // 清除缓存用户对象
		BaseApplication.getInstance().logout(null);
		savePassword("");// 将密码置空
	}

	/**
	 * 放大
	 * 
	 * @return
	 */
	public static AnimationSet getCommonScaleIn() {
		AnimationSet localAnimationSet = new AnimationSet(true);
		ScaleAnimation localScaleAnimation = new ScaleAnimation(1.0F, 1.3F,
				1.0F, 1.3F, 1, 0.5F, 1, 0.5F);
		localScaleAnimation.setDuration(400L);
		localAnimationSet.addAnimation(localScaleAnimation);
		return localAnimationSet;
	}

	/**
	 * 缩小
	 * 
	 * @return
	 */
	public static Animation getCommonScaleOut() {
		AnimationSet localAnimationSet = new AnimationSet(true);
		ScaleAnimation localScaleAnimation = new ScaleAnimation(1.3F, 1.0F,
				1.3F, 1.0F, 1, 0.5F, 1, 0.5F);
		localScaleAnimation.setDuration(400L);
		localAnimationSet.addAnimation(localScaleAnimation);
		return localScaleAnimation;
	}

	/**
	 * 获取缓存路径
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件名
	 * @return 路径
	 */
	public static String getCachePath(final Context context, final String path) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File sdFile = Environment.getExternalStorageDirectory();
			return sdFile.getPath() + "/" + Constants.APP_DIR + "/" + path;
		} else {
			File file;
			if (context == null) {
				Log.i("msg", "getFilesDir == null");
				file = BaseApplication.getInstance().getFilesDir();
			} else {
				file = context.getFilesDir();
			}

			return file.getPath() + "/" + path;
		}
	}

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static void showShare(int myShareType, boolean silent,
			String platform, final String text, String pathImage,
			final String urlImage, final String urlId, Context context,
			View view) {
		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher, context.getResources()
				.getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("家教");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/aaa.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		oks.setSiteUrl("http://sharesdk.cn");

		// 当平台为朋友圈时把他的内容设为标题
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if (SinaWeibo.NAME.equals(platform.getName())) {

				} else if (WechatMoments.NAME.equals(platform.getName())) {
					paramsToShare.setText(text);
				} else if (QZone.NAME.equals(platform.getName())) {
					paramsToShare.setTitle("家教");
					paramsToShare.setTitleUrl("http://www.baidu.com");
					paramsToShare.setSite("家教");
					paramsToShare.setSiteUrl("http://www.baidu.com");
				} else if (QQ.NAME.equals(platform.getName())) {

					paramsToShare.setTitle("家教");
					paramsToShare.setTitleUrl("http://www.baidu.com");
					paramsToShare.setText(text);
				}
			}
		});

		// 启动分享GUI
		oks.show(context);
	}
	
	public static void showShare(final Context context,String paramString){
		OnekeyShare m_oneKeyShare = new OnekeyShare();
	      m_oneKeyShare.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if (platform.getName().equals(SinaWeibo.NAME))
				      Util.showSToast(context,"成功分享到新浪微博");
			}
		});
	      m_oneKeyShare.setAddress("");
	      m_oneKeyShare.setUrl("http://www.baidu.com");
	      m_oneKeyShare.setTitle("小伙伴请无视我的测试-Title");
	      m_oneKeyShare.setTitleUrl("http://weibo.com/codingfly/home");
	      m_oneKeyShare.setText("小伙伴请无视我的测试");
//	      m_oneKeyShare.setImagePath("/sdcard/aaa.jpg");// 确保SDcard下面存在此张图片
	      m_oneKeyShare.setImageUrl("http://pic.hualongxiang.com/images/wxsharelogo.png");
	      m_oneKeyShare.setSilent(false);
	      m_oneKeyShare.setPlatform(paramString);
	      m_oneKeyShare.setSite(context.getString(R.string.app_name));
	      m_oneKeyShare.show(context);
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}
}
