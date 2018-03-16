package com.mtech.image.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtech.image.model.Modules;
import com.mtech.image.model.Role;
import com.mtech.image.model.User;
import com.mtech.image.repository.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
    	User userFromDatabase = userRepository.findByUsername(username);
    	
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : userFromDatabase.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        Set<Modules> modules = new HashSet<>();
        for(Modules module : userFromDatabase.getModules()) {
        	modules.add(new Modules(module.getId(), module.getName(), module.getUsers()));
        }
        
        Set<Role> roles = new HashSet<>();
        for(Role role : userFromDatabase.getRoles()) {
        	roles.add(new Role(role.getId(), role.getAuthority(), role.getUsers()));
        }
        
        User userToReturn = new User(userFromDatabase.getUsername(), userFromDatabase.getPassword(), 
        							userFromDatabase.isEnabled(), userFromDatabase.isAccountNonExpired(), 
        							userFromDatabase.isAccountNonExpired(), userFromDatabase.isAccountNonLocked(), 
        							grantedAuthorities, modules, roles);
        return userToReturn;
        
        
        //User userToReturn = new User(username, userFromDatabase.getPassword(), true, true, true, true, grantedAuthorities, userFromDatabase.getModules());
        //return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}