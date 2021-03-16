package com.bigid.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_POST_MISC")
public class PostMisc {
	@Id
    @Column(name = "POST_ID")
	private Long id;
	@Column(name = "IS_NSWF")
	private Boolean isNSWF;
	@Column(name="TO_FIELD")
	private String toField;
	@Column(name="FROM_FIELD")
	private String fromField;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean isNSWF() {
		return isNSWF == null ? false : isNSWF;
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
}
