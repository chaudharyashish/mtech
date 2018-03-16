package com.mtech.image.service;

import com.mtech.image.model.User;

public interface UserService {

	void save(User user);
	User findByUsername(String username);
}
