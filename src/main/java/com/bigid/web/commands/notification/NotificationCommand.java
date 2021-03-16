package com.bigid.web.commands.notification;

import java.util.List;

public class NotificationCommand {
	
	
	private List<VoteNotification> voteNotifications;
	private List<POSTNotification> postNotifications;
	private List<NSFWNotification> nsfwNotifications;
	private List<CommentNotification> commentNotifications;
	private List<ReplyNotification> replyNotification;
	private List<FollowingNotification> followingNotifications;
	private List<SharedPostNotification> sharedPostNotifications;
	private List<LikeNotification> likeNotification;
	private List<DislikeNotification> dislikeNotification;
	private List<PushNotification> pushNotification;
	
	
	public List<VoteNotification> getVoteNotifications() {
		return voteNotifications;
	}
	public void setVoteNotifications(List<VoteNotification> voteNotifications) {
		this.voteNotifications = voteNotifications;
	}
	public List<POSTNotification> getPostNotifications() {
		return postNotifications;
	}
	public void setPostNotifications(List<POSTNotification> postNotifications) {
		this.postNotifications = postNotifications;
	}
	public List<NSFWNotification> getNsfwNotifications() {
		return nsfwNotifications;
	}
	public void setNsfwNotifications(List<NSFWNotification> nsfwNotifications) {
		this.nsfwNotifications = nsfwNotifications;
	}
	public List<CommentNotification> getCommentNotifications() {
		return commentNotifications;
	}
	public void setCommentNotifications(List<CommentNotification> commentNotifications) {
		this.commentNotifications = commentNotifications;
	}
	public List<FollowingNotification> getFollowingNotifications() {
		return followingNotifications;
	}
	public void setFollowingNotifications(List<FollowingNotification> followingNotifications) {
		this.followingNotifications = followingNotifications;
	}
	public List<SharedPostNotification> getSharedPostNotifications() {
		return sharedPostNotifications;
	}
	public void setSharedPostNotifications(List<SharedPostNotification> sharedPostNotifications) {
		this.sharedPostNotifications = sharedPostNotifications;
	}
	public List<LikeNotification> getLikeNotification() {
		return likeNotification;
	}
	public void setLikeNotification(List<LikeNotification> likeNotification) {
		this.likeNotification = likeNotification;
	}
	public List<DislikeNotification> getDislikeNotification() {
		return dislikeNotification;
	}
	public void setDislikeNotification(List<DislikeNotification> dislikeNotification) {
		this.dislikeNotification = dislikeNotification;
	}
	public List<PushNotification> getPushNotification() {
		return pushNotification;
	}
	public void setPushNotification(List<PushNotification> pushNotification) {
		this.pushNotification = pushNotification;
	}
	public List<ReplyNotification> getReplyNotification() {
		return replyNotification;
	}
	public void setReplyNotification(List<ReplyNotification> replyNotification) {
		this.replyNotification = replyNotification;
	}

}
