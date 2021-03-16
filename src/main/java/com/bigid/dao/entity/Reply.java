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
@Table(name = "USER_COMMENT_REPLY")
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private long replyId;
	@Column(name = "POST_ID")
	private long postId;
	@Column(name = "REPLIED_BY")
	private String replyCommentBy;
	@Column(name = "REPLY_CONTENT")
	private String replyCommentContent;
	@Column(name = "REPLY_COMMENT_LIKE")
	private int replyCommentLike;
	@Column(name = "REPLIED_TIME")
	private Date replyCommentTime;
	@Column(name = "IS_REPLIED_LIKE")
	private boolean repliedLike;
	@Column(name = "IS_REPLY_NOTIFICATION")
	private boolean replyNotification;
	@Column(name = "COMMENT_ID")
	private long commentId;
	@OneToMany(cascade = CascadeType.ALL)
	private List<PostReplyCommentVote> replyVotes;
	

	public List<PostReplyCommentVote> getReplyVotes() {
		return replyVotes;
	}
	public void setReplyVotes(List<PostReplyCommentVote> replyVotes) {
		this.replyVotes = replyVotes;
	}
	public long getReplyId() {
		return replyId;
	}	
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}
	public String getReplyCommentContent() {
		return replyCommentContent;
	}
	public String getReplyCommentBy() {
		return replyCommentBy;
	}
	public void setReplyCommentBy(String replyCommentBy) {
		this.replyCommentBy = replyCommentBy;
	}
	public void setReplyCommentContent(String replyCommentContent) {
		this.replyCommentContent = replyCommentContent;
	}
	public int getReplyCommentLike() {
		return replyCommentLike;
	}
	public void setReplyCommentLike(int replyCommentLike) {
		this.replyCommentLike = replyCommentLike;
	}
	public Date getReplyCommentTime() {
		return replyCommentTime;
	}
	public void setReplyCommentTime(Date replyCommentTime) {
		this.replyCommentTime = replyCommentTime;
	}
	public boolean isRepliedLike() {
		return repliedLike;
	}
	public void setRepliedLike(boolean repliedLike) {
		this.repliedLike = repliedLike;
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
