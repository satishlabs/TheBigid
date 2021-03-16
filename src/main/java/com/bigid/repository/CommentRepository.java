package com.bigid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.PostCommentVote;

/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * CommentRepository use to save all the post comment related data in database
 * @author satish
 *
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	/**This API is used to get the post on which user has commented.
	 * @param postId
	 * @return
	 */
	List<Comment> getCommentsByPostId(Long postId);

	/**This API is used to save the Comment Vote.
	 * @param dbVote
	 */
	public void save(PostCommentVote dbVote);	
}
