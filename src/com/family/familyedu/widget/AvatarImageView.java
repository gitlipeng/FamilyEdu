package com.family.familyedu.widget;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.family.familyedu.BaseActivity;
import com.family.familyedu.R;
import com.family.familyedu.inter.UploadAvatarListener;
import com.family.familyedu.util.AccountInfo;
import com.family.familyedu.util.Base64;
import com.family.familyedu.util.Constants;
import com.family.familyedu.util.FileCache;
import com.family.familyedu.util.ImagePathUtil;
import com.family.familyedu.util.PreferenceUtil;
import com.family.familyedu.util.Util;

public class AvatarImageView extends ImageView {

	/**
	 * 选择本地照片返回标识
	 */
	public static final int IMAGE_REQUEST_CODE = 200;

	/**
	 * 选择拍照返回标识
	 */
	public static final int CAMERA_REQUEST_CODE = 201;

	/**
	 * 裁剪图片返回标识
	 */
	public static final int RESULT_REQUEST_CODE = 202;
	
	/**
	 * 选择本地照片返回标识,android4.4以上版本，返回做不同的处理
	 */
	public static final int IMAGE_KITKAT_REQUEST_CODE = 203;

	private Context context;

	private BaseActivity activity;

	private String[] dialogItems;

	/**
	 * 拍照默认图片名称包括绝对路径
	 */
	public String iamgeFileName;

	/**
	 * 头像图片临时路径
	 */
	private String tempImagePath;

	/**
	 * 头像图片真实路径
	 */
	private String imagePath;

	/**
	 * 修改昵称任务
	 */
//	private HttpTask task;

	/**
	 * 请求id
	 */
	private static final int MODIFY_ID = 0;

	private UploadAvatarListener listener;

	Bitmap photoImage;

	public AvatarImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		dialogItems = context.getResources().getStringArray(R.array.select_upload_dialog_items);
		tempImagePath = Util.getCachePath(context, Constants.CACHE_PHOTO) + "/" + Util.getUserId(context) + "_temp" + ".jpg";
		onResume();
	}

	public void onResume() {
		if (Util.getUserId(context) != null && !"".equals(Util.getUserId(context))) {
			imagePath = Util.getCachePath(context, Constants.CACHE_PHOTO) + "/" + Util.getUserId(context) + ".jpg";

			Bitmap avatar = FileCache.newInstance(context).getBitmap(imagePath);
			if (avatar != null) {
				avatar = Util.toRoundBitmap(avatar);
				this.setImageBitmap(avatar);
			} else {
				avatar = BitmapFactory.decodeResource(getResources(), R.drawable.photo_default);
				this.setImageBitmap(Util.toRoundBitmap(avatar));
			}
		}
	}

	public void onClick(BaseActivity activity, UploadAvatarListener listener) {
		this.activity = activity;
		this.listener = listener;
		tempImagePath = Util.getCachePath(context, Constants.CACHE_PHOTO) + "/" + Util.getUserId(context) + "_temp" + ".jpg";
		imagePath = Util.getCachePath(context, Constants.CACHE_PHOTO) + "/" + Util.getUserId(context) + ".jpg";
		showUserIconDialog();
	}

	public void showUserIconDialog() {
		// if (!Util.checkEnvironment(context)) {
		// return;
		// }
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("上传照片");
		builder.setItems(dialogItems, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					pickFromCamera();
					return;
				}
				if (which == 1) {
					pickFromGallery();
				}
			}
		}).setCancelable(true);
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		if (dialog != null) {
			dialog.show();
		}
	}

	/**
	 * 相机拍照
	 */
	private void pickFromCamera() {
		String mCurrentPicName = "mo" + System.currentTimeMillis() + ".jpg";
		iamgeFileName = Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + mCurrentPicName;
		File dir = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "Camera");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(dir, mCurrentPicName);

		intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		activity.startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
	}

	/**
	 * 相册选取
	 */
	private void pickFromGallery() {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*");
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.startActivityForResult(intentFromGallery, IMAGE_KITKAT_REQUEST_CODE);
		}else{
			activity.startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case Activity.RESULT_OK:
				switch (requestCode) {
					case IMAGE_REQUEST_CODE:
						if (data != null) {
							startPhotoZoom(data.getData());
						}
						break;
					case IMAGE_KITKAT_REQUEST_CODE:
						if (data != null) {
							Uri uri = data.getData();
							String fileName = ImagePathUtil.getPath(context, uri);
							if(fileName != null){
								File tempFile = new File(fileName);
								startPhotoZoom(Uri.fromFile(tempFile));
							}else{
								Log.e("msg", "获取图片出错");
							}
						}
						break;
					case CAMERA_REQUEST_CODE:
						File tempFile = new File(iamgeFileName);
						startPhotoZoom(Uri.fromFile(tempFile));
						break;
					case RESULT_REQUEST_CODE:
						if (data != null) {
							getImageToView(data);
						}
						break;
					default:
						break;
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Log.d("Test", "startPhotoZoom:" + uri);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			try {
				Bitmap photo = extras.getParcelable("data");
				File tempFile = new File(tempImagePath);
				if (!tempFile.getParentFile().exists()) {
					tempFile.getParentFile().mkdirs();
				}

				if (!tempFile.exists()) {
					try {
						tempFile.createNewFile();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				// 保存头像到本地
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
				photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();

				photoImage = Util.toRoundBitmap(photo);
				// 上传头像到服务器
				modifyAvatar(photo);
			}
			catch (Exception e) {
				Log.w(activity.getClass().getName(), e);
			}
		}
	}

	/**
	 * 上传头像到服务器
	 * 
	 * @param photo
	 */
	private void modifyAvatar(Bitmap bitmap) {
		try {
//			ByteArrayOutputStream bao = new ByteArrayOutputStream();
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
//			byte[] bt = bao.toByteArray();
//			String mPhotoEncode = new String(Base64.encode(bt), "UTF-8");
//
//			String kw = Util.getVerifyString();
//			String sid = PreferenceUtil.getValue(context, Constants.PREFERENCES_NAME, Constants.SID, "");
//			JSONObject data = new JSONObject();
//
//			data.put("id", AccountInfo.mallUserId);
//			data.put("type", "1");// 1.头像 2.其他
//			data.put("avatar", mPhotoEncode);
//			data.put("userName", TextUtils.isEmpty(AccountInfo.terminalId) ? AccountInfo.email : AccountInfo.terminalId);
//			data.put(Constants.VERSION, Util.getVersionName(context));
//			data.put("IMEI", new PhoneUtil(context).getDeviceId());
//
//			if (task != null) {
//				task.cancel(true);
//			}
//			task = new HttpTask(MODIFY_ID, this);
//			task.execute(Constants.URI_MODIFY_PROFILE, data.toString(), kw, sid);
//			activity.showInfoProgressDialog("头像上传中，请稍后...");
		}
//		catch (JSONException e) {
//			Log.w(activity.getClass().getName(), e);
//		}
		catch (Exception e) {
			Log.w(activity.getClass().getName(), e);
		}
	}

//	@Override
//	public void onSuccess(int id, JSONObject json) {
//		if (id == MODIFY_ID) {
//			activity.hideInfoProgressDialog();
//			String flag = json.optString("flag");
//			if ("00-00".equals(flag)) {
//				AccountInfo.userPhoto = json.optString(Constants.USER_PHOTO, "");
//				PreferenceUtil.saveValue(context, Constants.PREFERENCES_NAME, Constants.USER_PHOTO, AccountInfo.userPhoto);
//				if (listener != null) {
//					listener.uploadAvatarSuccess();
//				}
//				activity.showToast(R.string.modifyavatar_success_msg);
//
//				// 设置成功后的头像
//				if (photoImage != null) {
//					this.setImageBitmap(photoImage);
//					// 将临时图片更名为真实图片
//					File tempImageFile = new File(tempImagePath);
//					if (tempImageFile.exists()) {
//						tempImageFile.renameTo(new File(imagePath));
//					}
//				}
//			} else {
//				activity.showToast(R.string.error_msg_26);
//			}
//		}
//	}

//	@Override
//	public void onException(int id) {
//		// 如果当前页面正在被销毁停止执行
//		if (activity.isFinishing()) {
//			return;
//		}
//		if (id == MODIFY_ID) {
//			activity.hideInfoProgressDialog();
//			boolean isNetwork = Util.isNetworkAvailable(activity.getApplicationContext());
//			if (!isNetwork) {
//				activity.showToast(R.string.connect_server_failed);
//			} else {
//				activity.showToast("保存失败，请稍后再试");
//			}
//		}
//	}

	public void loadFromUrl(String url) {
//		if (!TextUtils.isEmpty(url)) {
//			AsyncImageLoader.getInstance(context).getBitmapFromNet(url, new ImageLoaderListener() {
//
//				@Override
//				public void loadSuccess(Bitmap bitmap) {
//					if (bitmap != null) {
//						AvatarImageView.this.setImageBitmap(Util.toRoundBitmap(bitmap));
//						imagePath = Util.getCachePath(context, Constants.CACHE_PHOTO) + "/" + Util.getUserId(context) + ".jpg";
//						FileCache.newInstance(context).putBitmap(imagePath, bitmap);
//					}
//				}
//
//				@Override
//				public void loadFail() {
//				}
//			});
//		}
	}
}