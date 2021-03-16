package com.bigid.web.commands.notification;

import java.util.Date;

public class VoteNotification extends BaseNotification{
	
	private String userName;
	private Long userId;
	private String postName;
	private Long postId;
	private int upVoteCount;
	private int downVoteCount;
	private int totalVoteCount;
	private Date notifiedTime;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public int getUpVoteCount() {
		return upVoteCount;
	}

	public void setUpVoteCount(int upVoteCount) {
		this.upVoteCount = upVoteCount;
	}

	public int getDownVoteCount() {
		return downVoteCount;
	}
	public void setDownVoteCount(int downVoteCount) {
		this.downVoteCount = downVoteCount;
	}
	public int getTotalVoteCount() {
		return totalVoteCount;
	}
	public void setTotalVoteCount(int totalVoteCount) {
		this.totalVoteCount = totalVoteCount;
	}

	public Date getNotifiedTime() {
		return notifiedTime;
	}

	public void setNotifiedTime(Date notifiedTime) {
		this.notifiedTime = notifiedTime;
	}

	@Override
	public String toString() {
		return "VoteNotification [userName=" + userName + ", userId=" + userId + ", postName=" + postName + ", postId="
				+ postId + ", upVoteCount=" + upVoteCount + ", downVoteCount=" + downVoteCount + ", totalVoteCount="
				+ totalVoteCount + ", notifiedTime=" + notifiedTime + "]";
	}

}
