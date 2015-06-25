package com.family.familyedu.util;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.family.familyedu.BaseApplication;
import com.family.familyedu.bean.ClassBean;
import com.family.familyedu.bean.EduLevelBean;

public class Constants {
	/**
	 * 注册接口
	 */
	public static final String FEREGISTER = "feRegister";
	/**
	 * 修改密码接口
	 */
	public static final String FECHANGEUSERPWD = "feChangeUserPwd";
	
	/**
	 * 更改个人信息接口
	 */
	public static final String FEUPDATEINFO = "feUpdateUserInfo";
	/**
	 * 发布找家教信息
	 */
	public static final String FEADDPOSITION = "feAddPosition";
	/**
	 * 职位详情接口
	 */
	public static final String FEGETPOSITIONDETAIL = "feGetPositionDetailForUser";
	/**
	 * 申请职位接口
	 */
	public static final String FEAPPLYPOSITION = "feDoApplyPosition";
	/**
	 * 取消职位接口
	 */
	public static final String FECANCLEPOSITION = "feDoCancelPositionApply";
	/**
	 * 关闭职位接口
	 */
	public static final String FECLOSEPOSITION = "feDoClosePosition";
	/**
	 * 发布交流帖子接口
	 */
	public static final String FEADDPOSTS = "feAddPosts";
	
	/**
	 * 交流帖子评论列表接口
	 */
	public static final String FEGETPOSTSDETAILFORUSER = "feGetPostsDetailForUser";
	/**
	 * 回复帖子
	 */
	public static final String FEDOREPLYPOSTS = "feDoReplyPosts";
	/**
	 * 赞帖子
	 */
	public static final String FEDOUPPOSTS = "feDoUpPosts";
	/**
	 * 倒赞帖子
	 */
	public static final String FEDODOWNPOSTS = "feDoDownPosts";
	/**
	 * 搜索家教列表接口
	 */
	public static final String FEGETFAMILYTEACHERLIST = "feGetFamilyTeacherList";
	/**
	 * 用户类型
	 */
	public static final String USERTYPE_PARENT = "10";
	public static final String USERTYPE_TEACHER = "20";
	/**
	 * 性别
	 */
	public static final String SEX_M = "M";
	public static final String SEX_W = "W";
	
	/**
	 * 本地存储名称
	 */
	public static final String PREFERENCES_NAME = "FamilyEdu";
	
	/**
	 * 科目
	 */
	public static List<String> classNameList;
	public static List<ClassBean> classList;
	/**
	 * 学历
	 */
	public static List<String> eduLevelList;
	
	/**
	 * 程序缓存目录的文件夹的名称
	 */
	public static final String APP_DIR = ".familyedu";

	public static final String CACHE_PHOTO = "avatar";// 头像
	
	public static final String VERSIONCODE = "versioncode";// 版本
	
}
