package com.bigid.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.Reply;
import com.bigid.repository.CommentRepository;
import com.bigid.repository.ReplyRepository;
import com.bigid.services.CommentService;



/**
 * @author satish
 * This is the CommentServiceImpl to write the all business logic of comment's realated API.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService{
	private final Logger logger = Logger.getLogger(CommentServiceImpl.class);
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired 
	ReplyRepository repliesRepository;
	
	
	
	@Override
	public List<Comment> getCommentedByPostId() {
		List<Comment> commentList = commentRepository.findAll();
		if(commentList != null && commentList.size()>0){
			commentList.get(0).getCommentId();
		}
		return commentList;
	}
	
	
	@Override
	public List<Reply> getRepliesCommentByPostId() {
		List<Reply> replyList = repliesRepository.findAll();
		if(replyList != null && replyList.size()>0){
			replyList.get(0).getReplyId();
		}
		return replyList;
	}
	


}
