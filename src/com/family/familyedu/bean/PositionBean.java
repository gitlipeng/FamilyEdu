package com.family.familyedu.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;


/**
 * 职位Bean
 * @author user
 *
 */
public class PositionBean extends BmobObject implements Serializable{
	private static final long serialVersionUID = 1L;

	public PositionBean() {
		this.setTableName("t_position");
	}
	private String title;
	private String type;
	private User fromUserID;
	private String price;
	private String duration;
	private String content;
	private String area;
	private ClassBean learnClassID;
	private String status;

	public User getFromUserID() {
		return fromUserID;
	}
	public void setFromUserID(User fromUserID) {
		this.fromUserID = fromUserID;
	}
	public ClassBean getLearnClassID() {
		return learnClassID;
	}
	public void setLearnClassID(ClassBean learnClassID) {
		this.learnClassID = learnClassID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
