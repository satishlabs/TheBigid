package com.bigid.services;

import com.bigid.dao.entity.PostReplyCommentVote;
import com.bigid.web.commands.PostReplyCommandVoteDetail;

/**
 * *This is the ReplyService interface The service layer is there to provide
 * logic to operate on the data sent to and from the Repository and the client.
 * 
 * @author satish
 */
public interface ReplyService {
	
	/** This API is used to save the UP(Like) Vote on Reply based on replyId
	 * @param replyId
	 * @param postReplyCommandVoteDetail
	 * @return
	 */
	PostReplyCommentVote saveReplyUpVote(long replyId, PostReplyCommandVoteDetail postReplyCommandVoteDetail);
	
}
