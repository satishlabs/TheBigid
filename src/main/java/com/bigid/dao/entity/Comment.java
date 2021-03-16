package com.bigid.dao.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER_COMMENT")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long commentId;
	@Column(name = "COMMENTED_BY")
	private String commentPostedBy;
	@Column(name = "COMMENT_CONTENT")
	private String commentContent;
	@Column(name = "COMMENT_LIKES")
	private int commentLikes;
	@Column(name = "COMMENTED_TIME")
	private Date commentPostedTime;
	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "POST_ID")
	private long postId;
	@OneToMany(cascade = CascadeType.ALL)
	private List<User> likedBy;
	@Column(name = "IS_NOTIFICATION")
	private boolean commentNotification;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Reply> replies;
	@OneToMany(cascade = CascadeType.ALL)
	private List<PostCommentVote> votes;


	public List<PostCommentVote> getCommentVotes() {
		return votes;
	}
	public void setCommentVotes(List<PostCommentVote> votes) {
		this.votes = votes;
	}
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
//	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	public String getCommentPostedBy() {
		return commentPostedBy;
	}
	public void setCommentPostedBy(String commentPostedBy) {
		this.commentPostedBy = commentPostedBy;
	}	
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public int getCommentLikes() {
		return commentLikes;
	}
	public void setCommentLikes(int commentLikes) {
		this.commentLikes = commentLikes;
	}
	public Date getCommentPostedTime() {
		return commentPostedTime;
	}
	public void setCommentPostedTime(Date commentPostedTime) {
		this.commentPostedTime = commentPostedTime;
	}
	public List<User> getLikedBy() {
		return likedBy;
	}
	public void setLikedBy(List<User> likedBy) {
		this.likedBy = likedBy;
	}
	public List<Reply> getReplies() {
		return replies;
	}
	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	public boolean isCommentNotification() {
		return commentNotification;
	}
	public void setCommentNotification(boolean commentNotification) {
		this.commentNotification = commentNotification;
	}

}
