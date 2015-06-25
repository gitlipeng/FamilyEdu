package com.family.familyedu.bean;

import cn.bmob.v3.BmobObject;

public class PostCommentBean extends BmobObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PostBean postsID;
	private User fromUserID;
	private String content;
	public PostBean getPostsID() {
		return postsID;
	}
	public void setPostsID(PostBean postsID) {
		this.postsID = postsID;
	}
	public User getFromUserID() {
		return fromUserID;
	}
	public void setFromUserID(User fromUserID) {
		this.fromUserID = fromUserID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
