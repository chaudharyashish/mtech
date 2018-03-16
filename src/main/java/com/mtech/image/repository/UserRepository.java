package com.mtech.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mtech.image.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    
    //List<UserFromDatabase> fetchUserAsDTO(@Param("username") String username);
    
   
}