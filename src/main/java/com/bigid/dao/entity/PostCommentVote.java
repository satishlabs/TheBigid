package com.bigid.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bigid.common.enums.VoteType;

@Entity
@Table(name = "USER_POST_COMMENT_VOTE_DETAIL")
public class PostCommentVote {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	@Column(name = "POST_ID")
	private long postId;
	@Column(name = "COMMENT_ID")
	private long commentId;
	@Column(name = "USER_ID")
	private long userId;
	@Column(name = "VOTE_TYPE", columnDefinition = "varchar(32) default 'NOT_VOTED'")
	@Enumerated(EnumType.STRING)
	private VoteType voteType;
	@Column(name = "UP_VOTE_TIME")
	private Date upVotedTimeByMe;
	@Column(name = "DOWN_VOTE_TIME")
	private Date downVotedTimeByMe;
	@Column(name = "VOTE_COUNT")
	private Integer voteCount;
	@Column(name = "IS_UP_VOTE")
	private boolean upVote;
	@Column(name = "IS_DOWN_VOTE")
	private boolean downVote;
	


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public VoteType getVoteType() {

		return voteType;
	}
	public void setVoteType(VoteType voteType) {
		this.voteType = voteType;
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
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Integer getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
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
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}

}
