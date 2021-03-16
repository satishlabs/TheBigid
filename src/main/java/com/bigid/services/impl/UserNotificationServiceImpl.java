package com.bigid.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigid.business.Notification;
import com.bigid.dao.entity.Post;
import com.bigid.dao.entity.User;
import com.bigid.dao.entity.UserFollowPost;
import com.bigid.repository.NotificationRepository;
import com.bigid.repository.PostRepository;
import com.bigid.repository.UserFollowRepository;
import com.bigid.repository.UserRepository;
import com.bigid.services.UserNotificationService;
import com.bigid.web.commands.notification.CommentNotification;
import com.bigid.web.commands.notification.DislikeNotification;
import com.bigid.web.commands.notification.FollowingNotification;
import com.bigid.web.commands.notification.LikeNotification;
import com.bigid.web.commands.notification.NSFWNotification;
import com.bigid.web.commands.notification.NotificationCommand;
import com.bigid.web.commands.notification.POSTNotification;
import com.bigid.web.commands.notification.PushNotification;
import com.bigid.web.commands.notification.ReplyNotification;
import com.bigid.web.commands.notification.SharedPostNotification;

/**
 * @author satish
 *
 */
@Service("UserNotificationServiceImpl")
@Transactional
public class UserNotificationServiceImpl implements UserNotificationService{

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private UserFollowRepository followRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;


	public void publish(Notification notification){
		com.bigid.dao.entity.Notification notificationEntity = new com.bigid.dao.entity.Notification();
		BeanUtils.copyProperties(notification, notificationEntity);
		notificationRepository.save(notificationEntity);
	}


	public NotificationCommand getNotification(Long userId){

		User notifiedUser = userRepository.findById(userId); 
		NotificationCommand notificationCommand = new  NotificationCommand();
		
		int notificationCount = 0;
		List<POSTNotification> postNotifications = new ArrayList<POSTNotification>();
		List<NSFWNotification> nsfwNotifications = new ArrayList<NSFWNotification>();
		List<PushNotification> pushNotifications = new ArrayList<PushNotification>();
		List<CommentNotification> commentNotifications = new ArrayList<CommentNotification>();
		List<ReplyNotification> replyNotifications = new ArrayList<ReplyNotification>();
		List<FollowingNotification> follwoingNotifications = new ArrayList<FollowingNotification>();
		List<SharedPostNotification> sharedPostNotifications = new ArrayList<SharedPostNotification>();
		List<LikeNotification> likeNotifications = new ArrayList<LikeNotification>();
		List<DislikeNotification> disLikeNotifications = new ArrayList<DislikeNotification>();
		List<com.bigid.dao.entity.Notification> notificationsOnMyIds = notificationRepository.findAll();

		//TODO need to refactored the code to a common builder class
		for(com.bigid.dao.entity.Notification notification : notificationsOnMyIds){

			Post post =postRepository.findAllById(notification.getPostId());
			User user = userRepository.findById(notification.getUserId()); 
			if(post.getCreatedBy() != user.getId()){
				if(notification.getActivity().equalsIgnoreCase("LIKED") && notification.getStatus().equalsIgnoreCase("NEW")){
					if(isMyPOST(post.getCreatedBy(), notifiedUser.getId())){
						//					if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
						notificationCount++;
						//	}
						LikeNotification likeNotification= new LikeNotification();
						likeNotification.setId(notification.getId());
						likeNotification.setPostId(post.getId());
						likeNotification.setPostName(post.getTitle());
						likeNotification.setUserId(notifiedUser.getId());
						likeNotification.setByUserId(user.getId());
						likeNotification.setByUserName(user.getUsername());
						likeNotification.setUserName(notifiedUser.getUsername());
						likeNotification.setNotifiedTime(notification.getActivityTime());
						likeNotification.setNotificationStatus(notification.getStatus());
						likeNotifications.add(likeNotification);
					}
				}

				else if(notification.getActivity().equalsIgnoreCase("DISLIKED") && notification.getStatus().equalsIgnoreCase("NEW")){
					if(isMyPOST(post.getCreatedBy(), notifiedUser.getId())){
						//	if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
						notificationCount++;
						//	}
						DislikeNotification dislikeNotification= new DislikeNotification();
						dislikeNotification.setId(notification.getId());
						dislikeNotification.setPostId(post.getId());
						dislikeNotification.setPostName(post.getTitle());
						dislikeNotification.setUserId(notifiedUser.getId());
						dislikeNotification.setUserName(notifiedUser.getUsername());
						dislikeNotification.setNotifiedTime(notification.getActivityTime());
						dislikeNotification.setByUserId(user.getId());
						dislikeNotification.setByUserName(user.getUsername());
						dislikeNotification.setNotificationStatus(notification.getStatus());
						disLikeNotifications.add(dislikeNotification);
					}
				}
				else if(notification.getActivity().equalsIgnoreCase("SHARED") && notification.getStatus().equalsIgnoreCase("NEW")){
					if(isMyPOST(post.getCreatedBy(), notifiedUser.getId())){
						//					if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
						notificationCount++;
						//	}
						SharedPostNotification sharedNotification= new SharedPostNotification();
						sharedNotification.setId(notification.getId());
						sharedNotification.setPostId(post.getId());
						sharedNotification.setPostName(post.getTitle());
						sharedNotification.setUserId(notifiedUser.getId());
						sharedNotification.setByUserId(user.getId());
						sharedNotification.setByUserName(user.getUsername());
						sharedNotification.setUserName(notifiedUser.getUsername());
						sharedNotification.setNotifiedTime(notification.getActivityTime());
						sharedNotification.setNotificationStatus(notification.getStatus());
						sharedPostNotifications.add(sharedNotification);
					}
				}
				else if(notification.getActivity().equalsIgnoreCase("COMMENT") && notification.getStatus().equalsIgnoreCase("NEW")){
					if(isMyPOST(post.getCreatedBy(), notifiedUser.getId())){
						//					if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
						notificationCount++;
						//	}
						CommentNotification commentNotification= new CommentNotification();
						commentNotification.setId(notification.getId());
						commentNotification.setPostId(post.getId());
						commentNotification.setPostName(post.getTitle());
						commentNotification.setUserId(notifiedUser.getId());
						commentNotification.setByUserId(user.getId());
						commentNotification.setByUserName(user.getUsername());
						commentNotification.setUserName(notifiedUser.getUsername());
						commentNotification.setNotifiedTime(notification.getActivityTime());
						commentNotification.setNotificationStatus(notification.getStatus());
						commentNotifications.add(commentNotification);
					}
				}

				else if(notification.getActivity().equalsIgnoreCase("REPLIED") && notification.getStatus().equalsIgnoreCase("NEW")){
					if(isMyPOST(post.getCreatedBy(), notifiedUser.getId())){
						//					if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
						notificationCount++;
						//	}
						ReplyNotification replyNotification = new ReplyNotification(); 
						replyNotification.setId(notification.getId());
						replyNotification.setPostId(post.getId());
						replyNotification.setPostName(post.getTitle());
						replyNotification.setUserId(notifiedUser.getId());
						replyNotification.setUserName(notifiedUser.getUsername());
						replyNotification.setNotifiedTime(notification.getActivityTime());
						replyNotification.setByUserId(user.getId());
						replyNotification.setByUserName(user.getUsername());
						replyNotification.setNotificationStatus(notification.getStatus());
						replyNotifications.add(replyNotification);
					}
				}
				else if(notification.getActivity().equalsIgnoreCase("PUSH") && notification.getStatus().equalsIgnoreCase("NEW")){
					if(isMyPOST(post.getCreatedBy(), notifiedUser.getId())){
						//					if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
						notificationCount++;
						//	}
						PushNotification pushNotification= new PushNotification();
						pushNotification.setId(notification.getId());
						pushNotification.setPostId(post.getId());
						pushNotification.setPostName(post.getTitle());
						pushNotification.setUserId(notifiedUser.getId());
						pushNotification.setByUserId(user.getId());
						pushNotification.setByUserName(user.getUsername());
						pushNotification.setUserName(notifiedUser.getUsername());
						pushNotification.setNotifiedTime(notification.getActivityTime());
						pushNotification.setNotificationStatus(notification.getStatus());
						pushNotifications.add(pushNotification);
					}
				}
				else if(notification.getActivity().equalsIgnoreCase("NSFW") && notification.getStatus().equalsIgnoreCase("NEW")){
					if(isMyPOST(post.getCreatedBy(), notifiedUser.getId())){
						//					if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
						notificationCount++;
						//	}
						NSFWNotification nsfwNotification= new NSFWNotification();
						nsfwNotification.setId(notification.getId());
						nsfwNotification.setPostId(post.getId());
						nsfwNotification.setPostName(post.getTitle());
						nsfwNotification.setUserId(notifiedUser.getId());
						nsfwNotification.setByUserId(user.getId());
						nsfwNotification.setByUserName(user.getUsername());
						nsfwNotification.setUserName(notifiedUser.getUsername());
						nsfwNotification.setNotifiedTime(notification.getActivityTime());
						nsfwNotification.setNotificationStatus(notification.getStatus());
						nsfwNotifications.add(nsfwNotification);
					}
				}

				else if(notification.getActivity().equalsIgnoreCase("FOLLOW") && notification.getStatus().equalsIgnoreCase("NEW")){
					if(isMyPOST(post.getCreatedBy(), notifiedUser.getId())){
						//					if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
						notificationCount++;
						//	}
						FollowingNotification followingNotification= new FollowingNotification();
						followingNotification.setId(notification.getId());
						followingNotification.setPostId(post.getId());
						followingNotification.setPostName(post.getTitle());
						followingNotification.setUserId(notifiedUser.getId());
						followingNotification.setByUserId(user.getId());
						followingNotification.setByUserName(user.getUsername());
						followingNotification.setUserName(notifiedUser.getUsername());
						followingNotification.setNotifiedTime(notification.getActivityTime());
						followingNotification.setNotificationStatus(notification.getStatus());
						follwoingNotifications.add(followingNotification);
					}
				}

			}

			//userRepository.save(user);
		}
		List<UserFollowPost> followers = followRepository.findPostfollowByFollower(notifiedUser.getId());
		for(UserFollowPost follow : followers){

			List<com.bigid.dao.entity.Notification> notifications = notificationRepository.findAllByUserId(follow.getFollowing());

			for(com.bigid.dao.entity.Notification notification : notifications){

				Post post =postRepository.findAllById(notification.getPostId());
				User user = userRepository.findById(post.getCreatedBy()); 

				if(notification.getActivity().equalsIgnoreCase("POST_CREATE") && notification.getStatus().equalsIgnoreCase("NEW")){
					//	if(notification.getActivityTime().after(user.getLastLoginTimestamp())){
					//if(notification.getActivity().equalsIgnoreCase("NEW")){
					notificationCount++;
					//}
					//}
					POSTNotification postNotification = new POSTNotification();
					postNotification.setId(notification.getId());
					postNotification.setPostId(post.getId());
					postNotification.setPostName(post.getTitle());
					postNotification.setUserId(notifiedUser.getId());
					postNotification.setByUserId(user.getId());
					postNotification.setByUserName(user.getUsername());
					postNotification.setUserName(notifiedUser.getUsername());
					postNotification.setNotifiedTime(notification.getActivityTime());
					postNotification.setNotificationStatus(notification.getStatus());
					postNotifications.add(postNotification);
				}
				user.setNotificationCount(notificationCount);

			}

		}
		notificationCommand.setPostNotifications(postNotifications);
		notificationCommand.setNsfwNotifications(nsfwNotifications);
		notificationCommand.setCommentNotifications(commentNotifications);
		notificationCommand.setPushNotification(pushNotifications);
		notificationCommand.setFollowingNotifications(follwoingNotifications);
		notificationCommand.setSharedPostNotifications(sharedPostNotifications);
		notificationCommand.setLikeNotification(likeNotifications);
		notificationCommand.setDislikeNotification(disLikeNotifications);
		notificationCommand.setReplyNotification(replyNotifications);
		//
		notifiedUser.setNotificationCount(notificationCount);
		notifiedUser.setRead(false);
		userRepository.save(notifiedUser);
		return notificationCommand;
	}

	private boolean isMyPOST(Long createdBy, Long id) {
		if(createdBy == id){
			return true;
		}
		return false;
	}


	private List<com.bigid.dao.entity.Notification> filterNewNotification(List<com.bigid.dao.entity.Notification> notifications, User notifiedUser){
		List<com.bigid.dao.entity.Notification> filteredNotifications = new ArrayList<com.bigid.dao.entity.Notification>();
		for(com.bigid.dao.entity.Notification notification : notifications){
			if(notification.getActivityTime().after(notifiedUser.getLastLoginTimestamp())){
				filteredNotifications.add(notification);
			}
		}
		return filteredNotifications;
	}

	public ArrayList<com.bigid.dao.entity.Notification> getAllNewNotification(long postId) {
		List<com.bigid.dao.entity.Notification> notifedPostList = notificationRepository.findAllNewPostByPostId(postId);
		ArrayList<com.bigid.dao.entity.Notification> postNotificationList = new ArrayList<com.bigid.dao.entity.Notification>();
		if(notifedPostList != null){
			for(com.bigid.dao.entity.Notification notification : notifedPostList){
				if(notification.getStatus().equalsIgnoreCase("NEW")){
					postNotificationList.add(notification);
				}
			}
		}
		return postNotificationList;
	}

	@Override
	public com.bigid.dao.entity.Notification readNotificationStatus(int notificationId) {
		com.bigid.dao.entity.Notification notification = notificationRepository.findById(notificationId);
		notification.setStatus("Read");
		return notification = notificationRepository.save(notification);
	}


	@Override
	public Object updateAllNewNotification(long userId) {

		List<com.bigid.dao.entity.Notification> notificationList = notificationRepository.findAllNewNotificationByUserId(userId);
		User user = userRepository.findById(userId);
		for(com.bigid.dao.entity.Notification notification : notificationList){
			
				if(user.getId() == userId){
					if(notification.getStatus().equalsIgnoreCase("NEW")){
					notification.setStatus("Read");
					notificationRepository.save(notification);

				}
			}
		}
		return notificationList;
	}

}
