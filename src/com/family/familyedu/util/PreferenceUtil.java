package com.family.familyedu.util;

import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * 本地数据存储类. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2013-1-31 下午7:53:29
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author dingyj@c-platform.com
 * @version 1.0.0
 */
public class PreferenceUtil {
	
	public static int getValue(Context context, String node, String key,
			int defaultValue) {
		return context.getSharedPreferences(node, Context.MODE_PRIVATE).getInt(
				key, defaultValue);
	}

	public static String getValue(Context context, String node, String key,
			String defaultValue) {
		return context.getSharedPreferences(node, Context.MODE_PRIVATE)
				.getString(key, defaultValue);
	}

	public static boolean getValue(Context context, String node, String key,
			boolean defaultValue) {
		return context.getSharedPreferences(node, Context.MODE_PRIVATE)
				.getBoolean(key, defaultValue);
	}

	public static void saveValue(Context context, String node, String key,
			String value) {
		SharedPreferences.Editor sp = context.getSharedPreferences(node,
				Context.MODE_PRIVATE).edit();
		sp.putString(key, value);
		sp.commit();
	}

	public static void saveValue(Context context, String node, String key,
			boolean value) {
		SharedPreferences.Editor sp = context.getSharedPreferences(node,
				Context.MODE_PRIVATE).edit();
		sp.putBoolean(key, value);
		sp.commit();
	}

	public static void saveValue(Context context, String node, String key,
			int value) {
		SharedPreferences.Editor sp = context.getSharedPreferences(node,
				Context.MODE_PRIVATE).edit();
		sp.putInt(key, value);
		sp.commit();
	}
	
	public static boolean isItemViewType(int param, TreeSet<Integer> treeSet) {
		if (treeSet.contains(param)) {
			return true;
		} else {
			return false;
		}
	}
}
