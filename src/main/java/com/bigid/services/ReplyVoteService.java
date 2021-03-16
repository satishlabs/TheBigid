package com.bigid.services;

import com.bigid.dao.entity.PostReplyCommentVote;

/**
 * This is the ReplyVoteService interface The service layer is there to provide logic to operate on 
 * the data sent to and from the Repository and the client. 
 * @author satish
 *
 */
public interface ReplyVoteService {

	/**This API is used to save the comment UP(LIKE) on post comment.
	 * @param userId
	 * @param replyId
	 * @return
	 */
	PostReplyCommentVote replyVote(long userId, long replyId);

	/**This API is used to save the comment UP(LIKE) on post comment.
	 * @param replyId
	 * @return
	 */
	PostReplyCommentVote replyVote(long replyId);	
	
}
