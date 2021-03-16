package com.bigid.web.commands;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.minidev.json.annotate.JsonIgnore;

@JsonIgnoreProperties({"longitude,latitude"})
public class CurrentFeedCommand {
	

	private long id;
	private String username;
	private String location;
	private String title;
	private Double longitude;
	private Double latitude;
	
	@JsonIgnore
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@JsonIgnore
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	private Set<Double> coordinates = new LinkedHashSet<Double>();
	
	@JsonProperty("postId")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@JsonProperty("postedBy")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<Double> getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Set<Double> coordinates) {
		this.coordinates = coordinates;
	}	

}
