package com.bigid.services;

import java.util.List;

import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.Reply;

/**
 * This is the CommentService interface The service layer is there to provide logic to operate on 
 * the data sent to and from the Repository and the client. 
 * @author satish
 *
 */
public interface CommentService {
	

	/**This API is used to get the Commented user on Post by postId
	 * @return
	 */
	List<Comment> getCommentedByPostId();
	
	/**This API is used to get the ReplyBy user on who replied on comment.
	 * @return
	 */
	List<Reply> getRepliesCommentByPostId();
	
}
