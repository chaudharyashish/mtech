package com.mtech.image.utiities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public CustomAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {

		String moduleParam = UrlUtils.buildRequestUrl(request);
		String redirect = super.determineUrlToUseForThisRequest(request, response, exception);
		return UriComponentsBuilder.fromPath(redirect).queryParam("module", moduleParam).toUriString();
	}
}
