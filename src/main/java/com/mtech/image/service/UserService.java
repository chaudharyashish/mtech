package com.mtech.image.service;

import com.mtech.image.model.User;
import com.mtech.image.model.UserForm;

public interface UserService {

	void save(UserForm userForm);
	User findByUsername(String username);
}
