package com.bigid.dao.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class User extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum UserType { USER, ADMIN};
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable = false, unique = true)
	private Long id;
	
	@Column(name="USERNAME", length=15, nullable=false)
	private String username;
	@Column(name="PASSWORD", length=100, nullable = false)
	private String password;
	@Column(name="EMAIL")
	private String email;
	@Column(name = "LOCATION")
	private String location;
	@Column(name = "COUNTRY")
	private String country;
	@Column(name="LONGITUDE")
	private Double longitude;
	@Column(name="LATITUDE")
	private Double latitude;
	@Column(name="USER_TYPE")
	@Enumerated(EnumType.STRING)
	private UserType userType;
	@Column(name="AVATAR_IMG_PATH")
	private String avatarImgPath;
	@Column(name="DOB")
	private Date dateOfBirth;
	@Column(name="LAST_LOGIN_TSTMP")
	private Date lastLoginTimestamp; 
	@Column(name = "LOGOUT_TSTMP")
	private Date logoutTimestamp;
	@Column(name = "FOLLOWING_TO")
	private String followingTo;
	@Column(name = "NOTIFICATION_COUNT")
	private Integer notificationCount;
	@Column(name = "IS_READ")
	private boolean isRead;
	@ManyToMany
    @JoinTable(name = "USER_ROLE_REL", 
    			joinColumns = @JoinColumn(name = "USER_ID"), 
    			inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	private List<Role> roles;
    

	@OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
	private List<UserFollowPost> following; //gives following list
	
	@OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
	private List<UserFollowPost> followers; //gives followers list
	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "PUSH_AVAILABILITY_NO")
	private Integer pushAvailabilityNo;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	public String getAvatarImgPath() {
		return avatarImgPath;
	}
	public void setAvatarImgPath(String avatarImgPath) {
		this.avatarImgPath = avatarImgPath;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Date getLastLoginTimestamp() {
		return lastLoginTimestamp;
	}
	public void setLastLoginTimestamp(Date lastLoginTimestamp) {
		this.lastLoginTimestamp = lastLoginTimestamp;
	}
	public Date getLogoutTimestamp() {
		return logoutTimestamp;
	}
	public void setLogoutTimestamp(Date logoutTimestamp) {
		this.logoutTimestamp = logoutTimestamp;
	}
	public String getFollowingTo() {
		return followingTo;
	}
	public void setFollowingTo(String followingTo) {
		this.followingTo = followingTo;
	}
	public Integer getNotificationCount() {
		return notificationCount;
	}
	public void setNotificationCount(Integer notificationCount) {
		this.notificationCount = notificationCount;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public List<UserFollowPost> getFollowing() {
		return following;
	}
	public void setFollowing(List<UserFollowPost> following) {
		this.following = following;
	}
	public List<UserFollowPost> getFollowers() {
		return followers;
	}
	public void setFollowers(List<UserFollowPost> followers) {
		this.followers = followers;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getPushAvailabilityNo() {
		return pushAvailabilityNo;
	}
	public void setPushAvailabilityNo(Integer pushAvailabilityNo) {
		this.pushAvailabilityNo = pushAvailabilityNo;
	}
	
	
}
