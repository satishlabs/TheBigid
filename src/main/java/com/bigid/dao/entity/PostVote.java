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
@Table(name = "USER_POST_VOTE_DETAIL")
public class PostVote {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	@Column(name = "POST_ID")
	private long postId;
	@Column(name = "USER_ID")
	private long userId;

	@Column(name = "VOTE_TYPE", columnDefinition = "varchar(32) default 'NOT_VOTED'")
	@Enumerated(EnumType.STRING)
	private VoteType voteType;
	@Column(name = "UP_VOTE_TIME")
	private Date upVotedTimeByMe;
	@Column(name = "DOWN_VOTE_TIME")
	private Date downVotedTimeByMe;
	@Column(name = "PUSH_ACTIVATED_TIME")
	private Date pushActivatedTimeByMe;
	@Column(name = "REPORTED_NSFW_TIME")
	private Date reportedIsNSFWTimeByMe;
	@Column(name = "IS_PUSH_ACTIVATE")
	private boolean pushActivate;
	@Column(name = "IS_PUSH_ACTIVATED")
	private boolean pushActivated;
	@Column(name = "REPORTED_NSFW")
	private boolean reportedNSFW;
	@Column(name = "REASON_FOR_NSFW")
	private String reasonForNsfw;
	@Column(name = "IS_REPORTED_NSFW")
	private boolean reportedIsNSFW;
	@Column(name = "VOTE_COUNT")
	private int voteCount;
	@Column(name = "IS_UP_VOTE")
	private boolean upVote;
	@Column(name = "IS_DOWN_VOTE")
	private boolean downVote;
	@Column(name = "PUSH_COUNT")
	private int pushCount;
	
	
	
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

	public boolean isPushActivate() {
		return pushActivate;
	}

	public void setPushActivate(boolean pushActivate) {
		this.pushActivate = pushActivate;
	}

	public boolean isReportedNSFW() {
		return reportedNSFW;
	}

	public void setReportedNSFW(boolean reportedNSFW) {
		this.reportedNSFW = reportedNSFW;
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

	public Date getPushActivatedTimeByMe() {
		return pushActivatedTimeByMe;
	}

	public void setPushActivatedTimeByMe(Date pushActivatedTimeByMe) {
		this.pushActivatedTimeByMe = pushActivatedTimeByMe;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
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
	public int getPushCount() {
		return pushCount;
	}
	public void setPushCount(int pushCount) {
		this.pushCount = pushCount;
	}
	public boolean isPushActivated() {
		return pushActivated;
	}
	public void setPushActivated(boolean pushActivated) {
		this.pushActivated = pushActivated;
	}
	public boolean isReportedIsNSFW() {
		return reportedIsNSFW;
	}
	public void setReportedIsNSFW(boolean reportedIsNSFW) {
		this.reportedIsNSFW = reportedIsNSFW;
	}
	public String getReasonForNsfw() {
		return reasonForNsfw;
	}
	public void setReasonForNsfw(String reasonForNsfw) {
		this.reasonForNsfw = reasonForNsfw;
	}
	public Date getReportedIsNSFWTimeByMe() {
		return reportedIsNSFWTimeByMe;
	}
	public void setReportedIsNSFWTimeByMe(Date reportedIsNSFWTimeByMe) {
		this.reportedIsNSFWTimeByMe = reportedIsNSFWTimeByMe;
	}
}
