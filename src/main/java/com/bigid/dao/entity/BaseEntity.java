package com.bigid.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity implements Serializable{


	private static final long serialVersionUID = 1L;
	@Column(name="CREATION_TIMESTAMP")
	private Date creationTimestamp;
	@Column(name="LAST_MODIFIED_TIMESTAMP")
	private Date lastModifiedTimestamp;
	

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}
	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	public Date getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}
	public void setLastModifiedTimestamp(Date lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}
	
}
