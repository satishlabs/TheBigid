package com.bigid.web.commands.notification;

public class VoteVO{
	
	private String userName;
	private Long userId;
	private String postName;
	private Long postId;
	private int upVoteCount;
	private int downVoteCount;
	private int totalVoteCount;
	
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

	@Override
	public String toString() {
		return "VoteVO [userName=" + userName + ", userId=" + userId + ", postName=" + postName + ", postId=" + postId
				+ ", upVoteCount=" + upVoteCount + ", downVoteCount=" + downVoteCount + ", totalVoteCount="
				+ totalVoteCount + "]";
	}
	
	
	
	
	
	

}
