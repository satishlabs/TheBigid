package com.bigid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigid.dao.entity.Notification;


/**
 * Spring Data JPA focuses on using JPA to store data in a relational database	
 * NotificationRepository use to notification all the user related data in database
 * @author satish
 *
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{
	
	/**This API is used to find the all Notification By userId
	 * @param userId
	 * @return
	 */
	List<Notification>   findAllByUserId(Long userId);

	/**This API is used to get the all New Notification by PostId
	 * @param postId
	 * @return
	 */
	List<Notification> findAllNewPostByPostId(long postId);

	/**This API is used to get the Notification by notificationId
	 * @param notificationId
	 * @return
	 */
	com.bigid.dao.entity.Notification findById(int notificationId);

	/**This API is used to get the All new Notification ny userId
	 * @param userId
	 * @return
	 */
	public List<com.bigid.dao.entity.Notification> findAllNewNotificationByUserId(long userId);
}
