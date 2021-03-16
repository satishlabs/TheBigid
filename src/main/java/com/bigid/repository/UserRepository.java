package com.bigid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.User;

/**
 * Spring Data JPA focuses on using JPA to store data in a relational database
 * UserRepository use to save all the user related data in database
 * @author satish
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**This API is used to find the Username
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

	/**This API is used to find the loggedInUserId
	 * @param loggedInUserId
	 * @return
	 */
	User findById(long loggedInUserId);

	/**This API is used to find the loggedInUser
	 * @param loggedInUser
	 * @return
	 */
	User findByUsername(User loggedInUser);

}
