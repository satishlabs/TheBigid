package com.bigid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.PostShared;
/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * PostSharedRepository use to share all the of post all the user related data in database
 * @author satish
 *
 */
@Repository
public interface PostSharedRepository extends JpaRepository<PostShared, Long> {
	
	/**This API is used to find the post shared by end user
	 * @param userId
	 * @return
	 */
	public List<PostShared> findPostSharedByUserId(Long userId);

	/**This API is used to find the post saved by end user
	 * @param postId
	 * @return
	 */
	public List<PostShared> findPostSavedByPostId(Long postId);


}
