package com.bigid.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bigid.common.enums.PostType;
import com.bigid.dao.entity.Comment;
import com.bigid.dao.entity.Post;

/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * PostRepository use to save all the post related data in database
 * @author satish
 *
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	/**This API is used to get the Post.
	 * @param pageRequest
	 * @return
	 */
	List<Post> findAllBy(Pageable  pageRequest);
	
	/**This API is used to get the Post by postId
	 * @param id
	 * @return
	 */
	Post findAllById(Long id);

	/**This API is used to find the all Post by parent postId
	 * @param postId
	 * @param comment
	 * @param pageRequest
	 * @return
	 */
	List<Comment> findAllPostByParentPostIdAndType(Long postId, PostType comment, Pageable pageRequest);

	/**This API is used to find the all comment by parent commentId
	 * @param comment
	 * @param pageRequest
	 * @return
	 */
	List<Post> findAllByTypeNot(PostType comment, Pageable pageRequest);
	
	/**This API is used to get the all saved post by end user.
	 * @param statusType
	 * @return
	 */
	@Query("SELECT p FROM Post p WHERE p.statusType = statusType")
    public List<Post> findAllSaved(@Param("statusType") String statusType);


}
