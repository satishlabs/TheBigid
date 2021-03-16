package com.bigid.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_POST_SHARED")
public class PostShared {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long id;
	@Column(name = "POST_ID")
	private Long postId;
	@Column(name = "USER_ID")
	private Long userId;
	@Column(name = "SHARED_BY")
	private String sharedBy;
	@Column(name = "STATUS_TYPE")
	private String statusType;
	@Column(name = "SHARED_TIME")
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
