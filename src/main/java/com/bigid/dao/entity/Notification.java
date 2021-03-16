package com.bigid.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author satish
 *
 */
@Entity
@Table(name="NOTIFICATION")
public class Notification{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false, unique = true)
	private int id;
	@Column(name="USER_ID")
	private long userId;
	@Column(name="POST_ID")
	private long postId;
	@Column(name="NOTIFICATION_ACTIVITY")
	private Date activityTime;
	@Column(name="NOTIFICATION_TYPE")
	private String activity;
	@Column(name="STATUS")
	private String status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	public Date getActivityTime() {
		return activityTime;
	}
	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Notification [id=" + id + ", userId=" + userId + ", postId=" + postId + ", activityTime=" + activityTime
				+ ", activity=" + activity + ", status=" + status + "]";
	}	
}
