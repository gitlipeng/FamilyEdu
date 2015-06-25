package com.family.familyedu.bean;

import cn.bmob.v3.BmobObject;

/**
 * 科目
 * @author user
 *
 */
public class ClassBean extends BmobObject {
	private static final long serialVersionUID = 1L;

	public ClassBean() {
		this.setTableName("t_class");
	}

	private String name;
	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
