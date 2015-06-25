package com.family.familyedu.bean;

import cn.bmob.v3.BmobObject;

public class VersionBean extends BmobObject{
	private static final long serialVersionUID = 1L;
	private String version;
	private String type;
	private String status;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
