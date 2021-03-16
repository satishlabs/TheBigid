package com.bigid.web.commands;

import com.bigid.dao.entity.BaseEntity;
import com.bigid.dao.entity.Post;

/**
 * @author satish
 *
 */
public class RequestCommand extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1289170773990753681L;
	
	private Long id;
	private Post post;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	@Override
	public String toString() {
		return "RequestCommand [id=" + id + ", post=" + post + "]";
	}
	
	
	

}
