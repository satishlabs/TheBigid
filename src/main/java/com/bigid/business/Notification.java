package com.bigid.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.bigid.dao.entity.Post;
import com.bigid.dao.entity.User;
import com.bigid.repository.PostRepository;
import com.bigid.repository.UserRepository;



/**
 * @author satish
 *
 */

public class Notification {
	private static final long serialVersionUID = 1L;
	private Long id;
	private long userId;
	private long postId;
	private Date activityTime;
	private String activity;
	private String status;

	
	
	public static Notification newInstance(Long userId,Long postId,String activity){
		Notification notification = new Notification();

		
		notification.setStatus("NEW");
		notification.setActivityTime(new Date());
		notification.setPostId(postId);
		notification.setUserId(userId);
		notification.setActivity(activity);
		return notification;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	
}
