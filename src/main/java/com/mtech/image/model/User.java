 package com.mtech.image.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "user")
public class User extends org.springframework.security.core.userdetails.User {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5698598250645942130L;
	private Long id;
    //private String passwordConfirm;
    private String username = super.getUsername();
	private String password = super.getPassword();
    private Set<Role> roles;
    private Set<Modules> modules;
    
    public User() {
    	this("dummy username", "dummy password", new HashSet<>());
    }

	public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public User(String username, String password, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities, 
            Set<Modules> modules, Set<Role> roles) {
		
    	super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
		
    	this.modules = modules;
    	this.roles = roles;
	}

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    /* 

    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }*/

    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "user_module_mapping", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "module_id"))
	public Set<Modules> getModules() {
		return modules;
	}

    public void setModules(Set<Modules> modules) {
    	this.modules = modules;
    }
	
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name = "user_role_mapping", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
   
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
 
}