package com.bigid.exceptions;
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	Object[] messageParams = null;
	Throwable rootCause = null;
	
	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String message, Throwable rootCause) {
		super(message,rootCause);
	}

	public BusinessException(Throwable rootCause) {
		super(rootCause);
	}
	
	public Object[] getMessageParams() {
		return messageParams;
	}
	
	public Throwable getRootCause() {
		return rootCause;
	}
	
	public void setRootCause(Throwable rootCause) {
		this.rootCause = rootCause;
	}
}
	