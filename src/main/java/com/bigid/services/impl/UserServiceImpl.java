package com.bigid.services.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bigid.dao.entity.User;
import com.bigid.dao.entity.User.UserType;
import com.bigid.dao.entity.UserFollowPost;
import com.bigid.repository.RoleRepository;
import com.bigid.repository.UserFollowRepository;
import com.bigid.repository.UserRepository;
import com.bigid.services.SecurityService;
import com.bigid.services.UserService;
import com.bigid.web.commands.UserCommand;
import com.bigid.web.common.Constants;



@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	SecurityService securityManager;

	@Autowired
	private UserService userService;


	@Autowired
	private UserFollowRepository userFollowRepository;

	@Autowired
	Environment environment;

	@Override
	public void saveOrUpdate(UserCommand user) throws IOException {

		User userEntity = new User();
		BeanUtils.copyProperties(user, userEntity);

		userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setRoles(new ArrayList<>(roleRepository.findAll()));
		userEntity.setUserType(UserType.USER);
		userEntity.setLocation(user.getLocation());
		userEntity.setLatitude(user.getLatitude());
		userEntity.setCountry(user.getCountry());
		userEntity.setLongitude(user.getLongitude());
		userEntity.setCreationTimestamp(new Date(System.currentTimeMillis()));
		userEntity.setLastModifiedTimestamp(new Date(System.currentTimeMillis()));
		userEntity.setLastLoginTimestamp(new Date(System.currentTimeMillis()));
		userEntity.setLogoutTimestamp(new Date(System.currentTimeMillis()));
		userEntity.setPushAvailabilityNo(Constants.ByDefaultPushAvail);
		String sourceData = user.getAvatarImgPath();
		if (sourceData != null && sourceData.length() > 0) {
			String[] parts = sourceData.split(",");
			String imageString = parts[1];
			BufferedImage image = null;
			byte[] imageByte;

			//BASE64Decoder decoder = new BASE64Decoder();
			 Base64.Decoder dec = Base64.getDecoder();
			 
			imageByte = dec.decode(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();
			// write the image to a file
			File outputfile = new File(
					environment.getProperty("image.path.local.diskProfile") + user.getUsername() + ".png");
			String imagePathView = environment.getProperty("image.path.viewProfile") + outputfile.getName();

			ImageIO.write(image, "png", outputfile);

			userEntity.setAvatarImgPath(imagePathView);
		} else {
			File outputfile = new File(environment.getProperty("image.path.local.diskDefault") + "default.png");
			String imagePathView = environment.getProperty("image.path.viewDefault") + outputfile.getName();
			userEntity.setAvatarImgPath(imagePathView);
		}

		userRepository.save(userEntity);

		userEntity.setUserId(userEntity.getUsername()+userEntity.getId());
		userRepository.save(userEntity);
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

	@Override
	public User updateAvtar(UserCommand newUser,String username) throws IOException {
		User user = userService.findByUsername(username);
		String userImgs = newUser.getAvatarImgPath();
		System.out.println("userImgs  @@" + userImgs);
		if (userImgs != null && userImgs.length() > 0) {

			String[] parts = userImgs.split(",");
			String imageString = parts[1];

			BufferedImage image = null;
			byte[] imageByte;

			System.out.println("imageString:" + imageString);

			//BASE64Decoder decoder = new BASE64Decoder();
			 Base64.Decoder dec = Base64.getDecoder();
			imageByte = dec.decode(imageString);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();


			File outputfile = new File(
					environment.getProperty("image.path.local.diskProfile") + username+ ".png");
			String imagePathView = environment.getProperty("image.path.viewProfile") + outputfile.getName();

			ImageIO.write(image, "png", outputfile);
			System.out.println("outputfile:@@@" + outputfile + " ###imagePathView %" + imagePathView);

			user.setAvatarImgPath(imagePathView);
		}
		return user;

	}

	@Override
	public Object getFollowersByPostId(Long userId) {
		List<UserFollowPost> postList = userFollowRepository.findPostfollowByFollower(userId);
		ArrayList<UserFollowPost> userFollowPostList = new ArrayList<UserFollowPost>();
		if (postList != null && postList.size()>0) {
			for (UserFollowPost postfollow : postList) {
				postfollow.getFollowing();
				userFollowPostList.add(postfollow);
			}
		}
		return postList;
	}
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}


	@Override
	public User loadUserDetailByUsername(String userData)
			throws UsernameNotFoundException {

		com.bigid.dao.entity.User user = userRepository.findByUsername(userData);
		return user;
	}

	@Override
	public List<UserFollowPost> getFollowersFor(Long postId) {

		return null;
	}

	@Override
	public User findByUserId(long userId) {
		return userRepository.findById(userId);
	}

}