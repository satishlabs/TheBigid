package com.bigid.web.commands;

import java.io.Serializable;
import java.util.List;

public class PostListCommand  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4945656238410235292L;
	
	List<PostResponseCommand> posts;
	
	
	public void setPosts(List<PostResponseCommand> postDetailCmdLst) {
		this.posts = postDetailCmdLst;
	}
	
	public List<PostResponseCommand> getPosts() {
		return posts;
	}

}
