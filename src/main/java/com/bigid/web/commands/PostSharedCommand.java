package com.bigid.web.commands;

import java.util.Date;

public class PostSharedCommand {
	
	private long id;
	private Long postId;
	private Long userId;
	private String sharedBy;
	private String statusType;
	private Date sharedTime;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public Date getSharedTime() {
		return sharedTime;
	}
	public void setSharedTime(Date sharedTime) {
		this.sharedTime = sharedTime;
	}
	public String getSharedBy() {
		return sharedBy;
	}
	public void setSharedBy(String sharedBy) {
		this.sharedBy = sharedBy;
	}

}
