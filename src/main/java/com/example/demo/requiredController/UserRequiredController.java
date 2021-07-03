package com.example.demo.requiredController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Issue;
import com.example.demo.repository.DebatusUserRepository;
import com.example.demo.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/required/user")
public class UserRequiredController {

	@Autowired
	UserService userService;
	
	@Autowired
	DebatusUserRepository userRepository;
	
	@GetMapping("/publishableIssues")
	public List<Issue> getPublishableIssues(@RequestHeader("jwt-auth-token") String token) {
		return userService.getPublishableIssues(token);
	}
	
}
