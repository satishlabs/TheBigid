package com.bigid.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER_FOLLOWE_POST")
public class UserFollowPost {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	@Column(name = "FOLLOWED_BY")
	private long follower;
	@Column(name = "FOLLOWING_TO")
	private long following;
	@Column(name = "IS_NOTIFICATION_ON")
	private boolean followNotification;
	@Column(name = "SINCE_FOLLOWING")
	private Date sinceFollowing;
	@Column(name = "POST_ID")
	private long postId;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	public long getFollower() {
		return follower;
	}
	public void setFollower(long follower) {
		this.follower = follower;
	}
	
	public long getFollowing() {
		return following;
	}
	public void setFollowing(long following) {
		this.following = following;
	}
	
	public boolean isFollowNotification() {
		return followNotification;
	}
	public void setFollowNotification(boolean followNotification) {
		this.followNotification = followNotification;
	}
	
	public Date getSinceFollowing() {
		return sinceFollowing;
	}
	public void setSinceFollowing(Date sinceFollowing) {
		this.sinceFollowing = sinceFollowing;
	}
	
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	@Override
	public String toString() {
		return "UserFollowPost [id=" + id + ", follower=" + follower + ", following=" + following
				+ ", followNotification=" + followNotification + ", sinceFollowing=" + sinceFollowing + ", postId="
				+ postId + "]";
	}

}
