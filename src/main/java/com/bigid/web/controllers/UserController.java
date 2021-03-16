package com.bigid.web.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bigid.dao.entity.MessageResponse;
import com.bigid.dao.entity.User;
import com.bigid.exceptions.ServerValidationException;
import com.bigid.repository.UserRepository;
import com.bigid.services.UserService;
import com.bigid.services.impl.UserServiceImpl;
import com.bigid.web.commands.UserCommand;
import com.bigid.web.common.Constants;
import com.bigid.web.controllers.validator.UserRegistrationValidator;

//@SecureRestController
@RestController
@RequestMapping("/user/")
public class UserController {
	private final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UploadController imageUploadController;

	@Autowired
	private UserRegistrationValidator userValidator;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	Environment environment;

	@Autowired
	private MessageSource messageSource;
	

	/**
	 * User can update his basic details + avatar image using /user/profile url.
	 * 
	 * @param userCommand
	 * @param uploadAvtarImg
	 * @throws IOException
	 */
	@RequestMapping(value = "/profile", method = { RequestMethod.POST, RequestMethod.PUT })
	public void updateProfile(@ModelAttribute("userForm") UserCommand userCommand,
			@RequestParam(name = "uploadAvtarImg", required = false) MultipartFile uploadAvtarImg) throws IOException {

		if (uploadAvtarImg != null) {
			Map<String, String> uploadImageMap = imageUploadController.uploadImage(uploadAvtarImg,
					Constants.USER_PROFILE);
			// TODO: Need to save images in hierarchical format, so that images
			// operation can be focused rather scattered all over.
			if (uploadImageMap.get("status").equals("success")) {
				userCommand.setAvatarImgPath(uploadImageMap.get("imgURL"));
			}
		}
		userService.saveOrUpdate(userCommand);
	}

	/**
	 * This API is used to new user can register their details with his profile
	 * pic;
	 * 
	 * @author satish
	 * @param newUser
	 * @param bindingResult
	 * @param uploadAvtarImg
	 * @throws IOException
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void userSignup(@RequestBody UserCommand newUser, BindingResult bindingResult) throws IOException {
		userValidator.validate(newUser, bindingResult);
		logger.info("User newUser details :" + newUser);
		if (bindingResult.hasErrors()) {
			throw new ServerValidationException("Invalid request", bindingResult);
		}


		userService.saveOrUpdate(newUser);
		logger.info("user saved successfully!!");

	}

	/**
	 * This API is used to get the user image based on url
	 * 
	 * @author satish
	 * @param name
	 * @param request
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/image/{name:.+}", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif", method = RequestMethod.GET)

	public MessageResponse getUserImagePath(@PathVariable("name") String name, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String imagePath = "/resources/images/" + name;
		String contextPath = request.getContextPath();
		String port = environment.getProperty("local.server.port");
		String url = "http://localhost" + ":" + port + contextPath + "/" + imagePath;

		logger.info("imagePath @:" + imagePath + "contextPath @ :" + contextPath + "port #:" + port + "url #:" + url);

		response.sendRedirect(url);
		return new MessageResponse(messageSource.getMessage("success_message", null, null),
				messageSource.getMessage("update_message", null, null));
		// return new MessageResponse(messageSource.getMessage(success_message,
		// null));

	}

	/**
	 * This API is used to get the compelete users details based on username
	 * 
	 * @author satish
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserDetail(@PathVariable("username") String username) {

		User user = userServiceImpl.loadUserDetailByUsername(username);
		if (user != null) {
			if (user.getLastLoginTimestamp() != null) {
				Date lastLogin = user.getLastLoginTimestamp();
				Date current = new Date(System.currentTimeMillis());
				lastLogin = changeDate(lastLogin);
				current = changeDate(current);
				if (lastLogin.compareTo(current) < 0) {
					user.setPushAvailabilityNo(Constants.ByDefaultPushAvail);
				}

			}
			user.setLastLoginTimestamp(new Date(System.currentTimeMillis()));
			userRepository.save(user);
		}
		logger.info("User :" + user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	public Date changeDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * To update the Userdetails with this API
	 * 
	 * @author satish
	 * @param id
	 * @param usr
	 * @return
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public String updateUser(@RequestParam Long id, @RequestBody User usr) {
		try {
			User user = userRepository.findOne(id);
			user.setUserId(usr.getUserId());
			user.setLastModifiedTimestamp(new Date(System.currentTimeMillis()));
			user.setUsername(usr.getUsername());
			user = userRepository.save(user);
		} catch (Exception ex) {
			return "Error updating the user: " + ex.toString();
		}
		return "User succesfully updated!";

	}

	/**
	 * This API is used to change the Password
	 * 
	 * @author satish
	 * @param id
	 * @param currentPassword
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "/changepassword/{id}/{currentPassword}/{newPassword}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<User> changePassword(@PathVariable("id") String id,
			@PathVariable("currentPassword") String currentPassword, @PathVariable("newPassword") String newPassword) {

		User user = userRepository.findOne(Long.valueOf(id));
		CharSequence rawPassword = currentPassword;
		String encodedPassword = user.getPassword();
		if (bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
			changePwdUtil(user, newPassword);
		} else {
			return new ResponseEntity<User>(HttpStatus.FAILED_DEPENDENCY);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/**
	 * Encode the password
	 * 
	 * @author satish
	 * @param user
	 * @param newPassword
	 */
	private void changePwdUtil(User user, String newPassword) {
		
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		userRepository.save(user);
	}
	
	@RequestMapping(value = "/changeprofileimage/{username}", method = RequestMethod.PUT)
	public ResponseEntity<Object> changeProfilePic(@RequestBody UserCommand newUser, @PathVariable("username") String username)
			throws IOException {
		User user = userService.findByUsername(username);
		if(user == null){
			return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("error_code", null, null),
					messageSource.getMessage("notification_error", null, null)), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(userService.updateAvtar(newUser,username),HttpStatus.OK);
		
	}

	/**
	 * This API is used to get the post follower based on userId
	 * 
	 * @author satish
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/logout/{userId}", method = { RequestMethod.GET })
	public Date lagoutTime(@PathVariable Long userId) {
		User user = userRepository.getOne(userId);
		user.setLogoutTimestamp(new Date(System.currentTimeMillis()));
		Date logoutTime = user.getLogoutTimestamp();
		userRepository.save(user);
		return logoutTime;
	}
	
	/**This API is used to reset the Notification count 0, when user will click on Notification Bell.
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/viewNotification/{userId}", method = { RequestMethod.PUT })
	public User resetNotification(@PathVariable Long userId) {
		User user = userRepository.getOne(userId);
		user.setNotificationCount(0);
		user.setRead(true);
		return userRepository.save(user);

	}
	
	}
