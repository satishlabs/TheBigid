package com.bigid.services;

import com.bigid.common.enums.VoteType;
import com.bigid.dao.entity.PostCommentVote;
import com.bigid.web.commands.CommentVoteDetail;

/**
 * *This is the UserService interface The service layer is there to provide
 * logic to operate on the data sent to and from the Repository and the client.
 * 
 * @author satish
 *
 */
public interface PostCommentVoteService {

	/**
	 * This API is used by add the Vote based on postId and Vote Type
	 * 
	 * @param postId
	 * @param voteType
	 */
	void addVote(Long postId, VoteType voteType);

	/**
	 * This API to used to get the User who liked the comment based on postId
	 * 
	 * @param postId
	 * @return
	 */
	CommentVoteDetail getPostVoteByPostId(Long postId);

	/**
	 * This API is used to save the comments likes based on commentId
	 * 
	 * @param commentId
	 * @param commentVoteDetail
	 * @return
	 */
	PostCommentVote saveCommentUpVote(long commentId, CommentVoteDetail commentVoteDetail);

	/**
	 * This API is used to save the comments dislikes based on commentId
	 * 
	 * @param commentId
	 * @param commentVoteDetail
	 * @return
	 */
	PostCommentVote saveCommentDownVote(long commentId, CommentVoteDetail commentVoteDetail);

	/**
	 * This API is used to get the Comment vote based on userId and commentId
	 * 
	 * @param userId
	 * @param commentId
	 * @return
	 */
	PostCommentVote getCommentVote(long userId, long commentId);

	/**
	 * This API is used to get the Comment vote based on commentId
	 * 
	 * @param commentId
	 * @return
	 */
	PostCommentVote getCommentVote(long commentId);

	/**This API is used to count the commentVoteDetails on Post.
	 * @param postId
	 * @return
	 */

	PostCommentVote getCommentVote(Long postId);

}