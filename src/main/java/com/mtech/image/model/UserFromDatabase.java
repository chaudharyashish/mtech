package com.mtech.image.model;

import java.util.Set;

public class UserFromDatabase {

	private Long id;
	private String username;
	private String password;
    private Set<Role> roles;
    private Set<Modules> modules;
    
	public UserFromDatabase(Long id, String username, String password, Set<Role> roles, Set<Modules> modules) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.modules = modules;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public Set<Modules> getModules() {
		return modules;
	}
	public void setModules(Set<Modules> modules) {
		this.modules = modules;
	}
    
    
}
