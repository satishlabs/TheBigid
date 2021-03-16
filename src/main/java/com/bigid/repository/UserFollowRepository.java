package com.bigid.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.UserFollowPost;


/**
/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * UserFollowRepository use to save all one user follow the other user related data in database
 * @author satish
 *
 */
@Repository
public interface UserFollowRepository extends JpaRepository<UserFollowPost, Long> {


	/**This API is used to get the list follow user, who is following you.
	 * @param userId
	 * @return
	 */
	public List<UserFollowPost> findPostfollowByFollower(Long userId);

	/**This API is used to get the list follow user, who is following you.
	 * @param userId
	 * @return
	 */
	public List<UserFollowPost> findPostfollowById(Long userId);

	/**
	/**This API is used to get the list follow user, who is following you.
	 * @param username
	 * @return
	 */
	public static List<UserFollowPost> findByFollowingUsername(String username) {
		return null;
	}

}
