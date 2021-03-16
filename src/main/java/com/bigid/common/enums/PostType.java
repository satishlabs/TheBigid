package com.bigid.common.enums;
/**
 * Enumeration which defines the various types of post user can create on bigid.
 * @author Dhirendra Singh
 *
 */
public enum PostType {
	
	GENERAL("GENERAL"),QUESTION("QUESTION"),VISUAL("VISUAL"),LINK("LINK"),COMMENT("COMMENT"), EVENT("EVENT");
	
	private String postType;

	private PostType(String templatingType) {
		this.postType = templatingType;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String templatingType) {
		this.postType = templatingType;
	}

}
