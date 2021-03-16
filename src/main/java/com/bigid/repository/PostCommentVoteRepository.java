package com.bigid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.PostCommentVote;


/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * PostCommentVoteRepository use to save all the post comment related data in database
 * @author satish
 *
 */
@Repository
public interface PostCommentVoteRepository extends JpaRepository<PostCommentVote, Long> {

	/**This API is used to get the comment , which is liked by end user.
	 * @param commentId
	 * @return
	 */
	List<PostCommentVote> getPostCommentVoteByCommentId(Long commentId);

}
