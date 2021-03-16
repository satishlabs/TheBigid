package com.bigid.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigid.dao.entity.PostReplyCommentVote;
import com.bigid.dao.entity.Reply;
import com.bigid.repository.ReplyRepository;
import com.bigid.services.ReplyVoteService;



@Service
@Transactional
public class ReplyVoteServiceImpl implements ReplyVoteService{
	private final Logger logger = Logger.getLogger(ReplyVoteServiceImpl.class);
	
	@Autowired
	private ReplyRepository replyRepository;
		
	@Override
	public PostReplyCommentVote replyVote(long userId, long replyId) {
		Reply reply = replyRepository.findOne(replyId);
		List<PostReplyCommentVote> postReplyCommentVoteList = reply.getReplyVotes();
		PostReplyCommentVote PostReplyCommentDBVotes = null;
		for(PostReplyCommentVote prcVotes : postReplyCommentVoteList){
			PostReplyCommentDBVotes = prcVotes;
			break;
		}
		return PostReplyCommentDBVotes;
	}

	@Override
	public PostReplyCommentVote replyVote(long replyId) {
		
		Integer voteCount = 0;
		Reply reply = replyRepository.findOne(replyId);
		List<PostReplyCommentVote> postReplyCommentVoteList = reply.getReplyVotes();
		PostReplyCommentVote postReplyCommentVoteDBVotes = new PostReplyCommentVote();
		for(PostReplyCommentVote  postReplyDBVote : postReplyCommentVoteList){
			voteCount = postReplyDBVote.getVoteCount();
			break;
		}	
		return postReplyCommentVoteDBVotes;
	}
	
	
}
