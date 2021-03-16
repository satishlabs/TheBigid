package com.bigid.web.commands.notification;

import java.util.Date;

public class NSFWNotification extends BaseNotification {
	private int id;
	private Long userId;
	private String userName;
	private Long postId;
	private String postName;
	private Long byUserId;
	private String byUserName;
	private Date notifiedTime;
	private String notificationStatus;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public Long getByUserId() {
		return byUserId;
	}
	public void setByUserId(Long byUserId) {
		this.byUserId = byUserId;
	}
	public String getByUserName() {
		return byUserName;
	}
	public void setByUserName(String byUserName) {
		this.byUserName = byUserName;
	}
	public Date getNotifiedTime() {
		return notifiedTime;
	}
	public void setNotifiedTime(Date notifiedTime) {
		this.notifiedTime = notifiedTime;
	}
	public String getNotificationStatus() {
		return notificationStatus;
	}
	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}
	@Override
	public String toString() {
		return "NSFWNotification [id=" + id + ", userId=" + userId + ", userName=" + userName + ", postId=" + postId
				+ ", postName=" + postName + ", byUserId=" + byUserId + ", byUserName=" + byUserName + ", notifiedTime="
				+ notifiedTime + ", notificationStatus=" + notificationStatus + "]";
	}
	
	
}
