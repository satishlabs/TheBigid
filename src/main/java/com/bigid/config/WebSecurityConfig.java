package com.bigid.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bigid.services.impl.UserDetailsServiceImpl;
import com.bigid.web.filter.JsonCredentialAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService; 
	
	@Autowired
	private AuthenticationSuccessHandler loginSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler loginFailureHandler; 
	
	@Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

	/**
	  * The authentication provider below uses MongoDB 
	  * to authenticate
	  * @param auth
	  * @throws Exception
	  */
	@Autowired
    public void configureGlobal( AuthenticationManagerBuilder auth ) throws Exception
    {
	      auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());; 
	    	
	}
	
	/**
	 *   This is here to ensure that the static content (JavaScript, CSS, etc)
     *   is accessible from the login page without authentication.
	 *   list of urls to Ignore 
	 */
	@Override
	public void configure( WebSecurity web ) throws Exception
	{
		/*web
			.ignoring()
				.antMatchers( "/static/pos/fonts/**","/static/pos/images/**","/static/pos/js/**","/static/pos/styles/**",
						"/getProgressbardetail",
						"/static/pos/progressbar.htm",
						"/static/pos/templates/receipt.html",
						"/offline/server/syncinfo.service",
						"/offline/server/storeDetail.service",
						"/offline/server/getPosUpdateInfo.service",
						"/offline/server/dbStateAndUpdate.service",*/
						//"/*/ajax/**/*.service");
	}
	
	/**
	 *   The authorizeRequests configuration is where we specify what roles are allowed access to what areas.
	 *   we configure our login form.
	 *   the logout page and process is configured.
	 */
	@Override
    protected void configure( HttpSecurity http ) throws Exception
    {
		http.headers().frameOptions().disable().and()
		  .csrf().disable()
		  .exceptionHandling()
		  .accessDeniedHandler(accessDeniedHandler)
		  .authenticationEntryPoint(authenticationEntryPoint)
		.and()
        .authorizeRequests()
            .antMatchers("/static/**", "/notification/**","/resources/**","/user/signup","/registration","/welcome", "/home", "/", "/upload/**", "/s/**","/post/**","/user/**","/user/image/").permitAll()
            .anyRequest().authenticated()
            .and().addFilterAt(new JsonCredentialAuthenticationFilter(loginSuccessHandler, loginFailureHandler){
            			{
            				setAuthenticationManager(authenticationManagerBean());
            				
            			}
            		}, UsernamePasswordAuthenticationFilter.class)
        .formLogin()
        .successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler)
        .defaultSuccessUrl("/welcome")
        .loginPage("/login")
            .permitAll()
        .and()
        .logout()
            .permitAll();
             
    }
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Bean
	AuthenticationSuccessHandler successHandler() {
	    return new AuthenticationSuccessHandler() {
	      @Override
	      public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
	    	httpServletResponse.setContentType(MediaType.APPLICATION_JSON.toString());
	        httpServletResponse.getWriter().append("{\"result\":\"success\"}");
	        httpServletResponse.setStatus(200);
	      }
	    };
	  }
  	
  	  @Bean
	  AuthenticationFailureHandler failureHandler() {
	    return new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					org.springframework.security.core.AuthenticationException exception)
					throws IOException, ServletException {
				response.setContentType(MediaType.APPLICATION_JSON.toString());
				response.getWriter().append("{\"result\":\"failure\"}");
				response.setStatus(401);
				
			}
	    };
	  }
	  
	  @Bean
	  AccessDeniedHandler accessDeniedHandler() {
	    return new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					org.springframework.security.access.AccessDeniedException accessDeniedException)
					throws IOException, ServletException {
				response.getWriter().append("Access denied :(");
				response.setStatus(403);
			}
	    };
	  }
	  
	  @Bean
	  AuthenticationEntryPoint authenticationEntryPoint() {
	    return new AuthenticationEntryPoint() {

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					org.springframework.security.core.AuthenticationException authException)
					throws IOException, ServletException {
				response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
				
			}
	    };
	  }
}