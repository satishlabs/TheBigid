package com.bigid.config;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

//@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler{
     
	/**
	 * authentication failure response 
	 */
	/*@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		 
		PrintWriter out = response.getWriter();

		out.print("{\"result\":\"failure\"}");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		out.close();
		
	}*/

}
