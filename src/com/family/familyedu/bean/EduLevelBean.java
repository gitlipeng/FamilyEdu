package com.family.familyedu.bean;

import cn.bmob.v3.BmobObject;

/**
 * 学历
 * @author user
 *
 */
public class EduLevelBean extends BmobObject {
	private static final long serialVersionUID = 1L;

	public EduLevelBean() {
		this.setTableName("t_eduLevel");
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
