package com.mtech.image.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtech.image.model.Role;
import com.mtech.image.model.User;
import com.mtech.image.model.UserForm;
import com.mtech.image.repository.RoleRepository;
import com.mtech.image.service.SecurityService;
import com.mtech.image.service.UserService;
import com.mtech.image.utiities.ApplicationConstants;
import com.mtech.image.validators.UserValidator;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private RoleRepository roleRepository;

	//Welcome
	@RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
	public ModelAndView welcome(Model model, @RequestParam(name="module", defaultValue="sso") String moduleName,
			HttpServletResponse response,
			HttpSession httpSession) {
		ModelAndView modelAndView = new ModelAndView();
		if(moduleName.equals(ApplicationConstants.ATTENDANCE_MODULE)) {
			return new ModelAndView("redirect:http://localhost:8082/"+moduleName+"/welcome"+moduleName+"?token="+httpSession.getAttribute("token"));
		}
		else if(moduleName.equals(ApplicationConstants.TIMESHEET_MODULE)) {
			return new ModelAndView("redirect:http://localhost:8081/"+moduleName+"/welcome"+moduleName+"?token="+httpSession.getAttribute("token"));
		}
		else {
			User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(null != loggedInUser) {
				modelAndView.addObject("user", loggedInUser);
				modelAndView.setViewName("welcome");
				return modelAndView;
			}
			else {
				return new ModelAndView("redirect:/logout");    		
			}
		}
	}

	//Login
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(name="module", defaultValue="sso") String moduleName,
			Model model, String error, String logout) {
		
		ModelAndView modelAndView = new ModelAndView();
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");
		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");
		
		modelAndView.setViewName("login");
		modelAndView.addObject("moduleName", moduleName);
		return modelAndView;
	}

	//Registration
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("userForm", new UserForm());
		return "registration";
	}



	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model) {
		userValidator.validate(userForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		List<Role> roles = roleRepository.findAll();
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (Role role : roles){
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		User user = new User(userForm.getUsername(), userForm.getPassword(), grantedAuthorities);
		userService.save(user);
		securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
		return "redirect:/welcome";
	}

    @RequestMapping(value="autologinsso")
    @ResponseBody
    public void autoLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
    	securityService.autologin(password, password);
    }
}
