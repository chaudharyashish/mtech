package com.mtech.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mtech.image.model.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

	UserToken findByToken(String token);
	
	UserToken findByIpAddress(String ipAddress);
}
