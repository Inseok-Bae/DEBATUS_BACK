package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.DebatusUser;
import com.example.demo.repository.DebatusUserRepository;
import com.example.demo.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	DebatusUserRepository userRepository;
	
	@GetMapping("/exist")
	public boolean existById(@RequestParam("userId") String id) {
		return userRepository.existsById(id);
	}
	
	@GetMapping("/login")
	public Map<String, String> logIn(@RequestParam("userId") String id, @RequestParam("password") String password) {
		return userService.logIn(id, password);
	}
	
	@PostMapping
	public void register(@RequestBody DebatusUser user) {
		userRepository.save(user);
	}
	
}
