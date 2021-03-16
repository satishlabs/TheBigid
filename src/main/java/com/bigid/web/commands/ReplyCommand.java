package com.bigid.web.commands;

import java.util.Date;

import com.bigid.dao.entity.PostReplyCommentVote;

public class ReplyCommand {
	
	private long replyId;
	private String replyCommentBy;
	private String replyCommentContent;
	private Integer replyCommentLike;
	private Date replyCommentTime;
	private long commentId;
	private long postId;
	private boolean replyNotification;
	private PostReplyCommentVote postReplyCommentVote;
	
	
	public PostReplyCommentVote getPostReplyCommentVote() {
		return postReplyCommentVote;
	}
	public void setPostReplyCommentVote(PostReplyCommentVote postReplyCommentVote) {
		this.postReplyCommentVote = postReplyCommentVote;
	}
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}
	public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
	public String getReplyCommentBy() {
		return replyCommentBy;
	}
	public void setReplyCommentBy(String replyCommentBy) {
		this.replyCommentBy = replyCommentBy;
	}
	public String getReplyCommentContent() {
		return replyCommentContent;
	}
	public void setReplyCommentContent(String replyCommentContent) {
		this.replyCommentContent = replyCommentContent;
	}
	public Integer getReplyCommentLike() {
		return replyCommentLike;
	}
	public void setReplyCommentLike(Integer replyCommentLike) {
		this.replyCommentLike = replyCommentLike;
	}
	public Date getReplyCommentTime() {
		return replyCommentTime;
	}
	public void setReplyCommentTime(Date replyCommentTime) {
		this.replyCommentTime = replyCommentTime;
	}
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public boolean isReplyNotification() {
		return replyNotification;
	}
	public void setReplyNotification(boolean replyNotification) {
		this.replyNotification = replyNotification;
	}
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
}
