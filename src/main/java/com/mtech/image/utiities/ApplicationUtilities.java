package com.mtech.image.utiities;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mtech.image.model.UserToken;

@Component
public class ApplicationUtilities {

	public static UserToken generateTokenAtLogin(UserDetails user) {
		UserToken userToken = new UserToken();
		userToken.setToken(LocalDateTime.now().toString());
		userToken.setUsername(user.getUsername());
		userToken.setGenerationDate(new Date());
		userToken.setExpiryDate(new Date(System.currentTimeMillis()+(5*60*1000)));
		try {
			userToken.setIpAddress(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userToken;
	}
}
