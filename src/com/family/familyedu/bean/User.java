package com.family.familyedu.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
	private static final long serialVersionUID = 1L;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 性别”M””W”
	 */
	private String sex;
	/**
	 * 出生年月1988-01
	 */
	private String birthday;
	/**
	 * 联系电话
	 */
	private String tel;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 用户类型 10家长 20家教
	 */
	private String userType;
	/**
	 * 学历
	 */
	private String eduLevel;
	/**
	 * 擅长的科目
	 */
	private String skilledClass;
	/**
	 * 职业
	 */
	private String profession;
	/**
	 * 年龄
	 */
	private String age;
	
	/**
	 * 辅导年级
	 */
	private String eduClass;

	public String getEduClass() {
		return eduClass;
	}

	public void setEduClass(String eduClass) {
		this.eduClass = eduClass;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEduLevel() {
		return eduLevel;
	}

	public void setEduLevel(String eduLevel) {
		this.eduLevel = eduLevel;
	}

	public String getSkilledClass() {
		return skilledClass;
	}

	public void setSkilledClass(String skilledClass) {
		this.skilledClass = skilledClass;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

}
