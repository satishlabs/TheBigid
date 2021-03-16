package com.bigid.services.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigid.common.enums.VoteType;
import com.bigid.dao.entity.PostReplyCommentVote;
import com.bigid.dao.entity.Reply;
import com.bigid.repository.ReplyRepository;
import com.bigid.repository.ReplyVoteRepository;
import com.bigid.services.ReplyService;
import com.bigid.web.commands.PostReplyCommandVoteDetail;



@Service
@Transactional
public class ReplyServiceImpl implements ReplyService{
	private final Logger logger = Logger.getLogger(ReplyServiceImpl.class);

	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private ReplyVoteRepository replyVoteRepository;
	

	@Override
	public PostReplyCommentVote saveReplyUpVote(long replyId, PostReplyCommandVoteDetail postReplyCommandVoteDetail) {
		PostReplyCommentVote commentReplyDBVote = null;
		boolean flagForSameVote = true;
		if (postReplyCommandVoteDetail != null) {
			Reply commentReplyVote = replyRepository.findOne(replyId);
			List<PostReplyCommentVote> commentReplyVotes = commentReplyVote.getReplyVotes();

			for (PostReplyCommentVote replyCommentDBVote : commentReplyVotes) {
				if (replyCommentDBVote.getUserId() == postReplyCommandVoteDetail.getUserId()) {
					commentReplyDBVote = replyCommentDBVote;
					break;
				}
			}
			if (postReplyCommandVoteDetail.isUpVote()) {
				if (null == commentReplyDBVote) {
					commentReplyDBVote = new PostReplyCommentVote();
					commentReplyDBVote.setPostId(commentReplyDBVote.getPostId());
					commentReplyDBVote.setCommentId(postReplyCommandVoteDetail.getCommentId());
					commentReplyDBVote.setPostId(postReplyCommandVoteDetail.getPostId());
					commentReplyDBVote.setReplyId(replyId);
					commentReplyDBVote.setUserId(postReplyCommandVoteDetail.getUserId());
					commentReplyDBVote.setUpVotedTimeByMe(new Date(System
							.currentTimeMillis()));
					commentReplyDBVote.setVoteType(VoteType.UP);
					commentReplyDBVote.setUpVote(true);

					commentReplyVotes.add(commentReplyDBVote);
				} else if (commentReplyDBVote.getVoteType().equals(VoteType.DOWN)) {
					commentReplyDBVote.setUpVotedTimeByMe(new Date(System
							.currentTimeMillis()));
					commentReplyDBVote.setVoteType(VoteType.UP);
					if (commentReplyDBVote.isUpVote() == true) {
						flagForSameVote = false;
					}
					commentReplyDBVote.setDownVote(false);
					commentReplyDBVote.setUpVote(true);
				} else if (commentReplyDBVote.getVoteType().equals(VoteType.UP)) {
					flagForSameVote = false;
				}
				if (flagForSameVote) {
					int vc = getVoteCounted(commentReplyDBVote, replyId);
					commentReplyDBVote.setVoteCount(vc);
					for (PostReplyCommentVote dbVote : commentReplyVotes) {
						dbVote.setVoteCount(vc);
						replyVoteRepository.save(dbVote);
					}
				}

				if (null != commentReplyDBVote) {

					replyVoteRepository.save(commentReplyDBVote);
					commentReplyVote.setReplyVotes(commentReplyVotes);
					replyRepository.save(commentReplyVote);
				}
			}
		}
		return commentReplyDBVote;
	}
	
	private int getVoteCounted(PostReplyCommentVote commentReplyDBVote, long replyId) {
		int vc = 0;
		Reply reply = replyRepository.getOne(replyId);
		List<PostReplyCommentVote> votes = reply.getReplyVotes();
		for (PostReplyCommentVote vote : votes) {
			if (null != vote.getVoteCount() && vc < vote.getVoteCount()) {
				vc = vote.getVoteCount();
			}
		}
		if (commentReplyDBVote.getVoteType().equals(VoteType.UP)) {
			vc++;
		} else if (commentReplyDBVote.getVoteType().equals(VoteType.DOWN)) {
			vc--;
		}
		return vc;
	}
	
}
