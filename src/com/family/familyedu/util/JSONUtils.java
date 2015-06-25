package com.family.familyedu.util;

import com.family.familyedu.bean.BaseData;
import com.family.familyedu.bean.PositionDetailData;
import com.family.familyedu.bean.PostCommentListData;
import com.google.gson.Gson;

public class JSONUtils {

	private static JSONUtils instance;

	public static JSONUtils getInstance() {
		if (instance == null) {
			instance = new JSONUtils();
		}
		return instance;
	}

	public BaseData parseBaseData(String jsonData, BaseData data) {
		Gson versionGson = new Gson();
		data = versionGson.fromJson(jsonData, BaseData.class);
		return data;
	}

	// 注册
	public BaseData parseRegisterData(String jsonData, BaseData data) {
		Gson versionGson = new Gson();
		data = versionGson.fromJson(jsonData, BaseData.class);
		return data;
	}

	//职位详情
	public PositionDetailData parsePositionDetailData(String jsonData,
			PositionDetailData data) {
		Gson versionGson = new Gson();
		data = versionGson.fromJson(jsonData, PositionDetailData.class);
		return data;
	}
	
	//
	public PostCommentListData parsePostCommentListData(String jsonData,
			PostCommentListData data) {
		try {
			Gson versionGson = new Gson();
			data = versionGson.fromJson(jsonData, PostCommentListData.class);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}