package com.bigid.services;

import com.bigid.dao.entity.PostVote;
import com.bigid.web.commands.VoteDetail;
/**
 * This is the PostVoteService interface The service layer is there to provide logic to operate on 
 * the data sent to and from the Repository and the client. 
 * @author satish
 *
 */
public interface PostVoteService {


	/**This API is used to get the Vote based pn userId and postId
	 * @param userId
	 * @param postId
	 * @return
	 */
	public PostVote getVote(long userId, long postId);
	
	/** This API is used to save the UP(Like) Vote on post based on that postId
	 * @param postId
	 * @param voteDetail
	 * @return
	 */
	PostVote saveUpVote(long postId, VoteDetail voteDetail);
	
	/**This API is used to save the DOWN(Dislike) Vote on post based on that postId
	 * @param postId
	 * @param voteDetail
	 * @return
	 */
	PostVote saveDownVote(long postId, VoteDetail voteDetail);
	
	/**This API is used to save the PUSH Vote Vote on post based on that postId
	 * @param postId
	 * @param voteDetail
	 * @return
	 */
	PostVote updatePushAvaility(long postId, VoteDetail voteDetail);

	/**This API is used to save the NSFW Vote on post based on that postId
	 * @param postId
	 * @param voteDetail
	 * @return
	 */
	PostVote updateIsNsfw(long postId, VoteDetail voteDetail);
	
	/**This API is used to get the Vote based on postId
	 * @param id
	 * @return
	 */
	PostVote getVote(Long id);

	
}
