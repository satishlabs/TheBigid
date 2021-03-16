package com.bigid.web.commands;
/**
 * Generic rest response which is shared with client. This common response will ensure client always gets response in standard format.
 * @author Dhirendra Singh
 *
 */
public class CustomHttpResponse {

	private Object data;
	private Object error;
	
	public void setData(Object data) {
		this.data = data;
	}
	public void setError(Object error) {
		this.error = error;
	}
	public Object getData() {
		return data;
	}
	public Object getError() {
		return error;
	}
	public CustomHttpResponse() {
	}
	
	public CustomHttpResponse(Object data, Object error) {
		this.data = data;
		this.error = error;
	}
	
	
}
