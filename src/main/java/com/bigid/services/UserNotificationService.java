package com.bigid.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.bigid.business.Notification;
import com.bigid.web.commands.notification.NotificationCommand;

/**
 * *This is the UserNotificationService interface The service layer is there to
 * provide logic to operate on the data sent to and from the Repository and the
 * client.
 * 
 * @author satish
 *
 */
@Service
public interface UserNotificationService {
	
	/**This API is used to save the all Types of Notication 
	 * @param notification
	 */
	public void publish(Notification notification);
	/**This API is used to get the notification based on userId
	 * @param userId
	 * @return
	 */
	public NotificationCommand getNotification(Long userId);

	/**This API is used to get the  NEW notification based on postId
	 * @param postId
	 * @return
	 */
	public ArrayList<com.bigid.dao.entity.Notification> getAllNewNotification(long postId);
	
	/**This API is used to read the notification based on UserId
	 * @param notificationId
	 * @return
	 */
	public com.bigid.dao.entity.Notification readNotificationStatus(int notificationId);
	/**This API is Used to update all NEW notification based on userId
	 * @param notificationId
	 * @return
	 */
	public Object updateAllNewNotification(long userId);
	


}
