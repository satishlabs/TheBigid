package com.bigid.web.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.bigid.dao.entity.BaseEntity;
import com.bigid.dao.entity.UserFollowPost;

/**This is the USER VO Class
 * @author satish
 *
 */
public class UserCommand extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3836388339568614010L;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private String username;
	private String password;
	private String passwordConfirm;
	private String email;
	private double longitude;
	private double latitude;
	private String location;
	private String country;
	private String avatarImgPath;
	private Date dateOfBirth;
	private String city;
	private String followingTo;
	private Date logoutTimestamp;
	private List<UserFollowPost> followers; 
	private String notificationCount;
	private boolean isRead;

	
	private String userId;
	private Integer pushAvailabilityNo;

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
	public String getAvatarImgPath() {
		return avatarImgPath;
	}
	public void setAvatarImgPath(String avatarImgPath) {
		this.avatarImgPath = avatarImgPath;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		if(StringUtils.isEmpty(dateOfBirth)){
			this.dateOfBirth = null;
		}else{
			try {
				this.dateOfBirth = sdf.parse(dateOfBirth);
			} catch (ParseException e) {
				this.dateOfBirth=null;
			}
		}
	}
	
	public void setDOB(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getFollowingTo() {
		return followingTo;
	}
	public void setFollowingTo(String followingTo) {
		this.followingTo = followingTo;
	}
	public Integer getPushAvailabilityNo() {
		return pushAvailabilityNo;
	}
	public void setPushAvailabilityNo(Integer pushAvailabilityNo) {
		this.pushAvailabilityNo = pushAvailabilityNo;
	}
	public List<UserFollowPost> getFollowers() {
		return followers;
	}
	public void setFollowers(List<UserFollowPost> followers) {
		this.followers = followers;
	}
	public Date getLogoutTimestamp() {
		return logoutTimestamp;
	}
	public void setLogoutTimestamp(Date logoutTimestamp) {
		this.logoutTimestamp = logoutTimestamp;
	}
	public String getNotificationCount() {
		return notificationCount;
	}
	public void setNotificationCount(String notificationCount) {
		this.notificationCount = notificationCount;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}	
	
}
