package com.family.familyedu.bean;

import java.util.List;

public class PositionDetailData extends BaseData{
	private String action;
	private String applyCount;
	private String btnTitle;
	private String positionApplyID;
	private List<ApplyUserData> applyUserList;
	
	public List<ApplyUserData> getApplyUserList() {
		return applyUserList;
	}
	public void setApplyUserList(List<ApplyUserData> applyUserList) {
		this.applyUserList = applyUserList;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getApplyCount() {
		return applyCount;
	}
	public void setApplyCount(String applyCount) {
		this.applyCount = applyCount;
	}
	public String getBtnTitle() {
		return btnTitle;
	}
	public void setBtnTitle(String btnTitle) {
		this.btnTitle = btnTitle;
	}
	public String getPositionApplyID() {
		return positionApplyID;
	}
	public void setPositionApplyID(String positionApplyID) {
		this.positionApplyID = positionApplyID;
	}
	
}
