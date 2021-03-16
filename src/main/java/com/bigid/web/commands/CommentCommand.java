package com.bigid.web.commands;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.bigid.dao.entity.PostCommentVote;

public class CommentCommand implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7338189274475014269L;
	
	
	private Long commentId;
	private String commentPostedBy;
	private String commentContent;
	private Integer commentLikes;
	private Date commentPostedTime;	
	private String userId;
	private long postId;
	private List<ReplyCommand> replies;
	private PostCommentVote commentVoteDetails;
	private boolean commentNotification;


	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

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

	public Integer getCommentLikes() {
		return commentLikes;
	}

	public void setCommentLikes(Integer commentLikes) {
		this.commentLikes = commentLikes;
	}

	public Date getCommentPostedTime() {
		return commentPostedTime;
	}

	public void setCommentPostedTime(Date commentPostedTime) {
		this.commentPostedTime = commentPostedTime;
	}

	public List<ReplyCommand> getReplies() {
		return replies;
	}

	public void setReplies(List<ReplyCommand> replies) {
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

	public PostCommentVote getCommentVoteDetails() {
		return commentVoteDetails;
	}

	public void setCommentVoteDetails(PostCommentVote commentVoteDetails) {
		this.commentVoteDetails = commentVoteDetails;
	}

	public boolean isCommentNotification() {
		return commentNotification;
	}

	public void setCommentNotification(boolean commentNotification) {
		this.commentNotification = commentNotification;
	}
}
