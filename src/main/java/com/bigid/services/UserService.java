package com.bigid.services;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bigid.dao.entity.User;
import com.bigid.dao.entity.UserFollowPost;
import com.bigid.web.commands.UserCommand;

/**
 * *This is the UserService interface The service layer is there to provide
 * logic to operate on the data sent to and from the Repository and the client.
 * 
 * @author satish
 *
 */
public interface UserService {

	/**
	 * This API is used to find the user by username
	 * 
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

	/**
	 * This function is used to set user password,user roles, and usertype
	 * 
	 * @param user
	 * @throws IOException 
	 */
	void saveOrUpdate(UserCommand user) throws IOException;



	/**To get the user details based on username
	 * @param userData
	 * @return
	 * @throws UsernameNotFoundException
	 */
	User loadUserDetailByUsername(String userData) throws UsernameNotFoundException;

	/**To get the followers of post 
	 * @param postId
	 * @return
	 */
	List<UserFollowPost> getFollowersFor(Long postId);

	/**To get the UserId
	 * @param userId
	 * @return
	 */
	User findByUserId(long userId);
	
	/**
	 * @param newUser
	 * @param username
	 * @return
	 * @throws IOException
	 */
	User updateAvtar(UserCommand newUser, String username) throws IOException;

	/**
	 * @param userId
	 * @return
	 */
	Object getFollowersByPostId(Long userId);

}