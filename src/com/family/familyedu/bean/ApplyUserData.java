package com.family.familyedu.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class ApplyUserData extends BmobObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private User applyUserID;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User getApplyUserID() {
		return applyUserID;
	}
	public void setApplyUserID(User applyUserID) {
		this.applyUserID = applyUserID;
	}

	
}

