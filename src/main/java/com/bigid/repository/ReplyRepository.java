package com.bigid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.Reply;
/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * ReplyRepository use to save all reply of comments of all the user related data in database
 * @author satish
 *
 */
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
	
	/**This API is used to get the user , who has replayed on particular comment based on commentId
	 * @param commentId
	 * @return
	 */
	List<Reply> getRepliesByCommentId(Long commentId);
	
}
