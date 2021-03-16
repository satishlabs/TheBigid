package com.bigid.web.controllers;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bigid.dao.entity.MessageResponse;
import com.bigid.dao.entity.Notification;
import com.bigid.dao.entity.User;
import com.bigid.repository.NotificationRepository;
import com.bigid.services.UserNotificationService;
import com.bigid.services.UserService;

@RestController
@RequestMapping("/notification/")
public class UserNotificationController {
	@SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(UserNotificationController.class);
	
	@Autowired
	private UserNotificationService notificationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	
	/**
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/subscribe/{userId}")
	public ResponseEntity<Object> subscribe(@PathVariable long userId){
		User user = userService.findByUserId(userId);
		if(user == null){
			return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
					messageSource.getMessage("notification_error", null, null)), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(notificationService.getNotification(userId),HttpStatus.OK);
	}
	
	/**
	 * @param postId
	 * @return
	 */
	@GetMapping(value = "/allnewnotification/{postId}")
	public ArrayList<Notification> getAllNewNotification(@PathVariable long postId){
		 
		return notificationService.getAllNewNotification(postId);
	}
	
	/**This API  is used to read change unread notification to read
	 * @param notificationId
	 * @return
	 */
	@PutMapping(value = "/readnotification/{notificationId}")
	public ResponseEntity<Object> readNotificationStatus(@PathVariable int notificationId){
		
		Notification notification = notificationRepository.findById(notificationId);
		 if(notification == null){
			 return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
						messageSource.getMessage("notificationId_notfound", null, null)), HttpStatus.BAD_REQUEST);
		 }
		 return new ResponseEntity<>(notificationService.readNotificationStatus(notificationId),HttpStatus.OK);
	}
	
	@PutMapping(value = "/updateAllNewNotification/{userId}")
	public ResponseEntity<Object> updateAllNewNotification(@PathVariable long userId){
		User user = userService.findByUserId(userId);
		 if(user == null){
			 return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
						messageSource.getMessage("notificationId_notfound", null, null)), HttpStatus.BAD_REQUEST);
		 }
		 return new ResponseEntity<>(notificationService.updateAllNewNotification(userId),HttpStatus.OK);
	}

}
