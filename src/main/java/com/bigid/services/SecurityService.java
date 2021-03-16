package com.bigid.services;

/**
 * Security service mainly related to authentication and authorization.
 * @author Dhirendra Singh
 *
 */
public interface SecurityService {
    
	String findLoggedInUsername();

    void autologin(String username, String password);
}