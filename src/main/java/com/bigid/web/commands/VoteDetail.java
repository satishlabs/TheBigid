package com.bigid.web.commands;

import java.io.Serializable;
import java.util.Date;

import com.bigid.common.enums.VoteType;

public class VoteDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5444416334763070887L;
	private String id;
	private VoteType voteType;
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	private int voteCount;
	private int pushCount;
	private boolean upVote;
	private boolean downVote;
	private Date upVotedTimeByMe;
	private Date downVotedTimeByMe;
	private Date pushActivatedTimeByMe;
	private Date reportedIsNSFWTimeByMe;
	private boolean pushActivate;
	private boolean pushActivated;
	private boolean reportedNSFW;
	private boolean reportedIsNSFW;
	private String reasonForNsfw;

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

	public Date getPushActivatedTimeByMe() {
		return pushActivatedTimeByMe;
	}

	public void setPushActivatedTimeByMe(Date pushActivatedTimeByMe) {
		this.pushActivatedTimeByMe = pushActivatedTimeByMe;
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
	public VoteType getVoteType() {
		return voteType;
	}
	public void setVoteType(VoteType voteType) {
		this.voteType = voteType;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
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
