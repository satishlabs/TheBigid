package com.bigid.web.commands;

import com.bigid.common.enums.PostType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectTest {

	public static void main(String[] args) throws JsonProcessingException {
		ObjectMapper m =new ObjectMapper();
		UserCommand user = new UserCommand();
		user.setUsername("UniqueName");
		user.setPassword("password11111");
		user.setPasswordConfirm("password11111");
		user.setDateOfBirth("21-12-1999");
		user.setCity("mumbai");
		user.setEmail("a@a.com");
		
		System.out.println(m.writeValueAsString(user));
		
		PostCommentCommand cmd = new PostCommentCommand();
		cmd.setBody("This is comment for post ");
		cmd.setIsAnonymous(true);
		cmd.setLatitude(12.3354);
		cmd.setLongitude(-1.5584);
		System.out.println(m.writeValueAsString(cmd));
		
	}
}
