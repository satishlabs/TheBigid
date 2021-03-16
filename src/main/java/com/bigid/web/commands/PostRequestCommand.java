package com.bigid.web.commands;

import java.util.Date;

import com.bigid.dao.entity.BaseEntity;

public class PostRequestCommand  extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2571606696878353092L;
	private Long id;
	private String title;
	private String body;
	private String tags;
	private String postType;
	private String postCategory;
	private Double longitude;
	private Double latitude; 
	private String imgPath;
	private Boolean isAnonymous;
	private Boolean isNSWF;
	private String toField;
	private String fromField;
	private Date postCreatedTime;
	private String username;
	private String userId;
	private boolean followUser;
	private String location;
	private String country;
	private String postUserInputCategory;
	private boolean expand;
	private boolean enableNotificationFromUser;
	private String postImg;
	private int popularity;
	private Date eventDate;
	private String statusType;
	private boolean postNotification;
	private int savedPostCount;
	private Long createdBy;
	private int commentsCount;
	private int voteCount;
	private int pushCount;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
	public String getPostCategory() {
		return postCategory;
	}
	public void setPostCategory(String postCategory) {
		this.postCategory = postCategory;
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
	public Boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public Boolean isNSWF() {
		return isNSWF;
	}
	public void setNSWF(Boolean isNSWF) {
		this.isNSWF = isNSWF;
	}
	public String getToField() {
		return toField;
	}
	public void setToField(String toField) {
		this.toField = toField;
	}
	public String getFromField() {
		return fromField;
	}
	public void setFromField(String fromField) {
		this.fromField = fromField;
	}
	public Boolean getIsAnonymous() {
		return isAnonymous;
	}
	public void setIsAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public Boolean getIsNSWF() {
		return isNSWF;
	}
	public void setIsNSWF(Boolean isNSWF) {
		this.isNSWF = isNSWF;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isFollowUser() {
		return followUser;
	}
	public void setFollowUser(boolean followUser) {
		this.followUser = followUser;
	}
	public String getPostUserInputCategory() {
		return postUserInputCategory;
	}
	public void setPostUserInputCategory(String postUserInputCategory) {
		this.postUserInputCategory = postUserInputCategory;
	}
	public boolean isExpand() {
		return expand;
	}
	public void setExpand(boolean expand) {
		this.expand = expand;
	}
	public boolean isEnableNotificationFromUser() {
		return enableNotificationFromUser;
	}
	public void setEnableNotificationFromUser(boolean enableNotificationFromUser) {
		this.enableNotificationFromUser = enableNotificationFromUser;
	}
	public Date getPostCreatedTime() {
		return postCreatedTime;
	}
	public void setPostCreatedTime(Date postCreatedTime) {
		this.postCreatedTime = postCreatedTime;
	}
	public String getPostImg() {
		return postImg;
	}
	public void setPostImg(String postImg) {
		this.postImg = postImg;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getPopularity() {
		return popularity;
	}
	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public boolean isPostNotification() {
		return postNotification;
	}
	public void setPostNotification(boolean postNotification) {
		this.postNotification = postNotification;
	}
	public int getSavedPostCount() {
		return savedPostCount;
	}
	public void setSavedPostCount(int savedPostCount) {
		this.savedPostCount = savedPostCount;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public int getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
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
		
}
