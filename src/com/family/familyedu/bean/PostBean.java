package com.family.familyedu.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
/**
 * 交流帖子
 * @author user
 *
 */
public class PostBean extends BmobObject implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 赞的数目
	 */
	private String upCount;
	/**
	 * 赞的人
	 */
	private String upUsers;
	/**
	 * 倒赞的数目
	 */
	private String downCount;
	/**
	 * 倒赞的人
	 */
	private String dowmUsers;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 发布者id
	 */
	private User fromUserID ;
	
	public PostBean() {
		 this.setTableName("t_posts");
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpCount() {
		return upCount;
	}
	public void setUpCount(String upCount) {
		this.upCount = upCount;
	}
	public String getUpUsers() {
		return upUsers;
	}
	public void setUpUsers(String upUsers) {
		this.upUsers = upUsers;
	}
	public String getDownCount() {
		return downCount;
	}
	public void setDownCount(String downCount) {
		this.downCount = downCount;
	}
	public String getDowmUsers() {
		return dowmUsers;
	}
	public void setDowmUsers(String dowmUsers) {
		this.dowmUsers = dowmUsers;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public User getFromUserID() {
		return fromUserID;
	}
	public void setFromUserID(User fromUserID) {
		this.fromUserID = fromUserID;
	}
	
}
