package com.mtech.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mtech.image.utiities.CustomAuthenticationEntryPoint;
import com.mtech.image.utiities.CustomAuthenticationSuccessHandler;
import com.mtech.image.utiities.CustomLogoutHandler;
import com.mtech.image.utiities.CustomUsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    	return new CustomAuthenticationSuccessHandler();
    }
    
    @Bean
    public CustomLogoutHandler customLogoutHandler() {
    	return new CustomLogoutHandler();
    }
    
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
    	return new CustomUsernamePasswordAuthenticationFilter();
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
                http
                	.exceptionHandling()
                	.authenticationEntryPoint(new CustomAuthenticationEntryPoint("/login"))
                	.and()
                	
                	.authorizeRequests()
                	.antMatchers("/login").permitAll()
                	.antMatchers("/downloadFile").permitAll()
                    .antMatchers("/resources/**", "/registration").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    
                    .formLogin()
                    //.loginPage("/login")
                    .successHandler(customAuthenticationSuccessHandler())
                    .permitAll()
                    .and()

                    //.addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                	
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .addLogoutHandler(customLogoutHandler())
                    .logoutSuccessUrl("/login")
                    .permitAll()
                    
                    .and()
                    .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}