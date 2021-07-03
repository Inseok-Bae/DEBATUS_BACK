package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Topic;
import com.example.demo.repository.TopicRepository;
import com.example.demo.service.TopicService;

@CrossOrigin
@RestController
@RequestMapping("/api/topic")
public class TopicController {

	@Autowired
	TopicRepository topicRepository;
	
	@Autowired
	TopicService topicService;
	
	@GetMapping
	public List<Topic> getAllTopics() {
		return topicRepository.findAll();
	}
	
	@GetMapping("/byPage")
	public Page<Topic> getAllTopicsByPage(Pageable pageable) {
		return topicRepository.findAll(pageable);
	}
	
	@GetMapping("/byPagePreview")
	public Map<String, Object> getAllTopicPreviewsByPage(Pageable pageable) {
		return topicService.getPreviews(pageable);
	}
	
	@GetMapping("/byContent")
	public List<Topic> getTopicsByContent(@RequestParam("title") String title, @RequestParam("text") String text) {
		List<Topic> result = new ArrayList<>();
		result.addAll(topicRepository.findByTitleLike("%" + title + "%"));
		result.addAll(topicRepository.findByTextLike("%" + text + "%"));
		return result;
	}
	
	@GetMapping("/byId")
	public Topic getTopicById(@RequestParam("id") Long id) {
		return topicRepository.findById(id).orElseThrow();
	}
	
	@GetMapping("/byIssueId")
	public List<Topic> getTopicsByIssueId(@RequestParam("id") Long id) {
		return topicService.getTopicsByIssueId(id);
	}
	
}
