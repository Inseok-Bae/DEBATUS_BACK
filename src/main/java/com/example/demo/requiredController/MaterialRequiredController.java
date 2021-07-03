package com.example.demo.requiredController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.MaterialRepository;
import com.example.demo.service.MaterialService;
import com.example.demo.vo.MaterialPostModel;

@CrossOrigin
@RestController
@RequestMapping("/api/required/material")
public class MaterialRequiredController {

	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	MaterialService materialService;
	
	@PostMapping
	public Long postMaterial(@RequestBody MaterialPostModel model, @RequestHeader("jwt-auth-token") String token) {
		return materialService.postMaterial(model, token);
	}
	
}
