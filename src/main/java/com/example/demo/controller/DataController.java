package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DataService;
import com.example.demo.vo.SubViewModel;

@CrossOrigin
@RestController
@RequestMapping("/api/data")
public class DataController {

	@Autowired
	DataService dataService;
	
	@GetMapping
	public SubViewModel getSubViewModel(@RequestParam Long issueId) {
		return dataService.getSubViewModels(issueId, "byIssue");
	}
	
	@GetMapping("/byTopicId")
	public SubViewModel getSubViewModelByTopicId(@RequestParam Long topicId) {
		return dataService.getSubViewModels(topicId, "byTopic");
	} 
	
}
