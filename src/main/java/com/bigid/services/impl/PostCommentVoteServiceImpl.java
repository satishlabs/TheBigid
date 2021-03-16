package com.bigid.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigid.common.enums.VoteType;
import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.PostCommentVote;
import com.bigid.dao.entity.PostVote;
import com.bigid.repository.CommentRepository;
import com.bigid.repository.PostCommentVoteRepository;
import com.bigid.repository.PostVoteRepository;
import com.bigid.services.PostCommentVoteService;
import com.bigid.web.commands.CommentVoteDetail;

@Service
@Transactional
public class PostCommentVoteServiceImpl implements PostCommentVoteService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostVoteRepository postVoteRepository;

	@Autowired
	private PostCommentVoteRepository postCommentVoteRepository;

	@Override
	public void addVote(Long postId, VoteType voteType) {
	}

	public void saveCommentVote(PostVote postVote) {

		List<PostVote> postVList = new ArrayList<PostVote>();
		postVList.add(postVote);

		postVoteRepository.save(postVote);
	}

	@Override
	public CommentVoteDetail getPostVoteByPostId(Long postId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PostCommentVote getCommentVote(long userId, long commentId) {
		Comment comment = commentRepository.findOne(commentId);
		List<PostCommentVote> postCommentVotesList = comment.getCommentVotes();
		
		PostCommentVote postCommentDBVotes = null;
		for(PostCommentVote postCommentVote : postCommentVotesList){
				postCommentDBVotes = postCommentVote;
				break;
		}
		return postCommentDBVotes;
	}


	
	@Override
	public PostCommentVote saveCommentUpVote(long commentId, CommentVoteDetail commentVoteDetail) {
		PostCommentVote userCommentDBVote = null;
		boolean flagForSameVote = true;
		if (commentVoteDetail != null) {
			Comment postCommentVote = commentRepository.findOne(commentId);
			List<PostCommentVote> commentVotes = postCommentVote.getCommentVotes();

			for (PostCommentVote commentDBVote : commentVotes) {
				if (commentDBVote.getUserId() == commentVoteDetail.getUserId()) {
					userCommentDBVote = commentDBVote;
					break;
				}
			}
			if (commentVoteDetail.isUpVote()) {
				if (null == userCommentDBVote) {
					userCommentDBVote = new PostCommentVote();
					userCommentDBVote.setPostId(postCommentVote.getPostId());
					userCommentDBVote.setCommentId(commentId);
					userCommentDBVote.setUserId(commentVoteDetail.getUserId());
					userCommentDBVote.setUpVotedTimeByMe(new Date(System
							.currentTimeMillis()));
					userCommentDBVote.setVoteType(VoteType.UP);
					userCommentDBVote.setUpVote(true);

					commentVotes.add(userCommentDBVote);
				} else if (userCommentDBVote.getVoteType().equals(VoteType.DOWN)) {
					userCommentDBVote.setUpVotedTimeByMe(new Date(System
							.currentTimeMillis()));
					userCommentDBVote.setVoteType(VoteType.UP);
					if (userCommentDBVote.isUpVote() == true) {
						flagForSameVote = false;
					}
					userCommentDBVote.setDownVote(false);
					userCommentDBVote.setUpVote(true);
				} else if (userCommentDBVote.getVoteType().equals(VoteType.UP)) {
					flagForSameVote = false;
				}
				if (flagForSameVote) {
					int vc = getVoteCounted(userCommentDBVote, commentId);
					userCommentDBVote.setVoteCount(vc);
					for (PostCommentVote dbVote : commentVotes) {
						dbVote.setVoteCount(vc);
						postCommentVoteRepository.save(dbVote);
					}
				}

				if (null != userCommentDBVote) {

					postCommentVoteRepository.save(userCommentDBVote);
					postCommentVote.setCommentVotes(commentVotes);
					commentRepository.save(postCommentVote);
				}
			}
		}
		return userCommentDBVote;
	}

	@Override
	public PostCommentVote saveCommentDownVote(long commentId,
			CommentVoteDetail commentVoteDetail) {
		PostCommentVote userCommentDBVote = null;
		boolean flagForSameVote = true;
		if (commentVoteDetail != null) {
			Comment postCommentVote = commentRepository.findOne(commentId);
			List<PostCommentVote> commentVotes = postCommentVote.getCommentVotes();
			for (PostCommentVote commentDBVote : commentVotes) {
				if (commentDBVote.getUserId() == commentVoteDetail.getUserId()) {
					userCommentDBVote = commentDBVote;
					break;
				}
			}
			if (commentVoteDetail.isDownVote()) {
				if (null == userCommentDBVote) {
					userCommentDBVote = new PostCommentVote();
					userCommentDBVote.setCommentId(commentId);
					userCommentDBVote.setDownVote(true);
					userCommentDBVote.setUpVote(false);
					userCommentDBVote.setPostId(postCommentVote.getPostId());
					userCommentDBVote.setUserId(commentVoteDetail.getUserId());
					userCommentDBVote.setDownVotedTimeByMe(new Date(System
							.currentTimeMillis()));
					userCommentDBVote.setVoteType(VoteType.DOWN);
					commentVotes.add(userCommentDBVote);

				} else if (userCommentDBVote.getVoteType().equals(VoteType.UP)) {
					userCommentDBVote.setDownVotedTimeByMe(new Date(System
							.currentTimeMillis()));
					if (userCommentDBVote.isDownVote() == true) {
						flagForSameVote = false;
					}
					userCommentDBVote.setDownVote(true);
					userCommentDBVote.setUpVote(false);
					userCommentDBVote.setVoteType(VoteType.DOWN);
				} else if (userCommentDBVote.getVoteType().equals(VoteType.DOWN)) {
					flagForSameVote = false;
				}
			}
			if (flagForSameVote) {
				int vc = getVoteCounted(userCommentDBVote, commentId);
				userCommentDBVote.setVoteCount(vc);
				for (PostCommentVote dbVote : commentVotes) {
					dbVote.setVoteCount(vc);
					postCommentVoteRepository.save(dbVote);
				}
			}
			if (null != userCommentDBVote) {
				postCommentVoteRepository.save(userCommentDBVote);
				postCommentVote.setCommentVotes(commentVotes);
				commentRepository.save(postCommentVote);
			}

		}
		return userCommentDBVote;
	}
	
	private int getVoteCounted(PostCommentVote userCommentDBVote, long commentId) {
		int vc = 0;
		Comment comment = commentRepository.getOne(commentId);
		List<PostCommentVote> votes = comment.getCommentVotes();
		for (PostCommentVote vote : votes) {
			if (null != vote.getVoteCount() && vc < vote.getVoteCount()) {
				vc = vote.getVoteCount();
			}
		}
		if (userCommentDBVote.getVoteType().equals(VoteType.UP)) {
			vc++;
		} else if (userCommentDBVote.getVoteType().equals(VoteType.DOWN)) {
			vc--;
		}
		return vc;
	}

	@Override
	public PostCommentVote getCommentVote(long commentId) {
		int voteCount = 0;
		Comment comment = commentRepository.findOne(commentId);
		List<PostCommentVote> postCommentVotesList = comment.getCommentVotes();
		PostCommentVote postCommentDBVotes = new PostCommentVote();
		for(PostCommentVote  postCommentDBVote : postCommentVotesList){
			voteCount = postCommentDBVote.getVoteCount();
			break;
		}
		
		return postCommentDBVotes;
	}
	
	@Override
	public PostCommentVote getCommentVote(Long postId) {
		int voteCount = 0;
		
		Comment comment = commentRepository.findOne(postId);
		List<PostCommentVote> votes = comment.getCommentVotes();
		PostCommentVote userDBVote = new PostCommentVote();
		for (PostCommentVote dbVote : votes) {
			voteCount = dbVote.getVoteCount();
			
			break;
		}
		userDBVote.setVoteCount(voteCount);
		
		return userDBVote;
	}






}
