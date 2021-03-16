package com.bigid.dao.entity;

import javax.persistence.Column;

public class MessageResponse {
	@Column(name = "CODE")
	private String code;
	@Column(name = "MESSAGE")
	private String message;
	
	public MessageResponse() {
		super();
	}
	public MessageResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
