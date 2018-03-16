package com.mtech.image.utiities;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.mtech.image.model.UserToken;
import com.mtech.image.repository.UserTokenRepository;

public class CustomLogoutHandler implements LogoutHandler{

	@Autowired
	UserTokenRepository userTokenRepository;
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Enumeration<String> params = request.getParameterNames();
		String token = null;
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			token = request.getParameter(paramName); 
		}
		
		if(token != null) {
			UserToken userToken = userTokenRepository.findByToken(token);
			if(userToken != null) {
				
				//delete token
				userTokenRepository.delete(userToken.getId());
			}
		}
		authentication = null;
		request.getSession().invalidate();
		try {
			response.sendRedirect("login?logout");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
