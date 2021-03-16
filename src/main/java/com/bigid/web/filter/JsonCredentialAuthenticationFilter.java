package com.bigid.web.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Filter which takes login credentials in json format{username:aaa, password:aaa} and do the authentication.
 * This authentication is extension to default authentication filter applied by spring security {@link UsernamePasswordAuthenticationFilter}
 * with capability to use json response, if authentication is requested using AJAX technique, otherwise normal default handlers works as usual.
 * @author Dhirendra Singh
 *
 TODO: Normal login may not be needed in future at that time remove the login for default handlers.
 */
public class JsonCredentialAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private static final Object XMLHTTP_REQUEST_HEADER = "XMLHttpRequest";
	private boolean postOnly = true;
	private AuthenticationSuccessHandler ajaxSuccessHandler, defaultSuccessHandler;
	private AuthenticationFailureHandler ajaxFailureHandler, defaultFailureHandler;
	
	public JsonCredentialAuthenticationFilter(AuthenticationSuccessHandler successHandler,
			AuthenticationFailureHandler failureHandler) {
		defaultFailureHandler = super.getFailureHandler();
		defaultSuccessHandler = super.getSuccessHandler();
		ajaxSuccessHandler = successHandler;
		ajaxFailureHandler = failureHandler;
	}



	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		
		String username = null, password = null, ajaxHttpHeader = null;
		
		boolean isJsonLoginRequest = false;
		//checking is login requested using AJAX technique and content-type as application/json
		if(request.getContentType().toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE) && 
				((ajaxHttpHeader = request.getHeader("X-Requested-With")) != null 
				&& XMLHTTP_REQUEST_HEADER.equals(ajaxHttpHeader))){
			isJsonLoginRequest = true;
			try {
				
					UserCredential credential = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
							.readValue(request.getInputStream(),UserCredential.class);
					
					if (StringUtils.isEmpty(credential.getUsername()) || StringUtils.isEmpty(credential.getPassword())) {
			            throw new AuthenticationServiceException("Username or Password not provided");
			        }
					username = credential.getUsername();
					password = credential.getPassword();
					
				} catch (IOException e) {
					throw new AuthenticationServiceException("REST login credential cannot be parsed. Parsing failed.");
			}
			
		} else {
			
			username = obtainUsername(request);
			password = obtainPassword(request);
	
			if (username == null) {
				username = "";
			}
	
			if (password == null) {
				password = "";
			}
	
			username = username.trim();
		}
		
		//setting ajax based handlers when request is made through ajax call
		setAuthenticationSuccessHandler(isJsonLoginRequest ? ajaxSuccessHandler : defaultSuccessHandler);
		setAuthenticationFailureHandler(isJsonLoginRequest ?  ajaxFailureHandler : defaultFailureHandler);

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return getAuthenticationManager().authenticate(authRequest);
	}
	
	
	
	static class UserCredential{
		String username;
		String password;
		
		//default constructor
		public UserCredential() {
		}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
	}

}
