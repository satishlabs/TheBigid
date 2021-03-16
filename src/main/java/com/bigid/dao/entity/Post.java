package com.bigid.dao.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bigid.common.enums.PostCategory;
import com.bigid.common.enums.PostType;

@Entity
@Table(name = "USER_POST")
public class Post extends BaseEntity{

	private static final long serialVersionUID = -2491447398473268341L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false, unique = true)
	private Long id;
	@Column(name="POST_TITLE")
	private String title;
	@Column(name="POST_BODY", length=3000)
	private String body;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name = "TAGS")
	@Column(name="TAGS")
	private Set<String> tags = new HashSet<String>();
	
	@Column(name="TYPE")
	@Enumerated(EnumType.STRING)
	private PostType type;
	@Column(name="CATEGORY")
	@Enumerated(EnumType.STRING)
	private PostCategory category;
	@Column(name = "STATUS_TYPE")
	private String statusType;
	@Column(name="LONGITUDE")
	private Double longitude;
	@Column(name="LATITUDE")
	private Double latitude; 
	@Column(name="IMG_PATH")
	private String imgPath;
	@Column(name="CREATED_BY")
	private Long createdBy;
	@OneToOne
	@JoinColumn(name="CREATED_BY", insertable=false, updatable=false)
	private User creator;
	@Column(name="TOTAL_VOTES_CNT")
	private int voteCount;
	@Column(name="TOTAL_COMMENTS_CNT")
	private int commentsCount;
	@Column(name="IS_ANONYMOUS")
	private Boolean isAnonymous = false;
	
	@ManyToOne(targetEntity = Post.class, cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	@JoinColumn(name = "PARENT_POST_ID", nullable = true)
	private Post parentPost;
	@OneToMany(mappedBy="parentPost")
	private List<Post> comments; 
	
	@OneToOne(cascade =CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private PostMisc postMisc;
	@OneToMany(cascade = CascadeType.ALL)
	private List<PostVote> votes; 
	@Column(name="POST_IMG")
	private String postImg;
	
	//@ManyToOne(cascade= CascadeType.ALL)
	
	@ManyToOne(fetch = FetchType.LAZY,cascade =CascadeType.ALL)
	@Access(AccessType.PROPERTY)
	@PrimaryKeyJoinColumn
	private PostRequest postRequest;
	@Column(name="POST_CREATED_TIME")
	private Date postCreatedTime;
	@Column(name="USERNAME")
	private String username;
	@Column(name="USER_ID")
	private String userId;
	@Column(name = "IS_FOLLOWER")
	private boolean followUser;
	@Column(name="LOCATION")
	private String location;
	@Column(name="COUNTRY")
	private String country;
	@Column(name = "IS_NOTIFICATION")
	private boolean postNotification;
	@Column(name="POPULARITY")
	private int popularity;
	@Column(name="EVENT_DATE")
	private Date eventDate;
	@Column(name="USER_INPUT_CATEGORY")
	private String postUserInputCategory;
	@Column(name = "SAVED_POST")
	private int savedPostCount;
	@Column(name = "PUSH_COUNT")
	private int pushCount;
	@Column(name = "EXPAND")
	private boolean expand;
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy="postId")
	private List<PostShared> savedPostByUser;
	
	
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, mappedBy="postId")
	private List<Comment> comment;

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
	public Set<String> getTags() {
		return tags;
	}
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	public PostType getType() {
		return type;
	}
	public void setType(PostType type) {
		this.type = type;
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
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public int getCommentsCount() {
		return commentsCount;
	}
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}
	public Boolean isAnonymous() {
		return isAnonymous;
	}
	public void setAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public PostMisc getPostMisc() {
		return postMisc;
	}
	public void setPostMisc(PostMisc postMisc) {
		this.postMisc = postMisc;
	}
	public List<PostVote> getVotes() {
		return votes;
	}
	public void setVotes(List<PostVote> votes) {
		this.votes = votes;
	}
	public User getCreator() {
		return creator;
	}
	private void setCreator(User creator) {
		this.creator = creator;
	}
	@Transient
	public Boolean getAnonymous() {
		return isAnonymous();
	}
	public Boolean getIsAnonymous() {
		return isAnonymous;
	}
	public void setIsAnonymous(Boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
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
	public boolean isPostNotification() {
		return postNotification;
	}
	public void setPostNotification(boolean postNotification) {
		this.postNotification = postNotification;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getPostCreatedTime() {
		return postCreatedTime;
	}
	public void setPostCreatedTime(Date postCreatedTime) {
		this.postCreatedTime = postCreatedTime;
	}
	public PostRequest getPostRequest() {
		return postRequest;
	}
	public void setPostRequest(PostRequest postRequest) {
		this.postRequest = postRequest;
	}
	public String getPostImg() {
		return postImg;
	}
	public void setPostImg(String postImg) {
		this.postImg = postImg;
	}
	public List<Comment> getComment() {
		return comment;
	}
	public void setComment(List<Comment> comment) {
		this.comment = comment;
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
	public int getSavedPostCount() {
		return savedPostCount;
	}
	public void setSavedPostCount(int savedPostCount) {
		this.savedPostCount = savedPostCount;
	}
	public List<PostShared> getSavedPostByUser() {
		return savedPostByUser;
	}
	public void setSavedPostByUser(List<PostShared> savedPostByUser) {
		this.savedPostByUser = savedPostByUser;
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
	public Post getParentPost() {
		return parentPost;
	}
	public void setParentPost(Post parentPost) {
		this.parentPost = parentPost;
	}
	public List<Post> getComments() {
		return comments;
	}
	public void setComments(List<Post> comments) {
		this.comments = comments;
	}
	public int getPushCount() {
		return pushCount;
	}
	public void setPushCount(int pushCount) {
		this.pushCount = pushCount;
	}
	
}
