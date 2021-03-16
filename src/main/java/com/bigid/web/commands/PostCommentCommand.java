package com.bigid.web.commands;

import com.bigid.common.enums.PostCategory;

public class PostCommentCommand {

	private Long id;
	private String body;
	private String tags;
	private PostCategory category;
	private Double longitude;
	private Double latitude; 
	private String imgPath;
	private Long createdBy;
	private Integer voteCount;
	private Integer commentsCount;
	private Boolean isAnonymous;
	private boolean commentNotification;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public PostCategory getCategory() {
		return category;
	}
	public void setCategory(PostCategory category) {
		this.category = category;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}
	public Integer getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(Integer commentsCount) {
		this.commentsCount = commentsCount;
	}
	public Boolean getIsAnonymous() {
		return isAnonymous;
	}
	public void setIsAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public boolean isCommentNotification() {
		return commentNotification;
	}
	public void setCommentNotification(boolean commentNotification) {
		this.commentNotification = commentNotification;
	}
}
