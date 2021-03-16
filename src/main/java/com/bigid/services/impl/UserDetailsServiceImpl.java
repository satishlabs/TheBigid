package com.bigid.services.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bigid.repository.UserRepository;
import com.bigid.web.common.Constants;

@Component("loginService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userDao;
	
	@Override
	public UserDetails loadUserByUsername(String userData)
			throws UsernameNotFoundException {
      
		     
		com.bigid.dao.entity.User user = userDao.findByUsername(userData);
		//com.bigid.dao.entity.User user = userDao.findByUsername(userData.substring(0,userData.indexOf(Constants.CEDILLA)));
         
		   List<SimpleGrantedAuthority> authorities = null;
		   if(user != null){  
			   
			   authorities = Arrays.asList( new SimpleGrantedAuthority("AUTHENTICATED"));
			   
			  /* for (Role role : user.getRoles()){
				   authorities.add(new SimpleGrantedAuthority(role.getName()));
		        }*/
			   
			   return new User(user.getUsername()+Constants.CEDILLA+user.getId(), user.getPassword(), authorities);
		   }
		   else{
			  throw new UsernameNotFoundException("authentication failed , wrong credentials");
		   }
	}

}
