package com.family.familyedu.bean;

import java.util.ArrayList;
import java.util.List;

public class PostCommentListData extends BaseData{
	private String canDoDown;
	private String canDoUp;
	private PostBean postBean;
	private String commentsSize;
	
	public String getCommentsSize() {
		return commentsSize;
	}
	public void setCommentsSize(String commentsSize) {
		this.commentsSize = commentsSize;
	}
	public PostBean getPostBean() {
		return postBean;
	}
	public void setPostBean(PostBean postBean) {
		this.postBean = postBean;
	}
	public String getCanDoDown() {
		return canDoDown;
	}
	public void setCanDoDown(String canDoDown) {
		this.canDoDown = canDoDown;
	}
	public String getCanDoUp() {
		return canDoUp;
	}
	public void setCanDoUp(String canDoUp) {
		this.canDoUp = canDoUp;
	}
	
}
