package com.bigid.web.commands;

import java.io.Serializable;
import java.util.Date;

import com.bigid.common.enums.VoteType;

public class PostReplyCommandVoteDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5444416334763070887L;
	private String id;
	private long postId;
	private VoteType voteType;
	private long userId;
	private long commentId;
	private long replyId;
	private Date upVotedTimeByMe;
	private Date downVotedTimeByMe;
	private Integer voteCount;
	private boolean upVote;
	private boolean downVote;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isUpVote() {
		return upVote;
	}
	public void setUpVote(boolean upVote) {
		this.upVote = upVote;
	}
	public boolean isDownVote() {
		return downVote;
	}
	public void setDownVote(boolean downVote) {
		this.downVote = downVote;
	}
	public Date getUpVotedTimeByMe() {
		return upVotedTimeByMe;
	}
	public void setUpVotedTimeByMe(Date upVotedTimeByMe) {
		this.upVotedTimeByMe = upVotedTimeByMe;
	}
	public Date getDownVotedTimeByMe() {
		return downVotedTimeByMe;
	}

	public void setDownVotedTimeByMe(Date downVotedTimeByMe) {
		this.downVotedTimeByMe = downVotedTimeByMe;
	}
	public VoteType getVoteType() {
		return voteType;
	}
	public void setVoteType(VoteType voteType) {
		this.voteType = voteType;
	}
	public Integer getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	public long getReplyId() {
		return replyId;
	}
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}


}
