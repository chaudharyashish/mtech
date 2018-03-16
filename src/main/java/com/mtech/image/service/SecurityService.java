package com.mtech.image.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.mtech.image.model.UserToken;

public interface SecurityService {

	void autologin(String username, String password);
	UserToken generateAccessToken(UserDetails user);
	UserDetails findLoggedInUser();
}
